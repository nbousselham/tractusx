# TdmAdminApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**purgeTestData**](TdmAdminApi.md#purgeTestData) | **GET** /catena-x/tdm/1.0/admin/purge | Purge all current test data




<a name="purgeTestData"></a>
# **purgeTestData**
> String purgeTestData(API_KEY)

Purge all current test data

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.TdmAdminApi;



TdmAdminApi apiInstance = new TdmAdminApi();

String API_KEY = "API_KEY_example"; // String | API KEY

try {
    String result = apiInstance.purgeTestData(API_KEY);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TdmAdminApi#purgeTestData");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **API_KEY** | **String**| API KEY |


### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



