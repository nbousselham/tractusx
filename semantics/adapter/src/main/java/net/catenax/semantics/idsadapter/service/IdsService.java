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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
import net.catenax.semantics.idsadapter.config.IdsAdapterConfigProperties;
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
@Service
@RequiredArgsConstructor
@Slf4j
public class IdsService extends BaseIdsService {

    @Autowired   
    private javax.sql.DataSource defaultDataSource;        

    /** adapter config */
    private final IdsAdapterConfigProperties adapterProperties;

    @Autowired
    public void setContractsApi(ContractsApi api) {
        contractsApi=api;
    }
    @Autowired
    public void setOfferedResourcesApi(OfferedResourcesApi api) {
        offeredResourcesApi=api;
    }
    @Autowired
    public void setCatalogsApi(CatalogsApi api) {
        catalogsApi=api;
    }
    @Autowired
    public void setRulesApi(RulesApi api) {
        rulesApi=api;
    }
    @Autowired
    public void setRepresentationsApi(RepresentationsApi api) {
        representationsApi=api;
    }
    @Autowired
    public void setArtifactsApi(ArtifactsApi api) {
        artifactsApi=api;
    }
    @Autowired
    public void setMessagesApi(MessagesApi api) {
        messagesApi=api;
    }
    @Autowired
    public void setObjectMapper(ObjectMapper api) {
        objectMapper=api;
    }
    @Autowired
    public void setAgreementsApi(AgreementsApi api) {
        agreementsApi=api;
    }

    /**
     * when the service start it may read its config
     * and automatically publish stuff
     */
    @PostConstruct
    public void setup() {
        if(adapterProperties.isOfferOnStart()) {
            adapterProperties.getOffers().entrySet().forEach( source -> {
                try {
                    getOrCreateOffer(source.getKey(),source.getValue());
                } catch(Exception e) {
                    log.error("Could not publisher offer "+source.getKey()+" in connector. Maybe it is not active?",e);
                }
            });
        }
        if(adapterProperties.isRegisterOnStart()) {                
            adapterProperties.getTwins().entrySet().forEach( twin -> {
                try {
                    System.out.println(registerTwins(twin.getKey(),twin.getValue()));
                } catch(Exception e) {
                    log.error("Could not register twins "+twin.getKey()+" in connector. Maybe it is not active?",e);
                }
            });
        }
    }

    /**
     * get or create a catalog
     * @param title key of the catalogue, must be no-null
     * @param catalogBluePrint representation, maybe null if internal configuration should be used
     * @return existing or new catalog representation
     */
    @Override
    public Catalog getOrCreateCatalog(String title, Catalog catalogBluePrint) {
        // is it described in our config?
        Catalog catalog;
        if(adapterProperties.getCatalogs().containsKey(title)) {
              // take the configured template
              catalog = adapterProperties.getCatalogs().get(title);
        } else {
               catalog = new Catalog();
        }
        if(catalogBluePrint!=null) {
            if(catalogBluePrint.getId()!=null) {
                catalog.setId(catalogBluePrint.getId());
            } 
            if(catalogBluePrint.getUri()!=null) {
                catalog.setUri(catalogBluePrint.getUri());
            }
            if(catalogBluePrint.getDescription()!=null) {
                catalog.setDescription(catalogBluePrint.getDescription());
            }
        }
        return super.getOrCreateCatalog(title,catalog);
    }

    /**
     * get or create a contract
     * @param title key of the contract
     * @param contractBluePrint blueprint of the contract
     * @return existing or new contract representation
     */
    public Contract getOrCreateContract(String title, Contract contractBluePrint) {
        // is it described in our config?
        Contract contract;
        if(adapterProperties.getContracts().containsKey(title)) {
              // take the configured template
              contract = adapterProperties.getContracts().get(title);
        } else {
              contract = new Contract();
        }
        if(contractBluePrint!=null) {
            if(contractBluePrint.getId()!=null) {
                contract.setId(contractBluePrint.getId());
            } 
            if(contractBluePrint.getUri()!=null) {
                contract.setUri(contractBluePrint.getUri());
            }
            if(contractBluePrint.getDescription()!=null) {
                contract.setDescription(contractBluePrint.getDescription());
            }
            if(contractBluePrint.getConsumer()!=null) {
                contract.setConsumer(contractBluePrint.getConsumer());
            }
            if(contractBluePrint.getStart()!=null) {
                contract.setStart(contractBluePrint.getStart());
            }
            if(contractBluePrint.getEnd()!=null) {
                contract.setEnd(contractBluePrint.getEnd());
            }
            if(contractBluePrint.getRules()!=null && !contractBluePrint.getRules().isEmpty()) {
                contract.setRules(contractBluePrint.getRules());
            }
        }
        return super.getOrCreateContract(title,contract);
    }

    /**
     * registers a new (re-)source in the ids
     * @param title key of the offer
     * @param offerBluePrint blueprint of the offer
     * @return new or already existing offer
     */
    public Offer getOrCreateOffer(String title, Offer offerBluePrint) {
        Offer offer;
        // is it described in our config?
        if(adapterProperties.getOffers().containsKey(title)) {
            // take the configured template
            offer = adapterProperties.getOffers().get(title);
        } else {
            offer = new Offer();
        }
        if(offerBluePrint!=null) {
            if(offerBluePrint.getId()!=null) {
                offer.setId(offerBluePrint.getId());
            }
            if(offerBluePrint.getUri()!=null) {
                offer.setUri(offerBluePrint.getUri());
            }
            if(offerBluePrint.getDescription()!=null) {
                offer.setDescription(offerBluePrint.getDescription());
            }
            if(offerBluePrint.getCatalog()!=null) {
                offer.setCatalog(offerBluePrint.getCatalog());
            }
            if(offerBluePrint.getContract()!=null) {
                offer.setContract(offerBluePrint.getContract());
            }
            if(offerBluePrint.getKeywords()!=null && !offerBluePrint.getKeywords().isEmpty()) {
                offer.setKeywords(offerBluePrint.getKeywords());
            }
            if(offerBluePrint.getLanguage()!=null) {
                offer.setLanguage(offerBluePrint.getLanguage());
            }
            if(offerBluePrint.getPaymentMethod()!=null) {
                offer.setPaymentMethod(offerBluePrint.getPaymentMethod());
            }
            if(offerBluePrint.getLicense()!=null) {
                offer.setLicense(offerBluePrint.getLicense());
            }
            if(offerBluePrint.getRepresentations()!=null && !offerBluePrint.getRepresentations().isEmpty()) {
                offer.setRepresentations(offerBluePrint.getRepresentations());
            }
        }
        return super.getOrCreateOffer(title,offer);
    }

     /**
     * downloads an xml-based source (file, statement, whatever)
     * @param response the outputstream to put the resource into
     * @param mediaType media type requested
     * @param params request parameters
     * @return the resulting media type of the data written to the response stream
     */
    @Override
    public String downloadForAgreement(OutputStream response, String mediaType, Map<String,String> params) {        
        log.info("Received a download request with params "+params+ "into stream "+response+" with default mediaType "+mediaType);
        
        //
        // Offer-Based Approach for Callback by Connector
        //
        if(params.containsKey("offer")) {

            String offer = params.get("offer");
            try {
                log.info("Looking up OFFER "+offer);

                Offer off = adapterProperties.getOffers().get(offer);
               
                String representation = params.get("representation");

                log.info("Looking up REPRESENTATION "+representation);
                
                Representation rep = off.getRepresentations().get(representation);

                String source = params.get("source");

                log.info("Looking up SOURCE "+source);

                Source so = rep.getSources().get(source);

                mediaType= handleSource(response,mediaType,so,params);
                
            } catch (Exception e) {
                log.error("Source not been found. Either no file was given or the offer/representation/source path does not exist. Leaving empty.",e);
            }
            
        } else {
            mediaType=super.downloadForAgreement(response,mediaType,params);
        } 
        
        return mediaType;
    }

    /**
     * access the datasource associated to a source
     * @param so the source rep
     * @return the datasource associated
     */
    @Override
    protected Connection getConnection(Source so) throws ClassNotFoundException, SQLException {
        if(so.getDatasource()!=null) {
            return super.getConnection(so);
        } 
        return defaultDataSource.getConnection();
    }

   /**
    * registers new twins 
    * @param twinType
    * @param twinSourceBluePrint
    * @return a twin registration response
    */
    @Override
    public String registerTwins(String twinType, Source twinSourceBluePrint) throws Exception {
        Source twinSource=adapterProperties.getTwins().get(twinType);
        if(twinSource==null) {
            twinSource=new Source();
        }
        if(twinSourceBluePrint!=null) {
            if(twinSourceBluePrint.getType()!=null) {
                twinSource.setType(twinSourceBluePrint.getType());
            }
            if(twinSourceBluePrint.getFile()!=null) {
                twinSource.setFile(twinSourceBluePrint.getFile());
            }
            if(twinSourceBluePrint.getDatasource()!=null) {
                twinSource.setDatasource(twinSourceBluePrint.getDatasource());
            }
            if(twinSourceBluePrint.getTransformation()!=null) {
                twinSource.setTransformation(twinSourceBluePrint.getTransformation());
            }
            if(twinSourceBluePrint.getId()!=null) {
                twinSource.setId(twinSourceBluePrint.getId());
            }
            if(twinSourceBluePrint.getUri()!=null) {
                twinSource.setUri(twinSourceBluePrint.getUri());
            }
            if(twinSourceBluePrint.getDescription()!=null) {
                twinSource.setDescription(twinSourceBluePrint.getDescription());
            }
            if(twinSourceBluePrint.getAliases()!=null && twinSourceBluePrint.getAliases().isEmpty()) {
                twinSource.setAliases(twinSourceBluePrint.getAliases());
            }
        }
        return super.registerTwins(twinType,twinSourceBluePrint);
   }

   @Override
   protected String getConnectorUrl() {
    return adapterProperties.getConnectorUrl();
   }

   @Override
   protected String getPublisher() {
    return adapterProperties.getPublisher();
   }

   @Override
   protected String getAdapterUrl() {
    return adapterProperties.getAdapterUrl();
   }
  

   @Override
   protected String getPortalUrl() {
    return adapterProperties.getPortalUrl();
   }
 
   @Override
   protected String getServiceUrl() {
    return adapterProperties.getServiceUrl();
   }

}