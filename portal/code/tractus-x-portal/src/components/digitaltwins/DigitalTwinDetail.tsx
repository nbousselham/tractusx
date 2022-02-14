import { useEffect, useState } from "react"
import { Link } from "react-router-dom";
import ErrorMessage from "../ErrorMessage";
import DescriptionList from "../lists/descriptionlist"
import Loading from "../loading";
import BackLink from "../navigation/BackLink"
import { getTwinById } from "./data"
import { DigitalTwin } from "./interfaces";
import Submodels from "./Submodels";

export function DigitalTwinDetail(props){
  const id = props.match.params.id;
  const [twin, setTwin] = useState<DigitalTwin>();
  const [error, setError] = useState('');

  useEffect(() => {
    getTwinById(id).then(
      res => setTwin(res),
      error => setError(error.message)
    )
  }, [id])


  return(
    <div className="p44">
      <BackLink history={props.history} />
      {twin ?
        <>
          <div className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
            <h2 className='fs28 bold' style={{textTransform: 'uppercase'}}>{twin.idShort || twin.identification}</h2>
            <div className='mt20 mb15'>
              {twin.description[0] &&
                <DescriptionList title="Description" description={twin.description[0].text} />
              }
              {twin.submodelDescriptors && 
                <DescriptionList title="Submodel Endpoints Count" description={twin.submodelDescriptors.length}/>
              }
              {twin.administration &&
                <div className='mt20'>
                  <h3 className="fs20 bold">Administration</h3>
                  <DescriptionList title="Version" description={twin.administration.version} />
                  <DescriptionList title="Revision" description={twin.administration.revision} />
                </div>
              }
              {twin.specificAssetIds.length > 0 &&
                <div className='mt20'>
                  <h3 className="fs20 bold">Specific Asset IDs</h3>
                  {twin.specificAssetIds.map(saId =>
                    <div key={saId.key} className="mt15 mb15">
                      <DescriptionList title="Key" description={saId.key} />
                      {saId.semanticId &&
                        <DescriptionList title="Semantic ID" description={saId.semanticId} />
                      }
                      {saId.subjectId &&
                        <DescriptionList title="Subject ID" description={saId.subjectId} />
                      }
                      <DescriptionList title="Value" description={saId.value} />
                    </div>
                  )}
                </div>
              }
            </div>
          </div>
          {twin.submodelDescriptors.length > 0 &&
            <div className='mt20'>
              <h3 className="fs20 bold">Submodel Descriptors</h3>
              <Submodels models={twin.submodelDescriptors}></Submodels>
            </div>
          }
        </> :
        <Loading />
      }
      {error && <ErrorMessage error={error} />}
    </div>
  )
}
