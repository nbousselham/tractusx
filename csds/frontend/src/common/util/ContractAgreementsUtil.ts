export const CONTRACT_AGREEMENTS_TABLE_HEADERS = [
  {
    text: "contract name",
    align: "start",
    value: "contractName",
  },
  { text: "status", value: "status" },
  { text: "date established", value: "dateEstablished" },
  { text: "data consumer", value: "dataConsumer" },
  { text: "access limited by use case", value: "accessControlUseCase" },
  { text: "access limited by company role", value: "accessControlByRole" },
  { text: "usage control", value: "usageControlType" },
  { text: "", align: "end", value: "actions", sortable: false },
];

export const contractAgreementItems = [
  {
    id: 1,
    contractName: "Contract BMW June2021",
    status: "suspended",
    dateEstablished: "06/15/2021",
    dataConsumerName: "BMW",
    accessLimitedByUsecase: ["Circular economy", "Traceability"],
    accessLimitedByCompanyRole: ["Recycler", "OEM"],
    usageControl: ["Limited"],
  },
  {
    id: 2,
    contractName: "Contract Bosch May2021",
    status: "accepted",
    dateEstablished: "05/02/2021",
    dataConsumerName: "Kaputt",
    accessLimitedByUsecase: ["Unlimited"],
    accessLimitedByCompanyRole: ["Recycler"],
    usageControl: ["Unlimited"],
  },
  {
    id: 3,
    contractName: "Contract Kaputt  April2021",
    status: "suspended",
    dateEstablished: "04/25/2021",
    dataConsumerName: "Bosch",
    accessLimitedByUsecase: ["Traceability"],
    accessLimitedByCompanyRole: ["OEM"],
    usageControl: ["Limited"],
  },
];
