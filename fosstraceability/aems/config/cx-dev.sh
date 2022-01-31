#
# Copyright 2021 The PartChain Authors. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Setup DEV specific environment variables
#/bin/bash

export ACCOUNT_ID="859682079338"
export ECR_REPO_NAME=asset-exchange-cx-dev-registry

if [[ "${ENVIRONMENT}" ]]; then
    echo 'Setting variables for stage' $ENVIRONMENT
    readonly TARGET_IMAGE_NAME="${ACCOUNT_ID}.dkr.ecr.eu-west-1.amazonaws.com/${ECR_REPO_NAME}"
    export TARGET_IMAGE_NAME
    echo 'TARGET_IMAGE_NAME:' $TARGET_IMAGE_NAME

    readonly KUBERNETES_SECRET="cx-dev/${PROJECT_NAME}/kubernetesconfig"
    export KUBERNETES_SECRET
    echo 'KUBERNETES_SECRET:' $KUBERNETES_SECRET

    # In case of critical security issue on the docker image delete it
    readonly DELETE_ON_FAILURE="No"
    export DELETE_ON_FAILURE

    readonly CROSS_ACC_ROLE=cx-dev-eks-crossaccount-deployment
    export CROSS_ACC_ROLE

else
    echo 'No environment variable found'
    exit 1
fi

