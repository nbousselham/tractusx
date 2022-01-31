export interface iContractAgreements {
  accessLimitedByCompanyRole: string;
  accessLimitedByUsecase: string;
  agreementInformation: string;
  contractName: string;
  createdTimeStamp: string;
  dataConsumer: string;
  dataUrl: string;
  dateEstablished: string;
  modifiedTimeStamp: string;
  status: string;
  usageControl: string;
}

export enum CONTRACT_AGREEMENT_STATUS {
  ACCEPTED = "Accepted",
  SUSPENDED = "Suspended",
  REJECTED = "Rejected",
}
