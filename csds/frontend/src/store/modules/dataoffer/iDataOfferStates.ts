import { iDataOffers } from "@/common/interfaces/dataOffers/IDataOffers";
export interface dataOfferState {
  dataOffersList: Array<iDataOffers>;
  isDataOffersLoading: boolean;
}
