<!---
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)

See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
-->

The Catena-X anonymizer is a federated service which replaces sensible business identifiers with
a special form of pseudonyms, here called anonyms.

As pseudonyms, anonyms are non-reversible in that you cannot infer the business identifiers from them.

As pseudonyms, anonyms are traceable in that they are consistenly mapped.

Unlike pseudonyms, anonyms can be "forgotten" on-demand by the platform.

### Configuration:

All configuration placed in `../infrastructure/manifests/semantics.yaml`

### Build Package:

Run `mvn install` to run unit tests, build and install the package (and submodules)

### Build & Run Package Locally:

Run `./run_local.sh` to run the (default, central) anonymizer locally.

Run 'curl --location --request POST 'http://localhost:8080/api/v1/anonymize' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '[
{
"domain": "urn:standard:org.iso:9721#",
"id": "3GNDA53P88S595616"
},
{
"domain": "urn:standard:org.iso:9721#",
"id": "WBADA53P88S595616"
}
]'
'

to produce some pseudonyms for the given VINs.

Rerun the same command to verify that the pseudonyms are indeed persistent (as long as the process lives).

Run 'curl --location --request POST 'http://localhost:8080/api/v1/forget' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--data-raw '[
{
"domain": "urn:standard:org.iso:9721#",
"id": "WBADA53P88S595616"
}
]' 
'

to forget the pseudonym for the second VIN (a total number of 1 forgotten pseudonyms should appear) and rerun the previous command to verify that only
the pseudonym to that VIN has changed.

Run `./run_local.sh -second` to run a second (federated) anonymizer locally.

Run 'curl --location --request PUT 'http://localhost:8080/api/v1/federate' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--data-raw '{
"endpoint": "http://localhost:8081/api/v1",
"expression": "urn:standard:org.iso:9721#(WBA|WBS|WBX|DAB|LBV|MMF|PM1|3MW|4US|5U|98M).{14,15}"
}'
'

to register the second anonymizer as responsible for BMW-type VINS.

And rerun the previous anonymization request to verify that indeed the namespace of the pseudonym for the matching VIN did change.

### Build Docker:

Run `cd ..;docker build -f anonymizer/Dockerfile --build-arg MAVEN_OPTS="-Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT}" -t $REGISTRY/semantics/anonymizerdev:$VERSION .;cd anonymizer`
RUN `docker push $REGISTRY/semantics/anonymizerdev:$VERSION`

where $REGISTRY is set to the target container/docker repository (like `catenaxacr.azurecr.io`) and $VERSION is set to the 
deployment version (usually `latest`).

### Redeploy in target environment

Run `kubectl rollout restart deployment anonymizer -n semantics`
