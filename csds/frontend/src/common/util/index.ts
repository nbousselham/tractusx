/* eslint-disable @typescript-eslint/ban-types */
import Vue from "vue";
import { ServiceUrlType } from "@/common/util/DataOfferUtil";

interface QueryObj {
  /* eslint-disable-next-line */
  [key: string]: any;
}

export default class Util {
  static PROVIDER_API_HOST_URL = process.env.VUE_APP_PROVIDER_API_HOST_URL;
  static CORE_API_HOST_URL = process.env.VUE_APP_CORE_API_HOST_URL;

  static FORM_DATA_CONFIG_HEADER = {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  };

  static getQueryParameters(attr: QueryObj): string {
    let str = "";
    const keys = Object.keys(attr);
    keys.forEach((key) => {
      if (str !== "") {
        str += "&";
      }
      str = `${str}${key}=${encodeURIComponent(attr[key])}`;
    });
    return str;
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  static buildUrl(path: string, url: string, attr?: object, append?: boolean) {
    let totalUrl = `${path}/${url}`;
    const parameters = attr ? this.getQueryParameters(attr) : null;
    if (append) {
      totalUrl = `${path}${url}`;
      return parameters ? `${totalUrl}&${parameters}` : totalUrl;
    }
    return parameters ? `${totalUrl}?${parameters}` : totalUrl;
  }

  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  static getRestApiUrl(url: string, serviceType: string, attr?: object) {
    return serviceType === ServiceUrlType.PROVIDER
      ? this.buildUrl(this.PROVIDER_API_HOST_URL, url, attr)
      : this.buildUrl(this.CORE_API_HOST_URL, url, attr);
  }

  /**
   * Create two way mapper Getter <-> Mutation for computed property
   * @param {string} getter
   * @param {string} mutation
   * @returns {any} Mapper for computed property
   */
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  static mapTwoWay<T>(getter: string, mutation: string) {
    return {
      get(this: Vue): T {
        return this.$store.getters[getter];
      },
      set(this: Vue, value: T) {
        this.$store.commit(mutation, value);
      },
    };
  }

  static getIdentoVerificationURL(): string {
    return process.env.VUE_APP_IDENTO_URL.replace(
      "token-value",
      localStorage.getItem("authToken")
    );
  }
}
