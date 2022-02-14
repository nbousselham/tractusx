package com.csds.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.csds.api.ConsumerArtifactAPI;
import com.csds.api.ConsumerContractAPI;
import com.csds.api.ConsumerDescriptionAPI;
import com.csds.api.ProviderOfferDetailsAPI;
import com.csds.entity.ContractAgreementsInformation;
import com.csds.exception.NoDataFoundException;
import com.csds.model.ConsumerOfferDescriptionResponseCatalog;
import com.csds.model.ConsumerOfferDescriptionResponseOffer;
import com.csds.model.ConsumerOfferResource;
import com.csds.model.ConsumerOfferResourceContractPermission;
import com.csds.model.ConsumerOfferResourceRepresentationArtifact;
import com.csds.model.ContractAgreementsResponse;
import com.csds.model.DataOfferDetails;
import com.csds.model.OrganizationDetails;
import com.csds.model.QueryDataOfferModel;
import com.csds.repository.ContractAgreementsInformationMongoRepository;
import com.csds.response.ResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerControlPanelService implements ProcessOnDataOffer {

	private static final String IDS_DATA_URI = "/ids/data";

	private static final String NEGOTIATE_CONTRACT_PART1 = "{\n" + "    \"@type\": \"ids:Permission\",\n"
			+ "    \"@id\": \"";

	private static final String NEGOTIATE_CONTRACT_PART2 = "\",\n   \"ids:description\": [\n" + "      {\n"
			+ "        \"@value\": \"";

	private static final String NEGOTIATE_CONTRACT_PART3 = "\",\n \"@type\": \"http://www.w3.org/2001/XMLSchema#string\"\n"
			+ "      }\n" + "    ],\n" + "    \"ids:title\": [\n" + "      {\n"
			+ "        \"@value\": \"Allow Data Usage\",\n"
			+ "        \"@type\": \"http://www.w3.org/2001/XMLSchema#string\"\n" + "      }\n" + "    ],\n"
			+ "    \"ids:action\": [\n" + "      {\n" + "        \"@id\": \"https://w3id.org/idsa/code/USE\"\n"
			+ "      }\n" + "    ],\n" + "    \"ids:postDuty\": [],\n" + "    \"ids:assignee\": [],\n"
			+ "    \"ids:assigner\": [],\n" + "    \"ids:constraint\": [],\n" + "    \"ids:preDuty\": [],\n"
			+ "    \"ids:target\":\"";

	private static final String NEGOTIATE_CONTRACT_PART4 = "\"\n" + "}";

	private static final ObjectMapper mapper = new ObjectMapper();

	private final ConsumerDescriptionAPI consumerDescriptionAPI;
	private final ConsumerContractAPI consumerContractAPI;
	private final ConsumerArtifactAPI consumerArtifactAPI;
	private final ProviderOfferDetailsAPI providerOfferDetailsAPI;

	@Autowired
	private ContractAgreementsInformationMongoRepository contractAgreementsInformationMongoRepository;

	public List<ContractAgreementsResponse> readContractAgreementsInformation() {

		List<ContractAgreementsResponse> contractAgreementsResponseList = Optional
				.ofNullable(contractAgreementsInformationMongoRepository.findAll().stream().map(contractInfo -> {
					return ContractProcessFunction.apply(contractInfo);
				}).collect(Collectors.toList())).orElseThrow(
						() -> new NoDataFoundException("Couldn't found contract agreement information in mongodb"));

		return contractAgreementsResponseList;
	}

	public List<QueryDataOfferModel> queryOnDataOffers(OrganizationDetails consumer, String recipient, String elementId)
			throws Exception {

		var recipientURL = recipient + IDS_DATA_URI;
		List<QueryDataOfferModel> queryOfferResposne = null;

		String allCatalog = Optional.ofNullable(readCatalogeAPI(consumer, recipientURL, ""))
				.orElseThrow(() -> new NoDataFoundException("Couldn't found catalog from data provider"));

		JsonNode catalogresponseObj = mapper.readTree(allCatalog);
		ConsumerOfferDescriptionResponseCatalog catalogResponse = mapper.treeToValue(catalogresponseObj,
				ConsumerOfferDescriptionResponseCatalog.class);
		log.info("Base connector ID:" + catalogResponse.getId());

		for (Object catlog : catalogResponse.getResourceCatalog()) {

			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> catObj = (LinkedHashMap<String, String>) catlog;

			var catalogId = catObj.get("@id").toString();

			String allOffer = Optional.ofNullable(readDescriptionAPI(consumer, recipientURL, catalogId))
					.orElseThrow(() -> new NoDataFoundException(
							"Couldn't found valid offer in catalog from data provider" + catalogId));

			JsonNode responseObj = mapper.readTree(allOffer);
			ConsumerOfferDescriptionResponseOffer offerResponse = mapper.treeToValue(responseObj,
					ConsumerOfferDescriptionResponseOffer.class);
			log.info("Base catalog ID:" + offerResponse.getId());

			queryOfferResposne = Optional
					.ofNullable(offerResponse.getOfferedResource().stream().map(offerResource -> {
						return OfferProcessFunction.apply(offerResource);
					}).collect(Collectors.toList())).orElseThrow(
							() -> new NoDataFoundException("Couldn't found offer information in data provider"));

		}

		return queryOfferResposne;
	}

	public ContractAgreementsResponse establishContractbetweenconsumerAndProvider(OrganizationDetails consumer,
			String recipient, String elementId) {
		ContractAgreementsInformation contractInformation = null;
		LocalDate ld = LocalDate.now();
		String dateEstablished = ld.getMonthValue() + "/" + ld.getDayOfMonth() + "/" + ld.getYear();
		DataOfferDetails offerData = null;
		try {
			var recipientURL = recipient + IDS_DATA_URI;

			ResponseEntity<ResponseObject> dataOfferDetailsResponse = providerOfferDetailsAPI
					.getOfferDetails(elementId);

			if (dataOfferDetailsResponse.getStatusCodeValue() == 200) {
				offerData = (mapper.convertValue(dataOfferDetailsResponse.getBody().getData(), DataOfferDetails.class));
			}

			String offerStr = Optional
					.ofNullable(readDescriptionAPI(consumer, recipientURL, recipient + "/offers/" + elementId))
					.orElseThrow(() -> new NoDataFoundException(
							"Couldn't found valid offer from data provider" + elementId));

			JsonNode responseObj = mapper.readTree(offerStr);
			ConsumerOfferResource OfferDetails = mapper.treeToValue(responseObj, ConsumerOfferResource.class);

			if (OfferDetails != null) {

				var offerId = OfferDetails.getId();
				ConsumerOfferResourceRepresentationArtifact artifact = null;

				if (OfferDetails.getRepresentation() != null && OfferDetails.getRepresentation().size() > 0) {
					artifact = OfferDetails.getRepresentation().get(0).getArtifacts().get(0);
				}

				List<ConsumerOfferResourceContractPermission> ruleList = null;

				if (OfferDetails.getContractOffer() != null && OfferDetails.getContractOffer().size() > 0) {
					ruleList = OfferDetails.getContractOffer().get(0).getPermission();
				}

				String contrResponse = negotiateContract(consumer, recipientURL, offerId, artifact.getId(), ruleList);
				JsonNode contrRes = mapper.readTree(contrResponse);
				log.info(contrRes.toPrettyString());

				var agreementRemoteId = contrRes.get("remoteId").asText();
				log.info(agreementRemoteId);

				var agreementSelfhref = contrRes.get("_links").get("self").get("href").asText();
				log.info(agreementSelfhref);

				var artifactDetails = Optional
						.ofNullable(mapper.readTree(getArtifactDetailsBasedonAgreement(consumer, agreementSelfhref)))
						.orElseThrow(() -> new NoDataFoundException(
								"Couldn't found valid artifact details in connectors" + agreementSelfhref));

				log.info(artifactDetails.toPrettyString());

				var dataUrl = Optional.ofNullable(artifactDetails).map(aj -> aj.get("_embedded"))
						.map(em -> em.get("artifacts")).map(a -> a.get(0)).map(z -> z.get("_links"))
						.map(l -> l.get("self")).map(s -> s.get("href")).map(JsonNode::asText)
						.map(s -> s.concat("/data/**")).orElseThrow(
								() -> new RuntimeException("Couldn't construct data retrieval URL from Artifact JSON"));

				String contractName = getContractName(offerData.getTitle(), consumer.getName(), ld);
				contractInformation = ContractAgreementsInformation.builder().dateEstablished(dateEstablished)
						.status("Accepted").dataConsumer(consumer.getName())
						//.contractName(offerData.getTitle() + "_" + consumer.getName() + "-" + ld.getMonth() + "/" + ld.getYear())
						.contractName(contractName)
						.dataOfferDetails(offerData).createdTimeStamp(new Date()).modifiedTimeStamp(new Date())
						.agreementRemoteId(agreementRemoteId).dataUrl(dataUrl).build();
			}

		} catch (Exception e) {
			String contractName = getContractName(offerData.getTitle(), consumer.getName(), ld);
			contractInformation = ContractAgreementsInformation.builder().dateEstablished(dateEstablished)
					.dataConsumer(consumer.getName())
					//.contractName(offerData.getTitle()+ "_" + consumer.getName() + "_" + ld.getMonth() + "/" + ld.getYear())
					.contractName(contractName)
					.createdTimeStamp(new Date()).modifiedTimeStamp(new Date()).status("Rejected")
					.dataOfferDetails(offerData).build();
			log.error("Exception in establishContractbetweenconsumerAndProvider" + e.getMessage());
		} finally {
			contractAgreementsInformationMongoRepository.save(contractInformation);
		}

		return ContractProcessFunction.apply(contractInformation);
	}
	
	private String getContractName(String title, String consumerName, LocalDate ld ) {
		
		return title.trim().length() > 20 ? title + "_" + ld.getMonth() + "/" + ld.getYear():title+ "_" + consumerName + "_" + ld.getMonth() + "/" + ld.getYear();
	}

	public String readData(OrganizationDetails consumer, String agreementRemoteId, String dataUrl)
			throws FileNotFoundException, IOException, Exception {

		String data = "{}";
		try {
			// Point example "{Longitude Latitude}=>{9.97375 53.56093}";
			var url = UriComponentsBuilder.fromHttpUrl(dataUrl).queryParam("agreementUri", agreementRemoteId)
					.queryParam("download", true);
			log.info("Read Data :" + url.toUriString());

			String userCredentials = consumer.getUsername() + ":" + consumer.getPassword();
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().header("Authorization", basicAuth)
					.uri(URI.create(url.toUriString())).build();

			HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFileDownload(
					Path.of(System.getProperty("user.dir")), StandardOpenOption.CREATE, StandardOpenOption.WRITE));
			log.debug(response.statusCode() + "");
			log.debug(response.headers() + "");

			if (response.statusCode() == 200) {
				Path path = response.body();
				log.debug("Path:" + path);
				data = readFileAsString(path.toString());
			}

		} catch (Exception e) {
			log.error("Exception in readData" + e.getMessage());
			throw e;
		}
		return data;
	}

	public static String readFileAsString(String fileName) throws Exception {
		FileReader fr = new FileReader(fileName);
		int i;
		StringBuilder bf = new StringBuilder();
		while ((i = fr.read()) != -1) {
			bf.append((char) i);
		}
		fr.close();
		String data = bf.toString();
		return data;
	}

	private String getAuthHeader(OrganizationDetails consumer) {
		val auth = consumer.getUsername() + ":" + consumer.getPassword();
//		val auth = "admin:password";
		String encoding1 = "";
		try {
			encoding1 = Base64.getEncoder().encodeToString((auth.toString()).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "Basic " + encoding1;
	}

	private String readCatalogeAPI(OrganizationDetails consumer, String recipient, String elementId) {
		var url = consumer.getBaseUrl();
		var body = consumerDescriptionAPI.getCatalogDetails(URI.create(url), getAuthHeader(consumer), recipient,
				elementId);
		return body.toString();
	}

	private String readDescriptionAPI(OrganizationDetails consumer, String recipient, String elementId) {
		var url = consumer.getBaseUrl();
		var body = consumerDescriptionAPI.getDescriptionDetails(URI.create(url), getAuthHeader(consumer), recipient,
				elementId);
		return body.toString();
	}

	private String negotiateContract(OrganizationDetails consumer, String recipient, String offerId, String artifactId,
			List<ConsumerOfferResourceContractPermission> ruleList)
			throws JsonMappingException, JsonProcessingException {
		StringBuilder body = new StringBuilder();
		for (ConsumerOfferResourceContractPermission consumerOfferResourceContractPermission : ruleList) {

			String descriptionValue = consumerOfferResourceContractPermission.getDescription().get(0).getValue();

			if (body.length() == 0)
				body.append(NEGOTIATE_CONTRACT_PART1 + consumerOfferResourceContractPermission.getId()
						+ NEGOTIATE_CONTRACT_PART2 + descriptionValue + NEGOTIATE_CONTRACT_PART3 + artifactId
						+ NEGOTIATE_CONTRACT_PART4);
			else
				body.append("," + NEGOTIATE_CONTRACT_PART1 + consumerOfferResourceContractPermission.getId()
						+ NEGOTIATE_CONTRACT_PART2 + descriptionValue + NEGOTIATE_CONTRACT_PART3 + artifactId
						+ NEGOTIATE_CONTRACT_PART4);
		}

		String bodyStr = "[" + body.toString() + "]";

		return consumerContractAPI.negotiateContract(URI.create(consumer.getBaseUrl()), getAuthHeader(consumer),
				bodyStr, recipient, offerId, artifactId, false);
	}

	private String getArtifactDetailsBasedonAgreement(OrganizationDetails consumer, String agreementSelfhref) {
		return consumerArtifactAPI.artifactDetails(URI.create(agreementSelfhref), getAuthHeader(consumer));
	}
}
