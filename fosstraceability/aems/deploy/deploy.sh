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

# Deploy Docker images for Webapp on eks
#/bin/bash 

echo 'Using Environment' $ENVIRONMENT

if [[ "$ENVIRONMENT" == "dev" ]]; then   
    
    kubectl config use-context arn:aws:eks:eu-west-1:737189273666:cluster/cx-dev-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"
    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record || echo "Maybe there is no aems-scheduler yet"

elif [[ "$ENVIRONMENT" == "int" ]]; then   

    kubectl config use-context arn:aws:eks:eu-west-1:651914698191:cluster/int-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n al --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n al --record || echo "Maybe there is no aems-scheduler yet"


elif [[ "$ENVIRONMENT" == "prod" ]]; then   

    kubectl config use-context arn:aws:eks:eu-west-1:709771689624:cluster/app-prod-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"

elif [[ "$ENVIRONMENT" == "cx-stage" ]]; then   

    kubectl config use-context arn:aws:eks:eu-west-1:859682079338:cluster/cx-dev-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"
    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record || echo "Maybe there is no aems-scheduler yet"

elif [[ "$ENVIRONMENT" == "cx-prod" ]]; then   

    kubectl config use-context arn:aws:eks:eu-west-1:859682079338:cluster/cx-prod-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"
    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record || echo "Maybe there is no aems-scheduler yet"

else 

    kubectl config use-context arn:aws:eks:eu-west-1:737189273666:cluster/cx-dev-partchain

    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n bmw --record || echo "Maybe there is no aems-scheduler yet"
    kubectl set image deployment/aems aems=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record
    kubectl set image deployment/aems-scheduler aems-scheduler=${TARGET_IMAGE_NAME}:${SHORT_COMMIT_ID} -n taas --record || echo "Maybe there is no aems-scheduler yet"

fi

