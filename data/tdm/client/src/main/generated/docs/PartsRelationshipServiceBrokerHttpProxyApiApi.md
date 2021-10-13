# PartsRelationshipServiceBrokerHttpProxyApiApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getPartAspectUpdate**](PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartAspectUpdate) | **GET** /catena-x/tdm/1.0/prs/broker/PartAspectUpdate | Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
[**getPartRelationshipUpdateList**](PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartRelationshipUpdateList) | **GET** /catena-x/tdm/1.0/prs/broker/PartRelationshipUpdateList | Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.
[**getPartTypeNameUpdate**](PartsRelationshipServiceBrokerHttpProxyApiApi.md#getPartTypeNameUpdate) | **GET** /catena-x/tdm/1.0/prs/broker/PartTypeNameUpdate | Get a PartTypeNameUpdate. Describes an update of a part type name.




<a name="getPartAspectUpdate"></a>
# **getPartAspectUpdate**
> List&lt;PartAspectUpdate&gt; getPartAspectUpdate(bpn, effectiveDateTimeStart, effectiveDateTimeEnd)

Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.PartsRelationshipServiceBrokerHttpProxyApiApi;



PartsRelationshipServiceBrokerHttpProxyApiApi apiInstance = new PartsRelationshipServiceBrokerHttpProxyApiApi();

String bpn = "bpn_example"; // String | 

LocalDate effectiveDateTimeStart = new LocalDate(); // LocalDate | 

LocalDate effectiveDateTimeEnd = new LocalDate(); // LocalDate | 

try {
    List<PartAspectUpdate> result = apiInstance.getPartAspectUpdate(bpn, effectiveDateTimeStart, effectiveDateTimeEnd);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PartsRelationshipServiceBrokerHttpProxyApiApi#getPartAspectUpdate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **bpn** | [**String**](.md)|  | [optional]
 **effectiveDateTimeStart** | **LocalDate**|  | [optional]
 **effectiveDateTimeEnd** | **LocalDate**|  | [optional]


### Return type

[**List&lt;PartAspectUpdate&gt;**](PartAspectUpdate.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


<a name="getPartRelationshipUpdateList"></a>
# **getPartRelationshipUpdateList**
> List&lt;PartRelationshipUpdateList&gt; getPartRelationshipUpdateList(bpn, effectiveDateTimeStart, effectiveDateTimeEnd)

Get a PartAspectUpdate. Describes an update of a PartAspectUpdate.

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.PartsRelationshipServiceBrokerHttpProxyApiApi;



PartsRelationshipServiceBrokerHttpProxyApiApi apiInstance = new PartsRelationshipServiceBrokerHttpProxyApiApi();

String bpn = "bpn_example"; // String | 

LocalDate effectiveDateTimeStart = new LocalDate(); // LocalDate | 

LocalDate effectiveDateTimeEnd = new LocalDate(); // LocalDate | 

try {
    List<PartRelationshipUpdateList> result = apiInstance.getPartRelationshipUpdateList(bpn, effectiveDateTimeStart, effectiveDateTimeEnd);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PartsRelationshipServiceBrokerHttpProxyApiApi#getPartRelationshipUpdateList");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **bpn** | [**String**](.md)|  | [optional]
 **effectiveDateTimeStart** | **LocalDate**|  | [optional]
 **effectiveDateTimeEnd** | **LocalDate**|  | [optional]


### Return type

[**List&lt;PartRelationshipUpdateList&gt;**](PartRelationshipUpdateList.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


<a name="getPartTypeNameUpdate"></a>
# **getPartTypeNameUpdate**
> List&lt;PartTypeNameUpdate&gt; getPartTypeNameUpdate(bpn, effectiveDateTimeStart, effectiveDateTimeEnd)

Get a PartTypeNameUpdate. Describes an update of a part type name.

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.PartsRelationshipServiceBrokerHttpProxyApiApi;



PartsRelationshipServiceBrokerHttpProxyApiApi apiInstance = new PartsRelationshipServiceBrokerHttpProxyApiApi();

String bpn = "bpn_example"; // String | 

LocalDate effectiveDateTimeStart = new LocalDate(); // LocalDate | 

LocalDate effectiveDateTimeEnd = new LocalDate(); // LocalDate | 

try {
    List<PartTypeNameUpdate> result = apiInstance.getPartTypeNameUpdate(bpn, effectiveDateTimeStart, effectiveDateTimeEnd);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PartsRelationshipServiceBrokerHttpProxyApiApi#getPartTypeNameUpdate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **bpn** | [**String**](.md)|  | [optional]
 **effectiveDateTimeStart** | **LocalDate**|  | [optional]
 **effectiveDateTimeEnd** | **LocalDate**|  | [optional]


### Return type

[**List&lt;PartTypeNameUpdate&gt;**](PartTypeNameUpdate.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



