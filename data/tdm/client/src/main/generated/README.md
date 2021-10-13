# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.catenax.tdm.api.v1.*;
import com.catenax.tdm.api.v1.auth.*;
import io.swagger.client.model.*;
import com.catenax.tdm.api.v1.client.AspectModelApi;

import java.io.File;
import java.util.*;

public class AspectModelApiExample {

    public static void main(String[] args) {
        

        AspectModelApi apiInstance = new AspectModelApi();
        
        String aspect = "aspect_example"; // String | Aspect Name
        
        String oneID = "oneID_example"; // String | Business Partner OneID
        
        String partUniqueID = "partUniqueID_example"; // String | UniqueID of part
        
        try {
            List<Object> result = apiInstance.getAspect(aspect, oneID, partUniqueID);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling AspectModelApi#getAspect");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AspectModelApi* | [**getAspect**](docs/AspectModelApi.md#getAspect) | **GET** /catena-x/tdm/1.0/aspect/{aspect}/{oneID}/{partUniqueID} | Retrieve Aspect
*BusinessPartnerServiceApi* | [**createBusinessPartnerNumber**](docs/BusinessPartnerServiceApi.md#createBusinessPartnerNumber) | **GET** /catena-x/tdm/1.0/businesspartner/bpn | Create Business Partner BPN
*BusinessPartnerServiceApi* | [**getBusinessPartner**](docs/BusinessPartnerServiceApi.md#getBusinessPartner) | **GET** /catena-x/tdm/1.0/businesspartner | Get Business Partner
*MemberCompanyServiceApi* | [**getMemberCompany**](docs/MemberCompanyServiceApi.md#getMemberCompany) | **GET** /catena-x/tdm/1.0/member/company | Retrieve Member Company
*MemberCompanyServiceApi* | [**getMemberCompanyRolesAll**](docs/MemberCompanyServiceApi.md#getMemberCompanyRolesAll) | **GET** /catena-x/tdm/1.0/member/company/role/all | Retrieve all possible Roles for a Business Partner
*PartsRelationshipServiceApi* | [**createVehicle**](docs/PartsRelationshipServiceApi.md#createVehicle) | **GET** /catena-x/tdm/1.0/vehicle/create/{oneid} | Create new vehicle BOM
*PartsRelationshipServiceBrokerHttpProxyApiApi* | [**getPartAspectUpdate**](docs/PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartAspectUpdate) | **GET** /catena-x/tdm/1.0/prs/broker/PartAspectUpdate | Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
*PartsRelationshipServiceBrokerHttpProxyApiApi* | [**getPartRelationshipUpdateList**](docs/PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartRelationshipUpdateList) | **GET** /catena-x/tdm/1.0/prs/broker/PartRelationshipUpdateList | Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
*PartsRelationshipServiceBrokerHttpProxyApiApi* | [**getPartTypeNameUpdate**](docs/PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartTypeNameUpdate) | **GET** /catena-x/tdm/1.0/prs/broker/PartTypeNameUpdate | Get a PartTypeNameUpdate. Describes an update of a part type name.
*TdmAdminApi* | [**purgeTestData**](docs/TdmAdminApi.md#purgeTestData) | **GET** /catena-x/tdm/1.0/admin/purge | Purge all current test data
*TraceabilityApi* | [**getTraceability**](docs/TraceabilityApi.md#getTraceability) | **GET** /catena-x/tdm/1.0/traceability/{oneid} | get all Traceability information for OneID


## Documentation for Models

 - [AggregateStatesEnumeration](docs/AggregateStatesEnumeration.md)
 - [Aspect](docs/Aspect.md)
 - [BPNIssuer](docs/BPNIssuer.md)
 - [BuildPositionEnumeration](docs/BuildPositionEnumeration.md)
 - [BusinessPartner](docs/BusinessPartner.md)
 - [DigitalFiles](docs/DigitalFiles.md)
 - [DigitalFilesInner](docs/DigitalFilesInner.md)
 - [DisassemblyStatus](docs/DisassemblyStatus.md)
 - [DocumentClassificationCharacteristic](docs/DocumentClassificationCharacteristic.md)
 - [DocumentClassificationCharacteristicInner](docs/DocumentClassificationCharacteristicInner.md)
 - [DocumentIdCharacteristic](docs/DocumentIdCharacteristic.md)
 - [DocumentIdCharacteristicInner](docs/DocumentIdCharacteristicInner.md)
 - [DocumentVersions](docs/DocumentVersions.md)
 - [DocumentVersionsInner](docs/DocumentVersionsInner.md)
 - [Documents](docs/Documents.md)
 - [DocumentsInner](docs/DocumentsInner.md)
 - [EoLOptions](docs/EoLOptions.md)
 - [EoLStory](docs/EoLStory.md)
 - [Error](docs/Error.md)
 - [ErrorResponse](docs/ErrorResponse.md)
 - [FurtherInformation](docs/FurtherInformation.md)
 - [GeneralInformation](docs/GeneralInformation.md)
 - [InReview](docs/InReview.md)
 - [IsParentOfCharacteristic](docs/IsParentOfCharacteristic.md)
 - [LanguageSet](docs/LanguageSet.md)
 - [LanguageSetInner](docs/LanguageSetInner.md)
 - [Logo](docs/Logo.md)
 - [ManufacturerDocumentation](docs/ManufacturerDocumentation.md)
 - [Material](docs/Material.md)
 - [MaterialCharacteristic](docs/MaterialCharacteristic.md)
 - [MaterialCollection](docs/MaterialCollection.md)
 - [MaterialCollectionInner](docs/MaterialCollectionInner.md)
 - [MaterialNamesEnumeration](docs/MaterialNamesEnumeration.md)
 - [MaterialTypesEnumeration](docs/MaterialTypesEnumeration.md)
 - [MemberCompany](docs/MemberCompany.md)
 - [MemberCompanyRole](docs/MemberCompanyRole.md)
 - [MultiLanguageProperty](docs/MultiLanguageProperty.md)
 - [PartAspectUpdate](docs/PartAspectUpdate.md)
 - [PartId](docs/PartId.md)
 - [PartIndividualDataCharacteristic](docs/PartIndividualDataCharacteristic.md)
 - [PartInfo](docs/PartInfo.md)
 - [PartRelationship](docs/PartRelationship.md)
 - [PartRelationshipUpdate](docs/PartRelationshipUpdate.md)
 - [PartRelationshipUpdateList](docs/PartRelationshipUpdateList.md)
 - [PartRelationshipWithInfos](docs/PartRelationshipWithInfos.md)
 - [PartStaticDataCharacteristic](docs/PartStaticDataCharacteristic.md)
 - [PartTreeParentCharacteristic](docs/PartTreeParentCharacteristic.md)
 - [PartTypeNameUpdate](docs/PartTypeNameUpdate.md)
 - [PartUniqueDataCharacteristic](docs/PartUniqueDataCharacteristic.md)
 - [PerformanceIndicatorCharacteristic](docs/PerformanceIndicatorCharacteristic.md)
 - [ProductClassifications](docs/ProductClassifications.md)
 - [ProductClassificationsInner](docs/ProductClassificationsInner.md)
 - [ProductDescription](docs/ProductDescription.md)
 - [ProductDimension3D](docs/ProductDimension3D.md)
 - [ProductImages](docs/ProductImages.md)
 - [ProductImagesInner](docs/ProductImagesInner.md)
 - [ProductUsage](docs/ProductUsage.md)
 - [QualityAlertDataCharacteristic](docs/QualityAlertDataCharacteristic.md)
 - [QualityTypeEnum](docs/QualityTypeEnum.md)
 - [RecyclingOptionsSelection](docs/RecyclingOptionsSelection.md)
 - [RecyclingProcessCharacteristic](docs/RecyclingProcessCharacteristic.md)
 - [RecyclingStatusCharacteristic](docs/RecyclingStatusCharacteristic.md)
 - [ReferenceElementSet](docs/ReferenceElementSet.md)
 - [Released](docs/Released.md)
 - [ReturnRequest](docs/ReturnRequest.md)
 - [ReuseOptionsSelection](docs/ReuseOptionsSelection.md)
 - [Role](docs/Role.md)
 - [SetOfDocumentTypes](docs/SetOfDocumentTypes.md)
 - [StatusValueCharacteristic](docs/StatusValueCharacteristic.md)
 - [StepsSequenceCharacteristics](docs/StepsSequenceCharacteristics.md)
 - [StepsSequenceCharacteristicsInner](docs/StepsSequenceCharacteristicsInner.md)
 - [TechnicalData](docs/TechnicalData.md)
 - [TechnicalProperties](docs/TechnicalProperties.md)
 - [TextStatementSet](docs/TextStatementSet.md)
 - [Traceability](docs/Traceability.md)
 - [VehicleHealthStatus](docs/VehicleHealthStatus.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

christian.kabelin@partner.bmw.de

