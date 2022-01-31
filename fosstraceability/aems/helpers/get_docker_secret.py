#!/usr/bin/env python3

import boto3
import base64
from botocore.exceptions import ClientError
from pathlib import Path
import json
import os
import json
import logging
import sys, getopt


def get_secret():

    secret_name = (
        "arn:aws:secretsmanager:eu-west-1:537473266542:secret:docker-account-rHo9Bp"
    )
    region_name = "eu-west-1"

    # Create a Secrets Manager client
    session = boto3.session.Session()
    client = session.client(service_name="secretsmanager", region_name=region_name)

    # In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
    # See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
    # We rethrow the exception by default.
    secret = ""

    try:
        print(f"getting secret for secret id {secret_name}")
        get_secret_value_response = client.get_secret_value(SecretId=secret_name)
    except Exception as e:
        raise e
    else:
        # Decrypts secret using the associated KMS CMK.
        # Depending on whether the secret is a string or binary, one of these fields will be populated.
        if "SecretString" in get_secret_value_response:
            secret = json.loads(get_secret_value_response["SecretString"])
        else:
            secret = json.loads(
                base64.b64decode(get_secret_value_response["SecretBinary"])
            )

    print("export DOCKER_SECRET=%s" % secret["docker-password"])


if __name__ == "__main__":
    secret = get_secret()
