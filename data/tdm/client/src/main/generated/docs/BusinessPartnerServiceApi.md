# BusinessPartnerServiceApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createBusinessPartnerNumber**](BusinessPartnerServiceApi.md#createBusinessPartnerNumber) | **GET** /catena-x/tdm/1.0/businesspartner/bpn | Create Business Partner BPN
[**getBusinessPartner**](BusinessPartnerServiceApi.md#getBusinessPartner) | **GET** /catena-x/tdm/1.0/businesspartner | Get Business Partner




<a name="createBusinessPartnerNumber"></a>
# **createBusinessPartnerNumber**
> String createBusinessPartnerNumber()

Create Business Partner BPN

Create Business Partner BPN

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.BusinessPartnerServiceApi;



BusinessPartnerServiceApi apiInstance = new BusinessPartnerServiceApi();

try {
    String result = apiInstance.createBusinessPartnerNumber();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BusinessPartnerServiceApi#createBusinessPartnerNumber");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.


### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


<a name="getBusinessPartner"></a>
# **getBusinessPartner**
> List&lt;BusinessPartner&gt; getBusinessPartner(businessPartnerNumber)

Get Business Partner

Get Business Partner

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.BusinessPartnerServiceApi;



BusinessPartnerServiceApi apiInstance = new BusinessPartnerServiceApi();

String businessPartnerNumber = "businessPartnerNumber_example"; // String | ID of Business Partner

try {
    List<BusinessPartner> result = apiInstance.getBusinessPartner(businessPartnerNumber);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BusinessPartnerServiceApi#getBusinessPartner");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **businessPartnerNumber** | [**String**](.md)| ID of Business Partner | [optional]


### Return type

[**List&lt;BusinessPartner&gt;**](BusinessPartner.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



