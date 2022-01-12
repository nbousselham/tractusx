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
  static getAllUseCases() {
    return axios.get(Util.getRestApiUrl("use-cases", ServiceUrlType.CORE));
  }
  static getOrgRoles() {
    return axios.get(Util.getRestApiUrl("roles", ServiceUrlType.CORE));
  }
  static getOrganizationsByRole(role: string) {
    const orgsByRoleUrl = `role/${role}/organizations`;
    return axios.get(Util.getRestApiUrl(orgsByRoleUrl, ServiceUrlType.CORE));
  }
  // eslint-disable-next-line @typescript-eslint/ban-types
  static createDataOffer(payload: object) {
    const config = {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    };
    return axios.post(
      Util.getRestApiUrl("create-data-offer", ServiceUrlType.PROVIDER),
      payload,
      config
    );
  }
}
