/* eslint-disable @typescript-eslint/explicit-module-boundary-types */
/* eslint-disable @typescript-eslint/no-explicit-any */
import axios from "axios";
import Util from "@/common/util/index";
import { ServiceUrlType } from "@/common/util/DataOfferUtil";

export default class DataOfferService {
  /**
   * DataOfferService
   * @returns {Promise}
   */

  static getDataOfferList() {
    return axios.get(
      Util.getRestApiUrl("data-Offers", ServiceUrlType.PROVIDER)
    );
  }
}
