/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.client.api.AgreementsApi;
import net.catenax.semantics.idsadapter.client.api.ArtifactsApi;
import net.catenax.semantics.idsadapter.client.api.CatalogsApi;
import net.catenax.semantics.idsadapter.client.api.ContractsApi;
import net.catenax.semantics.idsadapter.client.api.MessagesApi;
import net.catenax.semantics.idsadapter.client.api.OfferedResourcesApi;
import net.catenax.semantics.idsadapter.client.api.RepresentationsApi;
import net.catenax.semantics.idsadapter.client.api.RulesApi;
import net.catenax.semantics.idsadapter.client.model.ArtifactDesc;
import net.catenax.semantics.idsadapter.client.model.ArtifactView;
import net.catenax.semantics.idsadapter.client.model.CatalogDesc;
import net.catenax.semantics.idsadapter.client.model.CatalogView;
import net.catenax.semantics.idsadapter.client.model.ContractDesc;
import net.catenax.semantics.idsadapter.client.model.ContractRuleDesc;
import net.catenax.semantics.idsadapter.client.model.ContractRuleView;
import net.catenax.semantics.idsadapter.client.model.ContractView;
import net.catenax.semantics.idsadapter.client.model.Link;
import net.catenax.semantics.idsadapter.client.model.Links;
import net.catenax.semantics.idsadapter.client.model.OfferedResourceDesc;
import net.catenax.semantics.idsadapter.client.model.OfferedResourceView;
import net.catenax.semantics.idsadapter.client.model.PagedModelCatalogView;
import net.catenax.semantics.idsadapter.client.model.PagedModelContractView;
import net.catenax.semantics.idsadapter.client.model.PagedModelOfferedResourceView;
import net.catenax.semantics.idsadapter.client.model.RepresentationDesc;
import net.catenax.semantics.idsadapter.client.model.RepresentationView;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;
import net.catenax.semantics.idsadapter.restapi.dto.ContractRule;
import net.catenax.semantics.idsadapter.restapi.dto.DataSource;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.Representation;
import net.catenax.semantics.idsadapter.restapi.dto.Source;
import net.catenax.semantics.tools.ResultSetsToXmlSource;

/**
 * A service that manages the interaction with the connector
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Slf4j
public abstract class BaseIdsService {

    /** these are clients to the different endpoints of the connector */
    @NonNull
    protected ContractsApi contractsApi;
    @NonNull
    protected OfferedResourcesApi offeredResourcesApi;
    @NonNull
    protected CatalogsApi catalogsApi;
    @NonNull
    protected RulesApi rulesApi;
    @NonNull
    protected RepresentationsApi representationsApi;
    @NonNull
    protected ArtifactsApi artifactsApi;
    @NonNull
    protected MessagesApi messagesApi;
    @NonNull
    protected ObjectMapper objectMapper;
    @NonNull
    protected AgreementsApi agreementsApi;

    /** standard access policy */
    private final String PROVIDE_ACCESS_POLICY =
            "{ \"@context\" : { \"ids\" : \"https://w3id.org/idsa/core/\", \"idsc\" : \"https://w3id.org/idsa/code/\" }, \"@type\" : \"ids:Permission\", \"@id\" : \"https://w3id.org/idsa/autogen/permission/658ca300-4042-4804-839a-3c9548dcc26e\", \"ids:action\" : [ { \"@id\" : \"https://w3id.org/idsa/code/USE\" } ], \"ids:description\" : [ { \"@value\" : \"provide-access\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ], \"ids:title\" : [ { \"@value\" : \"Allow Data Usage\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ] }";


    protected abstract String getConnectorUrl();
    protected abstract String getPublisher();
    protected abstract String getAdapterUrl();
    protected abstract String getPortalUrl();
    protected abstract String getServiceUrl();

    /**
     * extract the UUID from this object
     * @param links link section of the object
     * @return UUID
     */
    protected UUID getSelfIdFromLinks(Links links) {
        return getIdFromLink(links.get("self"));
    }

    /**
     * extract the UUID from a given URI link
     * @param link link
     * @return UUID as the last part of the URI
     */
    protected UUID getIdFromLink(Link link) {
        String href = link.getHref();
        return UUID.fromString(href.substring(href.lastIndexOf('/') + 1));
    }

    /**
     * extract the absolute URI of this object
     * @param links link section of the object
     * @return absolute uri
     */
    protected String getHrefFromSelfLinks(Links links) {
        return links.get("self").getHref();
    }

    /**
     * extract the absolute URI of this object as a list
     * @param links link section of the object
     * @return absolute uri as a list
     */
    protected List<String> getHrefListFromSelfLinks(Links links) {
        return Collections.singletonList(getHrefFromSelfLinks(links));
    }

    /**
     * get or create a catalog
     * @param title key of the catalogue, must be no-null
     * @param catalog representation may not be null
     * @return catalog representation
     */
    public Catalog getOrCreateCatalog(String title, Catalog catalog) {
        PagedModelCatalogView catalogsView = catalogsApi.getAll10(null, null);
        for (CatalogView catalogView : catalogsView.getEmbedded().getCatalogs()) {
            if (title.equals(catalogView.getTitle())) {
                catalog.setDescription(catalogView.getDescription());
                catalog.setId(getSelfIdFromLinks(catalogView.getLinks()));
                catalog.setUri(getHrefFromSelfLinks(catalogView.getLinks()));
                return catalog;
            }
        }
        CatalogDesc catalogDesc = new CatalogDesc();
        catalogDesc.setTitle(title);
        catalogDesc.setDescription(catalog.getDescription());
        CatalogView catalogView =catalogsApi.create9(catalogDesc);
        catalog.setId(getSelfIdFromLinks(catalogView.getLinks()));
        catalog.setUri(getHrefFromSelfLinks(catalogView.getLinks()));
        return catalog;
    }

    /**
     * get or create a contract
     * @param title key of the contract
     * @param contract blueprint of the contract
     * @return existing or new contract representation
     */
    public Contract getOrCreateContract(String title, Contract contract) {
        PagedModelContractView contractsView = contractsApi.getAll8(null, null);
        for (ContractView contractView : contractsView.getEmbedded().getContracts()) {
            if (title.equals(contractView.getTitle())) {
                contract.setId(getSelfIdFromLinks(contractView.getLinks()));
                contract.setUri(getHrefFromSelfLinks(contractView.getLinks()));
                contract.setDescription(contractView.getDescription());
                contract.setConsumer(contractView.getConsumer());
                contract.setStart(contractView.getStart());
                contract.setEnd(contractView.getEnd());
                return contract;
            }
        }
        ContractDesc contractDesc = new ContractDesc();
        contractDesc.setTitle(title);
        contractDesc.setDescription(contract.getDescription());
        contractDesc.setStart(contract.getStart());
        contractDesc.setEnd(contract.getEnd());
        contractDesc.setProvider(getConnectorUrl());
        contractDesc.setConsumer(contract.getConsumer());
        ContractView contractView = contractsApi.create7(contractDesc);
        contract.setId(getSelfIdFromLinks(contractView.getLinks()));
        contract.setUri(getHrefFromSelfLinks(contractView.getLinks()));

        for(Map.Entry<String, ContractRule> rule: contract.getRules().entrySet()) {
            ContractRuleDesc contractRuleDesc = new ContractRuleDesc();
            contractRuleDesc.setTitle(rule.getKey());
            contractRuleDesc.setDescription(rule.getValue().getDescription());
            contractRuleDesc.setValue(rule.getValue().getValue());
            ContractRuleView ruleView = rulesApi.create1(contractRuleDesc);
            rule.getValue().setId(getSelfIdFromLinks(ruleView.getLinks()));
            rule.getValue().setUri(getHrefFromSelfLinks(ruleView.getLinks()));
        }
        List<String> ruleLinks=contract.getRules().values().stream().map( rule -> rule.getUri()).collect(Collectors.toList());
        contractsApi.addResourcesRule(ruleLinks, contract.getId());
        return contract;
    }

    /* name of this service */
    public abstract String getServiceName();

    /**
     * registers a new (re-)source in the ids
     * @param title key of the offer
     * @param offer blueprint of the offer
     * @return new or already existing offer
     */
    public Offer getOrCreateOffer(String title, Offer offer) {
        PagedModelOfferedResourceView offersView = offeredResourcesApi.getAll5(null, null);
        for (OfferedResourceView offerView : offersView.getEmbedded().getResources()) {
            if (title.equals(offerView.getTitle())) {
                offer.setId(getSelfIdFromLinks(offerView.getLinks()));
                offer.setUri(getHrefFromSelfLinks(offerView.getLinks()));
                offer.setDescription(offerView.getDescription());
                offer.setKeywords(offerView.getKeywords());
                offer.setLanguage(offerView.getLanguage());
                offer.setPaymentMethod(offerView.getPaymentModality().getValue());
                offer.setLicense(offerView.getLicense());
                for (RepresentationView representationView : offeredResourcesApi.getResource12(offer.getId(), null, null).getEmbedded().getRepresentations()) {
                    Representation representation;
                    if (offer.getRepresentations().containsKey(representationView.getTitle())) {
                        representation = offer.getRepresentations().get(representationView.getTitle());
                    } else {
                        representation = new Representation();
                    }
                    representation.setId(getSelfIdFromLinks(representationView.getLinks()));
                    representation.setUri(getHrefFromSelfLinks(representationView.getLinks()));
                    representation.setDescription(representationView.getDescription());
                    representation.setLanguage(representationView.getLanguage());
                    representation.setMediaType(representationView.getMediaType());
                    //representation.setModel(representationView.get)
                    offer.getRepresentations().put(representationView.getTitle(), representation);
                    for (ArtifactView artifactView : representationsApi.getResource10(representation.getId(), null, null).getEmbedded().getArtifacts()) {
                        Source source;
                        if (representation.getSources().containsKey(artifactView.getTitle())) {
                            source = representation.getSources().get(artifactView.getTitle());
                        } else {
                            source = new Source();
                        }
                        source.setId(getSelfIdFromLinks(artifactView.getLinks()));
                        source.setUri(getHrefFromSelfLinks(artifactView.getLinks()));
                        source.setDescription(artifactView.getDescription());
                        representation.getSources().put(artifactView.getTitle(), source);
                    }
                }
                return offer;
            }
        }

        OfferedResourceDesc resource = new OfferedResourceDesc();
        resource.setTitle(title);
        resource.setDescription(offer.getDescription());
        resource.setKeywords(offer.getKeywords());
        resource.setPublisher(getPublisher());
        resource.setLanguage(offer.getLanguage());
        resource.setPaymentMethod(OfferedResourceDesc.PaymentMethodEnum.valueOf(offer.getPaymentMethod()));
        resource.setLicense(offer.getLicense());
        resource.setEndpointDocumentation(getAdapterUrl()+"/adapter");
        OfferedResourceView resourceView = offeredResourcesApi.create4(resource);
        offer.setId(getSelfIdFromLinks(resourceView.getLinks()));
        offer.setUri(getHrefFromSelfLinks(resourceView.getLinks()));

        Catalog catalog=getOrCreateCatalog(offer.getCatalog(),null);
        if(catalog!=null) {
            catalogsApi.addResourcesOffer(Collections.singletonList(offer.getUri()), catalog.getId());
        }

        Contract contract=getOrCreateContract(offer.getContract(),null);
        if(contract!=null) {
            contractsApi.addResourcesOffers(Collections.singletonList(offer.getUri()), contract.getId());
        }

        for (Map.Entry<String, Representation> representationEntry : offer.getRepresentations().entrySet()) {
            Representation representation = representationEntry.getValue();
            // Add representation
            RepresentationDesc representationDesc = new RepresentationDesc();
            representationDesc.setTitle(representationEntry.getKey());
            representationDesc.setLanguage(representation.getLanguage());
            representationDesc.setDescription(representation.getDescription());
            representationDesc.setMediaType(representation.getMediaType());
            representationDesc.setStandard(representation.getModel());
            RepresentationView representationView = representationsApi.create3(representationDesc);
            representation.setId(getSelfIdFromLinks(representationView.getLinks()));
            representation.setUri(getHrefFromSelfLinks(representationView.getLinks()));

            for (Map.Entry<String, Source> path : representation.getSources().entrySet()) {
                Source source = path.getValue();
                ArtifactDesc artifactDesc = new ArtifactDesc();
                artifactDesc.setTitle(path.getKey());
                artifactDesc.setDescription(source.getDescription());
                artifactDesc.setAccessUrl(String.format(source.getCallbackPattern(),getAdapterUrl(),getServiceName(),title,representationEntry.getKey(),path.getKey()));
                ArtifactView artifactView = artifactsApi.create11(artifactDesc);
                source.setId(getSelfIdFromLinks(artifactView.getLinks()));
                source.setUri(getHrefFromSelfLinks(artifactView.getLinks()));
            }
            List<String> artifactList=representation.getSources().values().stream().map( source -> source.getUri()).collect(Collectors.toList());
            representationsApi.addResourcesArtifact(artifactList, representation.getId());
        }

        List<String> representationsList=offer.getRepresentations().values().stream().map( rep -> rep.getUri()).collect(Collectors.toList());
        offeredResourcesApi.addResourcesRepresentation(representationsList, offer.getId());
        return offer;
    }

    /**
     * downloads an xml-based source (file, statement, whatever)
     * @param response the outputstream to put the resource into
     * @param mediaType media type requested
     * @param params request parameters
     * @return the resulting media type of the data written to the response stream
     */
    public String downloadForAgreement(OutputStream response, String mediaType, Map<String,String> params) {        
        log.info("Received a download request with params "+params+ "into stream "+response+" with default mediaType "+mediaType);
        
        if(params.containsKey("file")) {
            Source source=new Source();
            source.setType("file");
            source.setFile(params.get("file"));
            source.setTransformation(params.get("transformation"));

            try {
                mediaType= handleSource(response,mediaType,source,params);
            } catch (Exception e) {
                log.error("File could not be processed. Leaving empty.",e);
            }

        //
        // Not supported approach
        //

        } else {
            log.error("Neither offer nor file given. Leaving empty.");
        }

        return mediaType;
    }

    /**
     * handle a given raw source for the given response stream
     * @param response stream
     * @param so source
     * @param params runtime params
     * @return new mediateType
     */
    protected  Map.Entry<String,javax.xml.transform.Source> handleRawSource(String mediaType, Source so, Map<String,String> params) throws Exception {
        switch(so.getType()) {
            case "file":    
                return handleSourceFile(mediaType, so, params);
            case "jdbc":
                return handleSourceJdbc(mediaType, so, params);
            default:
                throw new UnsupportedOperationException("Source type "+so.getType()+" is not supported.");
        }
    }

    /**
     * handle a given source for the given response stream
     * @param response stream
     * @param so source
     * @param params runtime params
     * @return new mediateType
     */
    protected String handleSource(OutputStream response, String mediaType, Source so, Map<String,String> params) throws Exception {
        Map.Entry<String,javax.xml.transform.Source> sourceImpl=handleRawSource(mediaType,so,params);
    
        mediaType=sourceImpl.getKey();

        String transformation=so.getTransformation();
        if(transformation==null) {
            transformation="xml2xml.xsl";
        }

        log.info("Accessing TRANSFORMATION source "+transformation);
        
        URL sheet = getClass().getClassLoader().getResource(transformation);

        mediaType="application/json";
                
        log.info("Media Type changed to "+mediaType);
                
        StreamSource xslt = new StreamSource(sheet.openStream());
        javax.xml.transform.Result out = new StreamResult(response);
        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");


        javax.xml.transform.Transformer transformer = setTransformationParameters(factory.newTransformer(xslt));
        transformer.transform(sourceImpl.getValue(), out);
        if(sourceImpl instanceof StreamSource) {
            ((StreamSource) sourceImpl).getInputStream().close();
        }
        return mediaType;
    }

    protected  javax.xml.transform.Transformer setTransformationParameters( javax.xml.transform.Transformer transformer ) {
        transformer.setParameter("SERVICE_URL",getServiceUrl());
        transformer.setParameter("ADAPTER_URL",getAdapterUrl());
        transformer.setParameter("PORTAL_URL",getPortalUrl());
        transformer.setParameter("CONNECTOR_ID","https://w3id.org/idsa/autogen/connectorEndpoint/a73d2202-cb77-41db-a3a6-05ed251c0b");
        return transformer;
    }

    /**
     * Handle a file based adapter/transformation source
     * @param mediaType mediatype requested
     * @param so source representation
     * @param params runtime parameters
     * @return pair of final media type and xml transformation source
     * @throws TransformerFactoryConfigurationError
     */
    protected Map.Entry<String,javax.xml.transform.Source> handleSourceFile(String mediaType, Source so, Map<String,String> params) throws TransformerFactoryConfigurationError, ParserConfigurationException {
        log.info("Accessing FILE source "+so.getFile());
        URL resource = getClass().getClassLoader().getResource(so.getFile());
        if(resource!=null) {
            try {
                InputStream resourceStream=resource.openStream();
                javax.xml.transform.Source xml = new StreamSource(resourceStream);
                return Map.entry("text/xml",xml);
            } catch (IOException e) {
                log.error("download & transform error.", e);
            }
        }

        log.error("File "+so.getFile()+" could not bee found. Leaving empty.");
        return Map.entry("text/xml",new DOMSource(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()));
    }

    /**
     * access the datasource associated to a source
     * @param so the source rep
     * @return the datasource associated
     */
    protected Connection getConnection(Source so) throws ClassNotFoundException, SQLException {
        DataSource ds=so.getDatasource();
        Class connClass = Class.forName (ds.getDriverClassName()); 
        return DriverManager.getConnection (ds.getUrl(), ds.getUsername(),ds.getPassword());
    }

    /**
     * Handle a relational based adapter/transformation source
     * @param mediaType
     * @param so
     * @param params
     * @return pair of final media type and xml transformation source
     * @throws TransformerFactoryConfigurationError
     */
    protected Map.Entry<String,javax.xml.transform.Source> handleSourceJdbc(String mediaType, Source so, final Map<String,String> params) throws ClassNotFoundException, SQLException, TransformerException {
         
        // load jdbc stuff
        final Connection fconn = getConnection(so);
        log.info("using configured DataSource Connection: " + fconn.toString());
        Map<String,ResultSet> resultSets=so.getAliases().entrySet()
            .stream().collect(Collectors.toMap( alias -> alias.getKey(), alias -> {
                try {
                    Statement stmt = fconn.createStatement();
                    String sql=alias.getValue();
                    for(Map.Entry<String,String> param : params.entrySet()) {
                        sql=sql.replace("{"+param.getKey()+"}",param.getValue().replace("+","%2b"));
                    }
                    log.info(sql);
                    return (ResultSet) stmt.executeQuery(sql);
                } catch(SQLException e) {
                    return null;
                }
            }));
         ResultSetsToXmlSource converter=new ResultSetsToXmlSource();

        javax.xml.transform.Source source = converter.convert(resultSets);
        fconn.close();

        return Map.entry("text/xml", source);
    }

   /**
    * registers new twins 
    * @param twinType
    * @param twinSource
    * @return the registration response
    */
    public String registerTwins(String twinType, Source twinSource) throws Exception {
        ByteArrayOutputStream outStream=new ByteArrayOutputStream();
        handleSource(outStream,"application/json",twinSource,new java.util.HashMap());
        outStream.close();
        String result=new String(outStream.toByteArray());
        return registerTwinDefinitions(twinType, result);
    }

   /**
    * registers new twin definitions
    * @param twinType the type of twins
    * @param twinSource the twin definitions as a payload
    * @return the registration response
    */
    public String registerTwinDefinitions(String twinType, String twinSource) throws Exception {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(getServiceUrl()+"/twins");
        httppost.addHeader("accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        httppost.setEntity(new StringEntity(twinSource));
        log.info("Accessing Twin Registry via "+httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        log.info("Received Twin Registry response "+response.getStatusLine());
        String finalResult = IOUtils.toString(response.getEntity().getContent());
        if(response.getStatusLine().getStatusCode()!=200) {
            throw new Exception(finalResult); 
        }
        return finalResult;
    }

}