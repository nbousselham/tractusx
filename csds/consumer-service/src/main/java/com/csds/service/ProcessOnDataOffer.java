package com.csds.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.csds.entity.ContractAgreementsInformation;
import com.csds.model.ConsumerOfferResource;
import com.csds.model.ConsumerOfferResourceContract;
import com.csds.model.ConsumerOfferResourceRepresentation;
import com.csds.model.ContractAgreementsResponse;
import com.csds.model.DataOfferDetails;
import com.csds.model.QueryDataOfferModel;

public interface ProcessOnDataOffer {

	public static final String UNLIMITED = "Unlimited";

	Function<List<String>, String> GetAsString = strList -> {
		return String.join(", ", strList);
	};

	Function<ConsumerOfferResource, QueryDataOfferModel> OfferProcessFunction = offerInfo -> {
		ConsumerOfferResourceContract contractOffer = offerInfo.getContractOffer().get(0);
		ConsumerOfferResourceRepresentation representation = offerInfo.getRepresentation().get(0);

		List<Map<String, String>> collect = contractOffer.getPermission().stream().map(permission -> {
			Map<String, String> permissionMap = new HashMap<>();
			permissionMap.put("ruleId", permission.getId());
			permissionMap.put("ruleValue", permission.getDescription().get(0).getValue());
			return permissionMap;
		}).collect(Collectors.toList());

		QueryDataOfferModel queryDataOfferModel = QueryDataOfferModel.builder().id(offerInfo.getId())
				.contractInfo(contractOffer.getId()).representation(representation.getId())
				.artifact(representation.getArtifacts().get(0).getId()).contractRules(collect).build();
		return queryDataOfferModel;
	};

	Function<ContractAgreementsInformation, ContractAgreementsResponse> ContractProcessFunction = contractInfo -> {

		ContractAgreementsResponse contResponse = ContractAgreementsResponse.builder()
				.contractName(contractInfo.getContractName()).dataConsumer(contractInfo.getDataConsumer())
				.dateEstablished(contractInfo.getDateEstablished()).status(contractInfo.getStatus())
				.createdTimeStamp(contractInfo.getCreatedTimeStamp())
				.agreementInformation(contractInfo.getAgreementRemoteId())
				.dataUrl(contractInfo.getDataUrl())
				.modifiedTimeStamp(contractInfo.getModifiedTimeStamp()).build();

		DataOfferDetails dataOfferDetails = contractInfo.getDataOfferDetails();
		if (dataOfferDetails != null) {
			contResponse.setAccessLimitedByCompanyRole(
					dataOfferDetails.getAccessControlByRoleType().equals(UNLIMITED) ? UNLIMITED
							: GetAsString.apply(dataOfferDetails.getByOrganizationRole()));

			contResponse.setAccessLimitedByUsecase(
					dataOfferDetails.getAccessControlUseCaseType().equals(UNLIMITED) ? UNLIMITED
							: GetAsString.apply(dataOfferDetails.getAccessControlUseCase()));

			contResponse.setUsageControl(dataOfferDetails.getUsageControlType());
		}

		return contResponse;
	};

}