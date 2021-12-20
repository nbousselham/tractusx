import { shallowMount, createLocalVue } from "@vue/test-utils";
import dataoffer from "@/store/modules/dataoffer";
import Vuex, { Store } from "vuex";
import Vuetify from "vuetify";
import Dashboard from "@/views/dashboard/Dashboard.vue";
import { GET_DATA_OFFERS } from "@/store/modules/dataoffer/actions/action-types";

const localVue = createLocalVue();
localVue.use(Vuetify);
localVue.use(Vuex);

describe("Dashboard.vue", () => {
  let vuetify: Vuetify;
  let actions: { GET_DATA_OFFERS: any };
  // eslint-disable-next-line @typescript-eslint/ban-types
  let store: Store<{}>;

  beforeEach(() => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    vuetify = new Vuetify();
    actions = {
      GET_DATA_OFFERS: jest.fn(),
    };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    store = new Vuex.Store({
      state: {},
      modules: {
        dataoffer,
      },
      actions,
    });
  });

  it("dispatches action", () => {
    const dashboardWrapper = shallowMount(Dashboard, {
      store,
      localVue,
      vuetify,
    });
    expect(actions.GET_DATA_OFFERS).toHaveBeenCalled();
  });
});
