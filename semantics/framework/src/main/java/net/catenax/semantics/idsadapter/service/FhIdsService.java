/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.idsadapter.service;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catenax.semantics.idsadapter.client.api.ArtifactsApi;
import net.catenax.semantics.idsadapter.client.api.CatalogsApi;
import net.catenax.semantics.idsadapter.client.api.ConnectorApi;
import net.catenax.semantics.idsadapter.client.api.ContractsApi;
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
import net.catenax.semantics.idsadapter.config.BaseIdsAdapterConfigProperties;
import net.catenax.semantics.idsadapter.restapi.dto.Catalog;
import net.catenax.semantics.idsadapter.restapi.dto.Contract;
import net.catenax.semantics.idsadapter.restapi.dto.ContractRule;
import net.catenax.semantics.idsadapter.restapi.dto.Offer;
import net.catenax.semantics.idsadapter.restapi.dto.Representation;
import net.catenax.semantics.idsadapter.restapi.dto.Source;

/**
 * A service that manages the interaction with the connector
 */
@RequiredArgsConstructor
@Slf4j
public class FhIdsService implements IdsService {

    /** these are clients to the different endpoints of the connector */
    final private ContractsApi contractsApi;
    final private OfferedResourcesApi offeredResourcesApi;
    final private CatalogsApi catalogsApi;
    final private RulesApi rulesApi;
    final private RepresentationsApi representationsApi;
    final private ArtifactsApi artifactsApi;
    final private ConnectorApi connectorApi;

    final private BaseIdsAdapterConfigProperties baseIdsAdapterConfigProperties;

    /** standard access policy */
    private final String PROVIDE_ACCESS_POLICY =
            "{ \"@context\" : { \"ids\" : \"https://w3id.org/idsa/core/\", \"idsc\" : \"https://w3id.org/idsa/code/\" }, \"@type\" : \"ids:Permission\", \"@id\" : \"https://w3id.org/idsa/autogen/permission/658ca300-4042-4804-839a-3c9548dcc26e\", \"ids:action\" : [ { \"@id\" : \"https://w3id.org/idsa/code/USE\" } ], \"ids:description\" : [ { \"@value\" : \"provide-access\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ], \"ids:title\" : [ { \"@value\" : \"Allow Data Usage\", \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\" } ] }";


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
     * get or create a catalog
     * @param title key of the catalogue, must be no-null
     * @return existing or new catalog representation
     */
    public Catalog getOrCreateCatalog(String title) {
        // is it described in our config?
        Catalog catalog;
        if (baseIdsAdapterConfigProperties.getCatalogs().containsKey(title)) {
            // take the configured template
            catalog = baseIdsAdapterConfigProperties.getCatalogs().get(title);
        } else {
            catalog = new Catalog();
        }
        return getOrCreateCatalog(title,catalog);
    }

    /**
     * get or create a contract
     * @param title key of the contract
     * @return existing or new contract representation
     */
    public Contract getOrCreateContract(String title) {
        // is it described in our config?
        Contract contract;
        if(baseIdsAdapterConfigProperties.getContracts().containsKey(title)) {
            // take the configured template
            contract = baseIdsAdapterConfigProperties.getContracts().get(title);
        } else {
            contract = new Contract();
        }
        return getOrCreateContract(title,contract);
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
        contractDesc.setProvider(baseIdsAdapterConfigProperties.getConnectorUrl());
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

    /**
     * registers a new (re-)source in the ids
     * @param title key of the offer
     * @param offer blueprint of the offer
     * @return new or already existing offer
     */
    @Override
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
        resource.setPublisher(baseIdsAdapterConfigProperties.getPublisher());
        resource.setLanguage(offer.getLanguage());
        resource.setPaymentMethod(OfferedResourceDesc.PaymentMethodEnum.fromValue(offer.getPaymentMethod()));
        resource.setLicense(offer.getLicense());
        resource.setEndpointDocumentation(baseIdsAdapterConfigProperties.getAdapterUrl()+"/adapter");
        OfferedResourceView resourceView = offeredResourcesApi.create4(resource);
        offer.setId(getSelfIdFromLinks(resourceView.getLinks()));
        offer.setUri(getHrefFromSelfLinks(resourceView.getLinks()));

        Catalog catalog=getOrCreateCatalog(offer.getCatalog());
        if(catalog!=null) {
            catalogsApi.addResourcesOffer(Collections.singletonList(offer.getUri()), catalog.getId());
        }

        Contract contract=getOrCreateContract(offer.getContract());
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
                artifactDesc.setAccessUrl(String.format(source.getCallbackPattern(),baseIdsAdapterConfigProperties.getAdapterUrl(),
                    baseIdsAdapterConfigProperties.getServiceName(),title,representationEntry.getKey(),path.getKey()));
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

    @Override
    public Object getSelfDescription() {
        return connectorApi.getPrivateSelfDescription();
    }


}