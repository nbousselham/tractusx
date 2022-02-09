export interface TwinList {
  items: DigitalTwin[],
  totalItems: number,
  itemCount: number,
  currentPage: number,
  totalPages: number
}

export interface DigitalTwin {
  administration: Administration,
  description: Description[],
  endpoints: Endpoints[],
  globalAssetId: {
    value: [
      string
    ]
  },
  idShort: string,
  identification: string,
  specificAssetIds: [
    {
      key: string,
      semanticId: semanticId,
      subjectId: {
        value: [
          string
        ]
      },
      value: string
    }
  ],
  submodelDescriptors: [
    {
      administration: Administration,
      description: Description[],
      endpoints: Endpoints[],
      idShort: string,
      identification: string,
      semanticId: semanticId
    }
  ]
}

interface Administration {
  revision: string,
  version: string
}

interface Description {
  language: string,
  text: string
}

interface Endpoints {
  interface: string,
  protocolInformation: {
    endpointAddress: string,
    endpointProtocol: string,
    endpointProtocolVersion: string,
    subprotocol: string,
    subprotocolBody: string,
    subprotocolBodyEncoding: string
  }
}

interface semanticId {
  value: string[]
}
