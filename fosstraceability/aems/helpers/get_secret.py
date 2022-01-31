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

    short_options = "k:s:"
    long_options = ["kubepath", "secretName"]

    try:
        arguments, values = getopt.getopt(sys.argv[1:], short_options, long_options)
    except getopt.GetoptError as err:
        # Output error, and return with an error code
        print(str(err))
        sys.exit(2)

    kubeconfig_directory = ""
    secret_name = ""
    for current_argument, current_value in arguments:
        if current_argument in ("-k", "--kubepath"):
            kubeconfig_directory = current_value
            print("kubeconfig path is %s" % kubeconfig_directory)
        if current_argument in ("-s", "--secretName"):
            secret_name = current_value
            print("Secret name is %s" % secret_name)

    region_name = "eu-west-1"

    # Create a Secrets Manager client
    session = boto3.session.Session()
    client = session.client(service_name="secretsmanager", region_name=region_name)

    # In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
    # See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
    # We rethrow the exception by default.
    secret = ""

    try:
        print("getting secret...")
        get_secret_value_response = client.get_secret_value(SecretId=secret_name)
    except Exception as e:
        raise e
    else:
        # Decrypts secret using the associated KMS CMK.
        # Depending on whether the secret is a string or binary, one of these fields will be populated.
        if "SecretString" in get_secret_value_response:
            secret = get_secret_value_response["SecretString"]
        else:
            secret = base64.b64decode(get_secret_value_response["SecretBinary"])

    with open(kubeconfig_directory, "w") as file:
        file.write(secret)


if __name__ == "__main__":
    get_secret()