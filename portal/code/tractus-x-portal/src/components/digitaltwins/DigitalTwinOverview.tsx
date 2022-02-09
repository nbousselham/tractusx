// Copyright (c) 2021 T-Systems
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { IContextualMenuItem, PrimaryButton, SearchBox } from '@fluentui/react';
import * as React from 'react';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../ErrorMessage';
import DescriptionList from '../lists/descriptionlist';
import Loading from '../loading';
import HelpContextMenu from '../navigation/HelpContextMenu/HelpContextMenu';
import { getTwins } from './data';
import { DigitalTwin } from './interfaces';

const helpMenuItems: IContextualMenuItem[] = [
  {
    key: 'digitwin',
    text: 'Documentation',
    href: 'https://confluence.catena-x.net/display/ARTI/Digital+Twin+Registry',
    target: '_blank',
  },
  {
    key: 'faq',
    text: 'FAQ',
    href: 'https://confluence.catena-x.net/display/ARTI/FAQ',
    target: '_blank',
  },
];

function DigitalTwinOverview(){
  const [twins, setTwins] = useState<DigitalTwin[]>();
  const [error, setError] = useState<[]>();
  const [searchInput, setSearchInput] = useState<string>()

  React.useEffect(()=>{
    getTwins()
      .then(
        res => {console.log(res); setTwins(res.items)},
        error => setError(error.message)
      );
  })

  const onClearFilter = () => {
    doSearch('');
  }

  const onSearchChange = (value) => {
    setSearchInput(value);
    doSearch(value);
  }

  const onSearchClear = () => {
    doSearch('');
  }

  const doSearch = (searchInput) => {
    console.log(`Filtering ${twins.length} twins using search ${searchInput}`);
  }

  return (
    <div className='p44 df fdc'>
      <HelpContextMenu menuItems={helpMenuItems}></HelpContextMenu>
      {twins ?
        <div>
          <h1 className="fs24 bold mb20">Digital Twins</h1>
          <div className="df aife jcfe mb20">
            <SearchBox className="w300"
              placeholder="Filter ID or description"
              value={searchInput}
              onClear={onSearchClear}
              onChange={(_, newValue) => onSearchChange(newValue)}/>
          </div>
          {twins ?
            <div className="df fwrap">
              {twins.map(twin => (
                <Link key={twin.globalAssetId[0]} className="m5 p20 bgpanel flex40 br4 bsdatacatalog tdn" to={{
                  pathname: `/home/digitaltwin/${twin.globalAssetId[0]}`
                }}>
                  <h2 className='fs24 fg191 bold mb20'>{twin.idShort}</h2>
                  <DescriptionList title="Submodel count:" description={twin.submodelDescriptors.length}/>
                  <DescriptionList title="specific assets count:" description={twin.specificAssetIds.length}/>
                </Link>
              ))}
            </div> :
            <div className="df fdc aic">
              <span className="fs20">No matches found!</span>
              <PrimaryButton text='Reset Filter' className="mt20" onClick={onClearFilter} />
          </div>
          }
        </div> :
      <div className="h100pc df jcc">
        {error ? <ErrorMessage error={error} /> : <Loading />}
      </div>
    }
    </div>
  );
}

export default DigitalTwinOverview;
