# TraceabilityApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getTraceability**](TraceabilityApi.md#getTraceability) | **GET** /catena-x/tdm/1.0/traceability/{oneid} | get all Traceability information for OneID




<a name="getTraceability"></a>
# **getTraceability**
> List&lt;Traceability&gt; getTraceability(oneid)

get all Traceability information for OneID

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.TraceabilityApi;



TraceabilityApi apiInstance = new TraceabilityApi();

String oneid = "oneid_example"; // String | The member company owning the requested Twin.

try {
    List<Traceability> result = apiInstance.getTraceability(oneid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TraceabilityApi#getTraceability");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **oneid** | [**String**](.md)| The member company owning the requested Twin. |


### Return type

[**List&lt;Traceability&gt;**](Traceability.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



