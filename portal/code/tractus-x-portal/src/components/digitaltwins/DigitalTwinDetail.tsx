import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import ErrorMessage from "../ErrorMessage";
import DescriptionList from "../lists/descriptionlist"
import Loading from "../loading";
import BackLink from "../navigation/BackLink"
import { getTwinById } from "./data"
import { DigitalTwin } from "./interfaces";

export function DigitalTwinDetail(props){
  const id = props.match.params.id;
  const [twin, setTwin] = useState<DigitalTwin>();
  const [error, setError] = useState('');

  useEffect(() => {
    getTwinById(id).then(
      res => {setTwin(res); console.log(res)},
      error => setError(error.message)
    )
  }, [id])


  return(
    <div className="p44">
      <BackLink history={props.history} />
      {twin ?
        <div className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
          <h2 className='fs24 bold'>{twin.idShort || twin.identification}</h2>
          <div className='mt20 mb30'>
            <DescriptionList title="Description" description={twin.description[0].text} />
            {twin.administration &&
              <>
                <h3 className="fs20 bold">Administration</h3>
                <DescriptionList title="Version" description={twin.administration.version} />
                <DescriptionList title="Revision" description={twin.administration.revision} />
              </>
            }
            {twin.submodelDescriptors && <DescriptionList title="Submodel Endpoints Count" description={twin.submodelDescriptors.length}/>}
            <div className='mt20'>
              <h3 className="fs20 bold">Endpoints</h3>
              {twin.endpoints && twin.endpoints.map(twinEp => (
                <>
                  <DescriptionList title="Interface" description={twinEp.interface}/>
                  <dl>
                    <dt className='dib minw150 fs14 fggrey'>Adress</dt>
                    <dd className='fs14 fg5a dib'>
                      <Link className="mr20" to={{
                        pathname: `/home/semanticmodel/${twinEp.protocolInformation.endpointAddress}`
                      }}>Endpoint Adress</Link>
                    </dd>
                  </dl>
                  <DescriptionList title="Protocol" description={twinEp.protocolInformation.endpointProtocol}/>
                  <DescriptionList title="Protocol Version" description={twinEp.protocolInformation.endpointProtocolVersion}/>
                </>
              ))}
            </div>
            <div className='mt20'>
              <h3 className="fs20 bold">Submodel Descriptors</h3>
              {twin.submodelDescriptors.map(submodel => (
                <div key={submodel.identification} className='mt20'>
                  <DescriptionList title="Name" description={submodel.idShort}/>
                  <DescriptionList title="Description" description={submodel.description[0]}/>
                  <DescriptionList title="Endpoints Count" description={submodel.endpoints.length}/>
                  <dl>
                    <dt className='dib minw150 fs14 fggrey'>Semantic ID</dt>
                    <dd className='fs14 fg5a dib'>
                      <Link
                        className="ml25"
                        to={{
                          pathname: `/home/semanticmodel/${submodel.semanticId.value[0]}`,
                          state: submodel.semanticId.value[0]
                        }}
                      >
                        {submodel.semanticId.value[0]}
                      </Link>
                    </dd>
                  </dl>
                  <h4 className="dib mt20 fs14">Endpoints</h4>
                  {submodel.endpoints.map(endpoint => (
                    <div key={endpoint.interface} className="ml25 mt10">
                      <DescriptionList title="Interface" description={endpoint.interface}/>
                      <dl>
                        <dt className='dib minw150 fs14 fggrey'>Adress</dt>
                        <dd className='fs14 fg5a dib'>
                          <Link className="mr20" to={{
                            pathname: `/home/aspect/${endpoint.protocolInformation.endpointAddress}`
                          }}>Aspect IDS Connector URL</Link>
                        </dd>
                      </dl>
                      <DescriptionList title="Protocol" description={endpoint.protocolInformation.endpointProtocol}/>
                      <DescriptionList title="Protocol Version" description={endpoint.protocolInformation.endpointProtocolVersion}/>
                    </div>
                  ))}
                </div>
              ))}
            </div>
          </div>
        </div> :
        <Loading />
      }
      {error && <ErrorMessage error={error} />}
    </div>
  )
}
