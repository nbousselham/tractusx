package com.csds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.model.UsagePolicies;
import com.csds.model.UsagePolicyType;
import com.csds.response.ResponseObject;
import com.csds.service.PolicesServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicesServicesImpl implements PolicesServices {


	@Override
	public ResponseObject getAllPolicy() {

		ResponseObject response = new ResponseObject();
		List<UsagePolicies> usagePoliciesList = getPolicyList();

		if(!usagePoliciesList.isEmpty()) {

			response.setData(usagePoliciesList);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
			response.setMessage(String.format(ApplicationMessageConstant.SUCCESS_OPERATION, "getAllPoliceNames"));
		}else {
			response.setMessage(ApplicationMessageConstant.NOT_FOUND);
			response.setData(null);
			response.setStatus(ApplicationMessageConstant.SUCCESS);
		}

		log.info(String.format(ApplicationMessageConstant.SUCCESS_OPERATION ,"getAllPoliceNames"));
		return response;
	}

	private List<UsagePolicies> getPolicyList() {

		List<UsagePolicies> usagePoliciesList = new ArrayList<>();

		for(UsagePolicyType uPolicy : UsagePolicyType.values()) {
			if(uPolicy.getLabel().equals(UsagePolicyType.USAGE_LOGGING.getLabel())) {
				break;
			}
			UsagePolicies usagePolicy =  new UsagePolicies();
			usagePolicy.setPolicyType(uPolicy.getLabel());
			usagePolicy.setPolicyDescription(uPolicy.getDescription());
			usagePoliciesList.add(usagePolicy);
		}
		return usagePoliciesList;
	}

}
