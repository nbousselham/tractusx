# PartsRelationshipServiceApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createVehicle**](PartsRelationshipServiceApi.md#createVehicle) | **GET** /catena-x/tdm/1.0/vehicle/create/{oneid} | Create new vehicle BOM




<a name="createVehicle"></a>
# **createVehicle**
> List&lt;PartRelationshipWithInfos&gt; createVehicle(oneid, count, vehicleType)

Create new vehicle BOM

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.PartsRelationshipServiceApi;



PartsRelationshipServiceApi apiInstance = new PartsRelationshipServiceApi();

String oneid = "oneid_example"; // String | OneID of manufacturer

Integer count = 56; // Integer | number of vehicles to create

String vehicleType = "vehicleType_example"; // String | Vehicle Type

try {
    List<PartRelationshipWithInfos> result = apiInstance.createVehicle(oneid, count, vehicleType);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PartsRelationshipServiceApi#createVehicle");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oneid** | [**String**](.md)| OneID of manufacturer |
 **count** | **Integer**| number of vehicles to create | [optional]
 **vehicleType** | **String**| Vehicle Type | [optional]


### Return type

[**List&lt;PartRelationshipWithInfos&gt;**](PartRelationshipWithInfos.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



