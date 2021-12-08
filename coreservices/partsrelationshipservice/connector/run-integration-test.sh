#!/bin/bash
set -euo pipefail
cd ..

if [ ! -f dev/local/cert.pfx ]; then
    echo "Missing file cert.pfx"
    exit 1
fi

export DOCKER_BUILDKIT=1
# Successful parts tree retrieval IT
docker-compose --profile connector build --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD
IT_SCRIPT=connector-integration-test.sh \
docker-compose --profile connector --profile prs up --exit-code-from=connector-integration-test --abort-on-container-exit

# Failing parts tree retrieval IT (prs not available)
docker-compose --profile connector build --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD
IT_SCRIPT=connector-api-failure-integration-test.sh \
docker-compose --profile connector up --exit-code-from=connector-integration-test --abort-on-container-exit