import {
  iDataOffers,
  iUseCase,
  iOrgRoles,
} from "@/common/interfaces/dataOffers/IDataOffers";
export interface dataOfferState {
  dataOffersList: Array<iDataOffers>;
  isDataOffersLoading: boolean;
  useCaseList: Array<iUseCase>;
  organizationRoles: Array<iOrgRoles>;
  orgsByRole: Array<any>;
  selectedOrgRoles: Array<iOrgRoles>;
  dataOfferFile: Record<string, unknown>;
  isNewDataOfferLoading: boolean;
  isFileError: boolean;
  isCreateOfferError: boolean;
}
