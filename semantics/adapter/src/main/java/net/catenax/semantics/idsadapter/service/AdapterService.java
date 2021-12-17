/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.idsadapter.service;

import java.io.OutputStream;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.config.IdsAdapterConfigProperties;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.Representation;
import net.catenax.semantics.idsadapter.restapi.dto.Source;

/**
 * A service that manages the interaction with the connector
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdapterService {
    private final FhIdsService baseIdsService;
    private final SimpleAdapterService adapterService;
    /** adapter config */
    private final IdsAdapterConfigProperties adapterProperties;

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
        return baseIdsService.getOrCreateOffer(title,offer);
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
        //
        // Offer-Based Approach for Callback by Connector
        //
        if (params.containsKey("offer")) {
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
                mediaType = adapterService.handleSource(response,mediaType,so,params);
            } catch (Exception e) {
                log.error("Source not been found. Either no file was given or the offer/representation/source path does not exist. Leaving empty.",e);
            }
        } else {
            mediaType = adapterService.downloadForAgreement(response,mediaType,params);
        } 
        return mediaType;
    }

   /**
    * registers new twins 
    * @param twinType
    * @param twinSourceBluePrint
    * @return a twin registration response
    */
    public String registerTwins(String twinType, Source twinSourceBluePrint) {
        Source twinSource = adapterProperties.getTwins().get(twinType);
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
        return adapterService.registerTwins(twinType,twinSource);
   }
}