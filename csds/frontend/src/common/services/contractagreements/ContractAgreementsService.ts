import axios from "axios";
import Util from "@/common/util/index";
import { ServiceUrlType } from "@/common/util";

export default class ContractAgreementsService {
  /**
   * ContractAgreementsService
   * @returns {Promise}
   */

  static getContractAgreements(): Promise<any> {
    return axios.get(
      Util.getRestApiUrl("read-contract-agreements", ServiceUrlType.CONSUMER)
    );
  }
}
