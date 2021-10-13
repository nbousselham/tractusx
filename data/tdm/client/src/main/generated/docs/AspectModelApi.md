# AspectModelApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAspect**](AspectModelApi.md#getAspect) | **GET** /catena-x/tdm/1.0/aspect/{aspect}/{oneID}/{partUniqueID} | Retrieve Aspect




<a name="getAspect"></a>
# **getAspect**
> List&lt;Object&gt; getAspect(aspect, oneID, partUniqueID)

Retrieve Aspect

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.AspectModelApi;



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
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **aspect** | **String**| Aspect Name |
 **oneID** | **String**| Business Partner OneID |
 **partUniqueID** | **String**| UniqueID of part |


### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



