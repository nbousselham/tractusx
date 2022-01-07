package com.csds.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csds.api.DataSpaceConnectorUsagePolicyExample;
import com.csds.api.DataspaceConnectorArtifactsApi;
import com.csds.api.DataspaceConnectorCatalogsApi;
import com.csds.api.DataspaceConnectorContractsApi;
import com.csds.api.DataspaceConnectorOffersApi;
import com.csds.api.DataspaceConnectorRepresentationsApi;
import com.csds.api.DataspaceConnectorRulesApi;
import com.csds.constant.ApplicationMessageConstant;
import com.csds.constants.APIConstants;
import com.csds.entity.DataOfferEntity;
import com.csds.entity.ProviderControlPanelConnectorDetails;
import com.csds.exception.ServiceException;
import com.csds.exception.ValidationException;
import com.csds.model.ArtifactDescription;
import com.csds.model.CatalogDescription;
import com.csds.model.CatalogList;
import com.csds.model.ContractDescription;
import com.csds.model.GetListResponse;
import com.csds.model.OfferDescription;
import com.csds.model.OfferFile;
import com.csds.model.OfferRequest;
import com.csds.model.ResourceRepresentationDescription;
import com.csds.model.RuleDescription;
import com.csds.model.UsagePolicyDetails;
import com.csds.model.UsagePolicyExampleRequest;
import com.csds.model.UsagePolicyType;
import com.csds.repository.DataOfferMongoRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderControlPanelService {

	private static final String CATALOG_NAME = "RECYCLER_CATALOG";

	@Autowired
	private APIConstants aPIConstants;

	private static ProviderControlPanelConnectorDetails connectorDetails;
	private static URI connectorProviderBaseUrl;

	private final DataspaceConnectorOffersApi offersApi;
	private final DataspaceConnectorCatalogsApi catalogsApi;
	private final DataspaceConnectorRulesApi rulesApi;
	private final DataspaceConnectorContractsApi contractsApi;
	private final DataspaceConnectorRepresentationsApi representationsApi;
	private final DataspaceConnectorArtifactsApi artifactsApi;
	private final DataSpaceConnectorUsagePolicyExample usagePolicyExampleApi;

	@Autowired
	private DataOfferMongoRepository dataOfferMongoRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations operations;

	public List<DataOfferEntity> getAllDataOffers() {
		return dataOfferMongoRepository.findAll();
	}

	public OfferFile getOfferFile(String id) throws IllegalStateException, IOException {
		GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		OfferFile offerFile = new OfferFile();
		offerFile.setTitle(file.getMetadata().get("filetitle").toString());
		offerFile.setStream(operations.getResource(file).getInputStream());
		return offerFile;
	}

	private String getAuthHeader() {
		val auth = connectorDetails.getConnectorUsername() + ":" + connectorDetails.getConnectorPassword();
		String encoding1 = "";
		try {
			encoding1 = Base64.getEncoder().encodeToString((auth.toString()).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "Basic " + encoding1;
	}

	public Object createDataOffer(OfferRequest offerRequest, MultipartFile file) throws Exception {

		try {
			connectorDetails = aPIConstants.getDatils();
			connectorProviderBaseUrl = new URI(connectorDetails.getConnectorbaseURL());

			Map<String, Object> offerIDSdetails = new HashMap<>();

			validateOfferRequestData(offerRequest);

			if (file == null || file.isEmpty()) {
				throw new ValidationException("The input file should not be null or empty");
			}

			// create offer
			var offerResponse = offersApi.registerOffer(connectorProviderBaseUrl, getAuthHeader(),
					getOfferDescription(offerRequest));
			var offerId = offerResponse.getUUIDFromLink();
			var offerSelfHref = offerResponse.getSelfHref();

			// get or create catalog
			var catalogResponse = Optional
					.ofNullable(catalogsApi.getAllCatalogs(connectorProviderBaseUrl, getAuthHeader()))
					.map(GetListResponse::getEmbedded).map(CatalogList::getCatalogs).orElseGet(ArrayList::new).stream()
					.filter(it -> CATALOG_NAME.equals(it.getTitle())).findFirst().orElseGet(() -> catalogsApi
							.createCatalog(connectorProviderBaseUrl, getAuthHeader(), getCatalogDescription()));
			var catalogId = catalogResponse.getUUIDFromLink();

			// link offer with catalog
			catalogsApi.linkOffer(connectorProviderBaseUrl, getAuthHeader(), catalogId, List.of(offerSelfHref));

			if (!Optional.of(offerRequest.getUsageControl()).isPresent()) {
				offerRequest.setUsageControl(new ArrayList<>());
			}

			offerRequest.getByOrganization().stream().forEach(organization -> {
				if (organization.getRole() != null && organization.getRole().equals("Recycler")) {
					UsagePolicyDetails usagePolicy = new UsagePolicyDetails();
					usagePolicy.setType("CONNECTOR_RESTRICTED_USAGE");
					usagePolicy.setUrl(organization.getBaseUrl());
					offerRequest.getUsageControl().add(usagePolicy);
				}
			});

			List<String> ruleSelfHref = new ArrayList<>();
			List<String> ruleIdList = new ArrayList<>();
			if (Optional.of(offerRequest.getUsageControl()).isPresent()) {

				offerRequest.getUsageControl().stream().forEach(usagePolicy -> {
					// Get Usage policy example
					var usagePolicyResponse = usagePolicyExampleApi.getUsagePolicyExample(connectorProviderBaseUrl,
							getAuthHeader(), getUsagePolicyExampleRequest(offerRequest, usagePolicy));
					// create rule
					var ruleResponse = rulesApi.registerRule(connectorProviderBaseUrl, getAuthHeader(),
							getRuleDescription(usagePolicy, usagePolicyResponse));

					ruleIdList.add(ruleResponse.getUUIDFromLink().toString());
					ruleSelfHref.add(ruleResponse.getSelfHref());
				});
			}

			// create contract
			var contractResponse = contractsApi.createContract(connectorProviderBaseUrl, getAuthHeader(),
					getContractDescription(offerRequest));
			var contractId = contractResponse.getUUIDFromLink();

			contractsApi.linkRules(connectorProviderBaseUrl, getAuthHeader(), contractId, ruleSelfHref);
			contractsApi.linkOffers(connectorProviderBaseUrl, getAuthHeader(), contractId, List.of(offerSelfHref));

			// create representation
			var representationResponse = representationsApi.registerRepresentation(connectorProviderBaseUrl,
					getAuthHeader(), getRepresentation(offerRequest));
			var representationId = representationResponse.getUUIDFromLink();
			var representationSelfHref = representationResponse.getSelfHref();

			// create artifact
			var artifactDescription = getArtifactDescription(offerRequest, file);
			var artifactResponse = artifactsApi.registerArtifact(connectorProviderBaseUrl, getAuthHeader(),
					artifactDescription);
			var artifactId = artifactResponse.getUUIDFromLink();
			var artifactSelfHref = artifactResponse.getSelfHref();

			// link artifact with representation
			representationsApi.linkArtifacts(connectorProviderBaseUrl, getAuthHeader(), representationId,
					List.of(artifactSelfHref));

			// link representation with resource
			offersApi.linkRepresentations(connectorProviderBaseUrl, getAuthHeader(), offerId,
					List.of(representationSelfHref));

			offerIDSdetails.put("offerId", offerId);
			offerIDSdetails.put("offerSelfHref", offerSelfHref);
			offerIDSdetails.put("catalogId", catalogId);
			offerIDSdetails.put("ruleIdList", ruleIdList);
			offerIDSdetails.put("ruleSelfHref", ruleSelfHref);
			offerIDSdetails.put("contractId", contractId);
			offerIDSdetails.put("representationId", representationId);
			offerIDSdetails.put("representationSelfHref", representationSelfHref);
			offerIDSdetails.put("artifactId", artifactId);
			offerIDSdetails.put("artifactSelfHref", artifactSelfHref);

			log.info(
					"Created: \n-------------------Offer: {}\n" + "-------------------Catalog: {}\n"
							+ "-------------------Rule: {}\n" + "-------------------Contract: {}\n"
							+ "-------------------Representation: {}\n" + "-------------------Artifact: {}",
					offerId, catalogId, ruleIdList, contractId, representationId, artifactId);

			return saveOfferInMongoDB(offerRequest, offerIDSdetails, file);

		} catch (ValidationException |

				ServiceException e) {
			throw e;
		}
	}

	public Object updateDataOffer(OfferRequest offerRequest, MultipartFile file) throws Exception {

		try {
			connectorDetails = aPIConstants.getDatils();
			connectorProviderBaseUrl = new URI(connectorDetails.getConnectorbaseURL());

			Map<String, Object> offerIDSdetails = new HashMap<>();

			var dataOffer = Optional.of(dataOfferMongoRepository.findById(offerRequest.getId())).get()
					.orElseThrow(() -> new ValidationException("Offer id is not valid or not present"));

			validateOfferRequestData(offerRequest);

			UUID offerId = UUID.fromString(dataOffer.getOfferIDSdetails().get("offerId").toString());
			var offerSelfHref = dataOffer.getOfferIDSdetails().get("offerSelfHref").toString();
			UUID catalogId = UUID.fromString(dataOffer.getOfferIDSdetails().get("catalogId").toString());

			// update offer
			offersApi.updateOffer(connectorProviderBaseUrl, getAuthHeader(), offerId,
					getOfferDescription(offerRequest));

			// Note:
			// 1. skip catalog creation and catlog to offer linking steps
			// 2. Delete all existing rules and their contract mapping in update offer case

			@SuppressWarnings("unchecked")
			List<String> ruleList = (ArrayList<String>) dataOffer.getOfferIDSdetails().get("ruleIdList");

			ruleList.stream().forEach(rule -> {
				UUID ruleId = UUID.fromString(rule);
				rulesApi.deleteRule(connectorProviderBaseUrl, getAuthHeader(), ruleId);
			});

			UUID existingContractId = UUID.fromString(dataOffer.getOfferIDSdetails().get("contractId").toString());
			contractsApi.deleteContract(connectorProviderBaseUrl, getAuthHeader(), existingContractId);

			if (!Optional.of(offerRequest.getUsageControl()).isPresent()) {
				offerRequest.setUsageControl(new ArrayList<>());
			}

			offerRequest.getByOrganization().stream().forEach(organization -> {
				if (organization.getRole() != null && organization.getRole().equals("Recycler")) {
					UsagePolicyDetails usagePolicy = new UsagePolicyDetails();
					usagePolicy.setType("CONNECTOR_RESTRICTED_USAGE");
					usagePolicy.setUrl(organization.getBaseUrl());
					offerRequest.getUsageControl().add(usagePolicy);
				}
			});

			List<String> ruleSelfHref = new ArrayList<>();
			List<String> ruleIdList = new ArrayList<>();
			if (Optional.of(offerRequest.getUsageControl()).isPresent()) {

				offerRequest.getUsageControl().stream().forEach(usagePolicy -> {
					// Get Usage policy example
					var usagePolicyResponse = usagePolicyExampleApi.getUsagePolicyExample(connectorProviderBaseUrl,
							getAuthHeader(), getUsagePolicyExampleRequest(offerRequest, usagePolicy));
					// create rule
					var ruleResponse = rulesApi.registerRule(connectorProviderBaseUrl, getAuthHeader(),
							getRuleDescription(usagePolicy, usagePolicyResponse));

					ruleIdList.add(ruleResponse.getUUIDFromLink().toString());
					ruleSelfHref.add(ruleResponse.getSelfHref());
				});
			}

			// create contract
			var contractResponse = contractsApi.createContract(connectorProviderBaseUrl, getAuthHeader(),
					getContractDescription(offerRequest));
			var contractId = contractResponse.getUUIDFromLink();

			contractsApi.linkRules(connectorProviderBaseUrl, getAuthHeader(), contractId, ruleSelfHref);
			contractsApi.linkOffers(connectorProviderBaseUrl, getAuthHeader(), contractId, List.of(offerSelfHref));

			// update representation
			var representationId = UUID.fromString(dataOffer.getOfferIDSdetails().get("representationId").toString());
			var representationSelfHref = dataOffer.getOfferIDSdetails().get("representationSelfHref").toString();

			representationsApi.updateRepresentation(connectorProviderBaseUrl, getAuthHeader(), representationId,
					getRepresentation(offerRequest));

			var artifactId = UUID.fromString(dataOffer.getOfferIDSdetails().get("artifactId").toString());
			var artifactSelfHref = dataOffer.getOfferIDSdetails().get("artifactSelfHref").toString();

			// update artifact
			if (file != null && !file.isEmpty()) {
				var artifactDescription = getArtifactDescription(offerRequest, file);
				artifactsApi.updateArtifact(connectorProviderBaseUrl, getAuthHeader(), artifactId, artifactDescription);
				ObjectId id = saveFileInMongoDB(offerRequest, file);

				String fileName = file.getOriginalFilename();
				dataOffer.setFileName(fileName);

				dataOffer.setFileId(id.toString());
			}

			// link artifact with representation
			representationsApi.linkArtifacts(connectorProviderBaseUrl, getAuthHeader(), representationId,
					List.of(artifactSelfHref));

			// link representation with resource
			offersApi.linkRepresentations(connectorProviderBaseUrl, getAuthHeader(), offerId,
					List.of(representationSelfHref));

			offerIDSdetails.put("offerId", offerId);
			offerIDSdetails.put("offerSelfHref", offerSelfHref);
			offerIDSdetails.put("catalogId", catalogId);
			offerIDSdetails.put("ruleIdList", ruleIdList);
			offerIDSdetails.put("ruleSelfHref", ruleSelfHref);
			offerIDSdetails.put("contractId", contractId);
			offerIDSdetails.put("representationId", representationId);
			offerIDSdetails.put("representationSelfHref", representationSelfHref);
			offerIDSdetails.put("artifactId", artifactId);
			offerIDSdetails.put("artifactSelfHref", artifactSelfHref);

			log.info(
					"Updated details: \n-------------------Offer: {}\n" + "-------------------Catalog: {}\n"
							+ "-------------------Rule: {}\n" + "-------------------Contract: {}\n"
							+ "-------------------Representation: {}\n" + "-------------------Artifact: {}",
					offerId, catalogId, ruleIdList, contractId, representationId, artifactId);

			dataOffer.setTitle(offerRequest.getTitle());
			dataOffer.setDescription(offerRequest.getDescription());
			dataOffer.setAccessControlUseCase(offerRequest.getAccessControlUseCase());
			dataOffer.setAccessControlUseCaseType(offerRequest.getAccessControlUseCaseType());
			dataOffer.setByOrganization(offerRequest.getByOrganization());
			dataOffer.setByOrganizationRole(offerRequest.getByOrganizationRole());
			dataOffer.setContractEndsinDays(offerRequest.getContractEndsinDays());
			dataOffer.setUsageControl(offerRequest.getUsageControl());
			dataOffer.setOfferIDSdetails(offerIDSdetails);
			dataOffer.setAccessControlByRoleType(offerRequest.getAccessControlByRoleType());
			dataOffer.setUsageControlType(offerRequest.getUsageControlType());

			return dataOfferMongoRepository.save(dataOffer);

		} catch (ValidationException | ServiceException e) {
			throw e;
		}
	}

	private DataOfferEntity saveOfferInMongoDB(OfferRequest offerRequest, Map<String, Object> offerIDSdetails,
			MultipartFile file) throws ServiceException {

		ObjectId id = saveFileInMongoDB(offerRequest, file);

		String fileName = file.getOriginalFilename();

		DataOfferEntity dataOfferEntity = DataOfferEntity.builder().title(offerRequest.getTitle()).fileName(fileName)
				.fileId(id.toString()).description(offerRequest.getDescription())
				.accessControlUseCase(offerRequest.getAccessControlUseCase())
				.accessControlUseCaseType(offerRequest.getAccessControlUseCaseType())
				.byOrganization(offerRequest.getByOrganization())
				.byOrganizationRole(offerRequest.getByOrganizationRole())
				.contractEndsinDays(offerRequest.getContractEndsinDays()).usageControl(offerRequest.getUsageControl())
				.offerIDSdetails(offerIDSdetails)
				.accessControlByRoleType(offerRequest.getAccessControlByRoleType())
				.usageControlType(offerRequest.getUsageControlType())
				.build();

		return dataOfferMongoRepository.save(dataOfferEntity);
	}

	private ObjectId saveFileInMongoDB(OfferRequest offerRequest, MultipartFile file) throws ServiceException {

		ObjectId id = null;
		try {
			DBObject metaData = new BasicDBObject();
			metaData.put("filetitle", offerRequest.getTitle());
			id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData);
		} catch (IOException e) {
			throw new ServiceException("Error while saving file in mongo database:" + e.getMessage());
		}
		return id;
	}

	private OfferDescription getOfferDescription(OfferRequest offerRequest) {
		return OfferDescription.builder().title(offerRequest.getTitle()).description(offerRequest.getDescription())
				.publisher(connectorProviderBaseUrl).language("EN").sovereign(URI.create("http://localhost")).build();
	}

	private CatalogDescription getCatalogDescription() {
		return CatalogDescription.builder().title(CATALOG_NAME).description(CATALOG_NAME).build();
	}

	private UsagePolicyExampleRequest getUsagePolicyExampleRequest(OfferRequest offerRequest,
			UsagePolicyDetails policy) {

		String policyName = policy.getType();
		UsagePolicyType policyType = UsagePolicyType.valueOf(policyName);

		UsagePolicyExampleRequest usageExampleInput = UsagePolicyExampleRequest.builder()
				.title(offerRequest.getTitle() + " " + policyName + " tilte")
				.description(offerRequest.getDescription() + " " + policyName + " description").type(policyType.label)
				.build();

		switch (policyType) {
		case N_TIMES_USAGE:
			usageExampleInput.setValue(policy.getValue());
			break;
		case USAGE_DURING_INTERVAL:
			usageExampleInput.setStart(policy.getStartDate());
			usageExampleInput.setEnd(policy.getEndDate());
			break;
		case DURATION_USAGE:
			usageExampleInput.setDuration(policy.getDuration());
			break;
		case CONNECTOR_RESTRICTED_USAGE:
			usageExampleInput.setUrl(policy.getUrl());
		default:
			break;
		}

		return usageExampleInput;
	}

	private RuleDescription getRuleDescription(UsagePolicyDetails policy, String usagePolicyResponse) {
		return RuleDescription.builder().title(policy.getType()).value(usagePolicyResponse).build();
	}

	private ContractDescription getContractDescription(OfferRequest offerRequest) {

		Integer contractendsindayas = offerRequest.getContractEndsinDays() == null ? 1
				: offerRequest.getContractEndsinDays();
		String CONTRACT_END_DATE = LocalDate.now().plusDays(contractendsindayas).atStartOfDay(ZoneOffset.UTC)
				.toString();

		return ContractDescription.builder().title(offerRequest.getTitle() + " contract")
				.start(ZonedDateTime.now(ZoneOffset.UTC).toString()).end(CONTRACT_END_DATE).build();
	}

	private ResourceRepresentationDescription getRepresentation(OfferRequest offerRequest) {
		return ResourceRepresentationDescription.builder().title(offerRequest.getTitle() + " representation")
				.description(offerRequest.getDescription() + " representation")
				.mediaType(MediaType.APPLICATION_JSON_VALUE).language("EN").build();
	}

	private ArtifactDescription getArtifactDescription(OfferRequest offerRequest, MultipartFile file) throws Exception {
		ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
		String fileContent = IOUtils.toString(stream, "UTF-8");
		return ArtifactDescription.builder().title(offerRequest.getTitle() + " artifact").value(fileContent).build();
	}

	private void validateOfferRequestData(OfferRequest offerRequest) {

		if (offerRequest.getTitle() != null && offerRequest.getTitle().equals("")) {
			throw new ValidationException(String.format(ApplicationMessageConstant.REQUIRED_FIELD, "title"));
		}

		if (offerRequest.getAccessControlUseCaseType() != null
				&& offerRequest.getAccessControlUseCaseType().equals("")) {
			throw new ValidationException(
					String.format(ApplicationMessageConstant.REQUIRED_FIELD, "access control usage"));
		}

		if (offerRequest.getByOrganizationRole() != null && offerRequest.getByOrganizationRole().isEmpty()) {
			throw new ValidationException(
					String.format(ApplicationMessageConstant.REQUIRED_FIELD, "Organization role"));
		}

		if (offerRequest.getByOrganization() != null && offerRequest.getByOrganization().isEmpty()) {
			throw new ValidationException(String.format(ApplicationMessageConstant.REQUIRED_FIELD, "Organization"));
		}

	}

}
