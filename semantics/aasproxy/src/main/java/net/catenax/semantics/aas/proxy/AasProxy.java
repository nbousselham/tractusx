/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.aas.proxy;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import net.catenax.semantics.aas.api.LookupApiDelegate;
import net.catenax.semantics.aas.api.RegistryAndDiscoveryInterfaceApi;
import net.catenax.semantics.aas.api.RegistryApiDelegate;
import net.catenax.semantics.aas.model.AssetAdministrationShellDescriptor;
import net.catenax.semantics.aas.model.AssetAdministrationShellDescriptorCollection;
import net.catenax.semantics.aas.model.IdentifierKeyValuePair;
import net.catenax.semantics.aas.model.SubmodelDescriptor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

/**
 * The AAS Proxy implements the service layer behind the web protocol layer.
 */
@Service
@RequiredArgsConstructor
public class AasProxy implements LookupApiDelegate, RegistryApiDelegate {

    /**
     * the central registry that we delegate to or that redirects us
     */
    protected final RegistryAndDiscoveryInterfaceApi delegate;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return LookupApiDelegate.super.getRequest();
    }

    @Override
    public ResponseEntity<Void> deleteAssetAdministrationShellDescriptorById(String aasIdentifier) {
        delegate.deleteAssetAdministrationShellDescriptorById(aasIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier) {
        delegate.deleteSubmodelDescriptorById(aasIdentifier, submodelIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AssetAdministrationShellDescriptorCollection> getAllAssetAdministrationShellDescriptors(Integer page, Integer pageSize) {
        try {
            return ResponseEntity.ok(delegate.getAllAssetAdministrationShellDescriptors(page, pageSize));
        } catch(feign.FeignException e) {
            var response=ResponseEntity.status(e.status());
            if(e.responseBody().isPresent()) {
                response.body(e.responseBody().get().toString());
            }
            return response.build();
        }
    }

    @Override
    public ResponseEntity<List<SubmodelDescriptor>> getAllSubmodelDescriptors(String aasIdentifier) {
        return ResponseEntity.ok(delegate.getAllSubmodelDescriptors(aasIdentifier));
    }

    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> getAssetAdministrationShellDescriptorById(String aasIdentifier) {
        return ResponseEntity.ok(delegate.getAssetAdministrationShellDescriptorById(aasIdentifier));
    }

    @Override
    public ResponseEntity<SubmodelDescriptor> getSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier) {
        return ResponseEntity.ok(delegate.getSubmodelDescriptorById(aasIdentifier, submodelIdentifier));
    }

    @Override
    public ResponseEntity<AssetAdministrationShellDescriptor> postAssetAdministrationShellDescriptor(AssetAdministrationShellDescriptor assetAdministrationShellDescriptor) {
        return ResponseEntity.ok(delegate.postAssetAdministrationShellDescriptor(assetAdministrationShellDescriptor));
    }

    @Override
    public ResponseEntity<SubmodelDescriptor> postSubmodelDescriptor(String aasIdentifier, SubmodelDescriptor submodelDescriptor) {
        return ResponseEntity.ok(delegate.postSubmodelDescriptor(aasIdentifier, submodelDescriptor));
    }

    @Override
    public ResponseEntity<Void> putAssetAdministrationShellDescriptorById(String aasIdentifier, AssetAdministrationShellDescriptor assetAdministrationShellDescriptor) {
        delegate.putAssetAdministrationShellDescriptorById(aasIdentifier, assetAdministrationShellDescriptor);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> putSubmodelDescriptorById(String aasIdentifier, String submodelIdentifier, SubmodelDescriptor submodelDescriptor) {
        delegate.putSubmodelDescriptorById(aasIdentifier, submodelIdentifier, submodelDescriptor);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAllAssetLinksById(String aasIdentifier) {
        delegate.deleteAllAssetLinksById(aasIdentifier);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<String>> getAllAssetAdministrationShellIdsByAssetLink(List<IdentifierKeyValuePair> assetIds) {
        return ResponseEntity.ok(delegate.getAllAssetAdministrationShellIdsByAssetLink(assetIds));
    }

    @Override
    public ResponseEntity<List<IdentifierKeyValuePair>> getAllAssetLinksById(String aasIdentifier) {
        return ResponseEntity.ok(delegate.getAllAssetLinksById(aasIdentifier));
    }

    @Override
    public ResponseEntity<List<IdentifierKeyValuePair>> postAllAssetLinksById(String aasIdentifier, List<IdentifierKeyValuePair> identifierKeyValuePair) {
        return ResponseEntity.ok(delegate.postAllAssetLinksById(aasIdentifier, identifierKeyValuePair));
    }
}
