#!/bin/bash

set -euo pipefail

purge_output_file=purge.txt.tmp
json_data_file=data.json.tmp

curl -f 'http://localhost:8080/catena-x/tdm/1.0/admin/purge?API_KEY=SPEEDBOAT' -H "accept: application/json" > $purge_output_file
curl -f 'http://localhost:8080/catena-x/tdm/1.0/vehicle/create/CAXLZJVJEBYWYYZZ?count=3&vehicleType=G31' -H "accept: application/json" > $json_data_file

echo 'COPY public.part_attribute (name, oneidmanufacturer, objectidmanufacturer, value, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[].partInfos | .[] | .part as $p | ["partTypeName", $p.oneIDManufacturer, $p.objectIDManufacturer, .partTypeName, $n, $n] | @csv' $json_data_file
echo '\.'

echo 'COPY public.part_aspect (name, oneidmanufacturer, objectidmanufacturer, url, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[].partInfos | .[] | .part as $p | .aspects[] | [.name, $p.oneIDManufacturer, $p.objectIDManufacturer, .url, $n, $n] | @csv' $json_data_file
echo '\.'

echo 'COPY public.part_relationship (oneidmanufacturer, objectidmanufacturer, parent_oneidmanufacturer, parent_objectidmanufacturer, part_relationship_list_id, upload_date_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[].relationships | .[] | [.child.oneIDManufacturer, .child.objectIDManufacturer, .parent.oneIDManufacturer, .parent.objectIDManufacturer, "78F4BB1B-2EBB-418C-9C16-3E74BACCBEAC", $n] | @csv' $json_data_file
echo '\.'
