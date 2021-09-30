#!/bin/bash

set -euo pipefail

purge_output_file=purge.txt.tmp
json_data_file=data.json.tmp

# Replace with TDM url once available
TDM_URL=localhost:8080

# Get relationships for a known business partner
curl -f 'http://'$TDM_URL'/catena-x/tdm/1.0/prs/broker/PartRelationshipUpdateList' -H "accept: application/json" > $json_data_file

# Generate SQL to load part_relationship data (parent-child relationships)
echo 'COPY public.part_relationship (oneidmanufacturer, objectidmanufacturer, parent_oneidmanufacturer, parent_objectidmanufacturer, part_relationship_list_id, upload_date_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[].relationships | .[].relationship | [.child.oneIDManufacturer, .child.objectIDManufacturer, .parent.oneIDManufacturer, .parent.objectIDManufacturer, "78F4BB1B-2EBB-418C-9C16-3E74BACCBEAC", $n] | @csv' $json_data_file
echo '\.'

# Get part aspects for a known business partner
curl -f 'http://'$TDM_URL'/catena-x/tdm/1.0/prs/broker/PartAspectUpdate' -H "accept: application/json" > $json_data_file

# Generate SQL to load part_aspect data (aspect URLs)
echo 'COPY public.part_aspect (name, oneidmanufacturer, objectidmanufacturer, url, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[] | .part as $p | .aspects[] | [.name, $p.oneIDManufacturer, $p.objectIDManufacturer, .url, $n, $n] | @csv' $json_data_file
echo '\.'

# Get part attributes for a known business partner
curl -f 'http://'$TDM_URL'/catena-x/tdm/1.0/prs/broker/PartTypeNameUpdate' -H "accept: application/json" > $json_data_file

# Generate SQL to load part_attribute data (partTypeName field)
echo 'COPY public.part_attribute (name, oneidmanufacturer, objectidmanufacturer, value, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[] | .part as $p | ["partTypeName", $p.oneIDManufacturer, $p.objectIDManufacturer, .partTypeName, $n,
$n] | @csv' $json_data_file
echo '\.'
