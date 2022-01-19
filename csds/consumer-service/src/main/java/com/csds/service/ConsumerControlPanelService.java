package com.csds.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.csds.model.ConsumerOfferDescriptionResponseCatalog;
import com.csds.model.ConsumerOfferDescriptionResponseOffer;
import com.csds.model.ConsumerOfferResource;
import com.csds.model.ConsumerOfferResourceContractPermission;
import com.csds.model.ConsumerOfferResourceRepresentationArtifact;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerControlPanelService {

	@Value("${connector.consumer.baseUrl}")
	private URI consumerConnectorBaseUrl;

	@Value("${connector.consumer.recipient}")
	private String recipient;

	@Value("${connector.username}")
	private String username;

	@Value("${connector.password}")
	private String password;

	@Value("${connector.consumer.geopoint}")
	private List<String> geopoints;

	private static final String NEGOTIATE_CONTRACT_PART1 = "{\n" + "    \"@type\": \"ids:Permission\",\n"
			+ "    \"@id\": \"";

	private static final String NEGOTIATE_CONTRACT_PART2 = "\",\n   \"ids:description\": [\n" + "      {\n"
			+ "        \"@value\": \"provide-access\",\n"
			+ "        \"@type\": \"http://www.w3.org/2001/XMLSchema#string\"\n" + "      }\n" + "    ],\n"
			+ "    \"ids:title\": [\n" + "      {\n" + "        \"@value\": \"Allow Data Usage\",\n"
			+ "        \"@type\": \"http://www.w3.org/2001/XMLSchema#string\"\n" + "      }\n" + "    ],\n"
			+ "    \"ids:action\": [\n" + "      {\n" + "        \"@id\": \"https://w3id.org/idsa/code/USE\"\n"
			+ "      }\n" + "    ],\n" + "    \"ids:postDuty\": [],\n" + "    \"ids:assignee\": [],\n"
			+ "    \"ids:assigner\": [],\n" + "    \"ids:constraint\": [],\n" + "    \"ids:preDuty\": [],\n"
			+ "    \"ids:target\":\"";

	private static final String NEGOTIATE_CONTRACT_PART3 = "\"\n" + "}";

	private static final ObjectMapper mapper = new ObjectMapper();

	public void configureAndReadProviderData() {
		try {
			String allCatalog = Optional.ofNullable(readDescriptionAPI(consumerConnectorBaseUrl, recipient, ""))
					.orElseThrow(() -> new RuntimeException("Couldn't found catalog from UI"));

			JsonNode catalogresponseObj = mapper.readTree(allCatalog);
			ConsumerOfferDescriptionResponseCatalog catalogResponse = mapper.treeToValue(catalogresponseObj,
					ConsumerOfferDescriptionResponseCatalog.class);
			log.info("Base connector ID:" + catalogResponse.getId());

			for (Object catlog : catalogResponse.getResourceCatalog()) {

				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> catObj = (LinkedHashMap<String, String>) catlog;

				var catalogId = catObj.get("@id").toString();

				String allOffer = Optional
						.ofNullable(readDescriptionAPI(consumerConnectorBaseUrl, recipient, catalogId))
						.orElseThrow(() -> new RuntimeException(
								"Couldn't found valid offer in catalog from UI" + catalogId));

				JsonNode responseObj = mapper.readTree(allOffer);
				ConsumerOfferDescriptionResponseOffer offerResponse = mapper.treeToValue(responseObj,
						ConsumerOfferDescriptionResponseOffer.class);
				log.info("Base catalog ID:" + offerResponse.getId());

				for (ConsumerOfferResource offer : offerResponse.getOfferedResource()) {
					var offerId = offer.getId();
					ConsumerOfferResourceRepresentationArtifact artifact = null;

					if (offer.getRepresentation() != null && offer.getRepresentation().size() > 0) {
						artifact = offer.getRepresentation().get(0).getArtifacts().get(0);
					}

					ConsumerOfferResourceContractPermission ruleId = null;

					if (offer.getContractOffer() != null && offer.getContractOffer().size() > 0) {
						ruleId = offer.getContractOffer().get(0).getPermission().get(0);
					}

					JsonNode contrRes = negotiateContract(recipient, offerId, artifact.getId(), ruleId.getId());
					log.info(contrRes.toPrettyString());

					var agreementRemoteId = contrRes.get("remoteId").asText();
					log.info(agreementRemoteId);

					var agreementSelfhref = contrRes.get("_links").get("self").get("href").asText();
					log.info(agreementSelfhref);

					var artifactDetails = Optional.ofNullable(getArtifactDetailsBasedonAgreement(agreementSelfhref))
							.orElseThrow(() -> new RuntimeException(
									"Couldn't found valid artifact details in NPM for" + agreementSelfhref));

					log.info(artifactDetails.toPrettyString());

					var dataUrl = Optional.ofNullable(artifactDetails).map(aj -> aj.get("_embedded"))
							.map(em -> em.get("artifacts")).map(a -> a.get(0)).map(z -> z.get("_links"))
							.map(l -> l.get("self")).map(s -> s.get("href")).map(JsonNode::asText)
							.map(s -> s.concat("/data/**")).orElseThrow(() -> new RuntimeException(
									"Couldn't construct data retrieval URL from Artifact JSON"));

					readData(agreementRemoteId, dataUrl);
				}
			}
		} catch (RuntimeException e) {
			log.error("Exception in registerAndreadUIParkingData" + e.getMessage());
		} catch (Exception e) {
			log.error("Exception in registerAndreadUIParkingData" + e.getMessage());
		}
	}

	public String readData(String agreementRemoteId, String dataUrl)
			throws FileNotFoundException, IOException, Exception {
		String data = "{}";
		try {
			// Point example "{Longitude Latitude}=>{9.97375 53.56093}";
			var url = UriComponentsBuilder.fromHttpUrl(dataUrl).queryParam("agreementUri", agreementRemoteId)
					.queryParam("download", true);
			log.info("Read Data :" + url.toUriString());

			String userCredentials = username + ":" + password;
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

	private HttpHeaders prepareHttpHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(username, password);
		return headers;
	}

	private JsonNode getArtifactDetailsBasedonAgreement(String agreementSelfhref) {

		var builder = UriComponentsBuilder.fromHttpUrl(agreementSelfhref + "/artifacts");
		var body = "";
		RestTemplate restTemplateDefault = new RestTemplate();
		HttpHeaders headers = prepareHttpHeader();
		headers.add("Content-Type", "application/json");
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		log.info("ArtifactDetailsBasedonAgreement:" + builder.toUriString());
		var entity = new HttpEntity<>(body, headers);
		ResponseEntity<JsonNode> responseEntity = restTemplateDefault.exchange(builder.toUriString(), HttpMethod.GET,
				entity, JsonNode.class);
		if (responseEntity.getStatusCodeValue() == 200) {
			return responseEntity.getBody();
		}
		return null;
	}

	private String readDescriptionAPI(URI baseUrl, String recipient, String elementId) {
		var url = consumerConnectorBaseUrl + "/ids/description";
		var builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("recipient", recipient).queryParam("elementId",
				elementId);
		var body = "";
		RestTemplate restTemplateDefault = new RestTemplate();
		HttpHeaders headers = prepareHttpHeader();
		headers.add("Content-Type", "application/json");
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		log.info("ReadDescriptionAPI:" + builder.toUriString());
		var entity = new HttpEntity<>(body, headers);
		ResponseEntity<String> responseEntity = restTemplateDefault.exchange(builder.toUriString(), HttpMethod.POST,
				entity, String.class);
		if (responseEntity.getStatusCodeValue() == 200) {
			return responseEntity.getBody();
		}
		return null;
	}

	private JsonNode negotiateContract(String recipient, String offerId, String artifactId, String ruleId) {

		var url = consumerConnectorBaseUrl + "/ids/contract";
		var builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("recipient", recipient)
				.queryParam("resourceIds", offerId).queryParam("artifactIds", artifactId).queryParam("download", false);

		var body = "[" + NEGOTIATE_CONTRACT_PART1 + ruleId + NEGOTIATE_CONTRACT_PART2 + artifactId
				+ NEGOTIATE_CONTRACT_PART3 + "]";

		RestTemplate restTemplateDefault = new RestTemplate();
		HttpHeaders headers = prepareHttpHeader();
		headers.add("Content-Type", "application/json");
		log.info("NegotiateContract" + builder.toUriString());
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		var entity = new HttpEntity<>(body, headers);
		return restTemplateDefault.postForObject(builder.toUriString(), entity, JsonNode.class);
	}

}