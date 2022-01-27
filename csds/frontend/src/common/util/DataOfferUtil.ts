export enum AccessControlType {
  LIMITED = "Limited",
  UNLIMITED = "Unlimited",
}

export const DATA_OFFER_TABLE_HEADERS = [
  {
    text: "data offer name",
    align: "start",
    value: "title",
  },
  { text: "file", value: "fileName" },
  { text: "access limited by use case", value: "accessControlUseCaseType" },
  { text: "access limited by company role", value: "accessControlByRoleType" },
  { text: "usage control", value: "usageControlType" },
  { text: "", align: "end", value: "actions", sortable: false },
];

export const DATE_TODAY = new Date(
  Date.now() - new Date().getTimezoneOffset() * 60000
)
  .toISOString()
  .substr(0, 10);
