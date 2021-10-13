# MemberCompanyServiceApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getMemberCompany**](MemberCompanyServiceApi.md#getMemberCompany) | **GET** /catena-x/tdm/1.0/member/company | Retrieve Member Company
[**getMemberCompanyRolesAll**](MemberCompanyServiceApi.md#getMemberCompanyRolesAll) | **GET** /catena-x/tdm/1.0/member/company/role/all | Retrieve all possible Roles for a Business Partner




<a name="getMemberCompany"></a>
# **getMemberCompany**
> List&lt;MemberCompany&gt; getMemberCompany(businessPartnerNumber)

Retrieve Member Company

Retrieve Member Company

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.MemberCompanyServiceApi;



MemberCompanyServiceApi apiInstance = new MemberCompanyServiceApi();

String businessPartnerNumber = "businessPartnerNumber_example"; // String | ID of Business Partner

try {
    List<MemberCompany> result = apiInstance.getMemberCompany(businessPartnerNumber);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MemberCompanyServiceApi#getMemberCompany");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **businessPartnerNumber** | [**String**](.md)| ID of Business Partner | [optional]


### Return type

[**List&lt;MemberCompany&gt;**](MemberCompany.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


<a name="getMemberCompanyRolesAll"></a>
# **getMemberCompanyRolesAll**
> List&lt;MemberCompanyRole&gt; getMemberCompanyRolesAll()

Retrieve all possible Roles for a Business Partner

Retrieve all possible Roles for a Business Partner

### Example
```java
// Import classes:
//import com.catenax.tdm.api.v1.ApiException;
//import com.catenax.tdm.api.v1.client.MemberCompanyServiceApi;



MemberCompanyServiceApi apiInstance = new MemberCompanyServiceApi();

try {
    List<MemberCompanyRole> result = apiInstance.getMemberCompanyRolesAll();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MemberCompanyServiceApi#getMemberCompanyRolesAll");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.


### Return type

[**List&lt;MemberCompanyRole&gt;**](MemberCompanyRole.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json



