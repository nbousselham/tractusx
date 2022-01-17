export interface iOrganization {
  baseUrl: string;
  id: string;
  name: string;
  role: string;
  status: string;
}
export interface iUsageControl {
  type: string;
  value?: number | string;
  url: string;
}
export interface iOfferIdsDetails {
  artifactId: string;
  artifactSelfHref: string;
  catalogId: string;
  contractId: string;
  offerId: string;
  offerSelfHref: string;
  representationId: string;
  representationSelfHref: string;
  ruleIdList: string[];
  ruleSelfHref: string[];
}
export interface iDataOffers {
  fileName: string;
  accessControlUseCase: string[];
  accessControlUseCaseType: string;
  byOrganization: iOrganization[];
  byOrganizationRole: string[];
  contractEndsinDays: number;
  description: string;
  fileId: string;
  offerIDSdetails: iOfferIdsDetails;
  title: string;
  _id: string;
  usageControl: iUsageControl[];
}

export interface iUseCase {
  id: number;
  name: string;
}

export interface iOrgRoles {
  id: number;
  role: string;
}
