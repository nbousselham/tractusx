export enum ServiceUrlType {
  PROVIDER = "PROVIDER",
  CORE = "CORE",
}

export const DATA_OFFER_TABLE_HEADERS = [
  {
    text: "Data offer Name",
    align: "start",
    value: "title",
  },
  { text: "File", value: "fileName" },
  { text: "Use cases", value: "accessControlUseCase" },
  { text: "Access limited by use case", value: "accessControlUseCaseType" },
  { text: "Access control", value: "byOrganizationRole" },
  { text: "Access limited by company role", value: "accessControlByRoleType" },
  { text: "Usage control", value: "usageControlType" },
  { text: "Contract ends in (Days)", value: "contractEndsinDays" },
  { text: "", align: "end", value: "actions", sortable: false },
];

export const DATE_TODAY = new Date(
  Date.now() - new Date().getTimezoneOffset() * 60000
)
  .toISOString()
  .substr(0, 10);
