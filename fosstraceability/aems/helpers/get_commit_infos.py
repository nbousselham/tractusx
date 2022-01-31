#!/usr/bin/env python3

import boto3
import os
from botocore.client import ClientError
import json
import logging
import sys, getopt


def main():

    short_options = "hb:k:v:"
    long_options = ["help", "bucket=", "key=", "version="]

    try:
        arguments, values = getopt.getopt(sys.argv[1:], short_options, long_options)
    except getopt.GetoptError as err:
        # Output error, and return with an error code
        print(str(err))
        sys.exit(2)

    bucket = ""
    version = ""
    key = ""

    for current_argument, current_value in arguments:
        if current_argument in ("-b", "--bucket"):
            bucket = current_value
            print("Bucket name is %s" % bucket)
        elif current_argument in ("-h", "--help"):
            print("Displaying help")
            print("./")
        elif current_argument in ("-v", "--version"):
            version = current_value
            print("S3 object version is %s" % version)
        elif current_argument in ("-k", "--key"):
            key = current_value
            print("S3 object key is %s" % key)

    client = boto3.client("s3")
    TagSet = client.get_object_tagging(Bucket=bucket, Key=key, VersionId=version)[
        "TagSet"
    ]

    for item in TagSet:
        if "branch" == item["Key"]:
            print("export BRANCH_NAME=%s" % item["Value"])
            branch = item["Value"]
        elif "commit_id" == item["Key"]:
            print("export COMMIT_ID=%s" % item["Value"])

    if "cx-prod" in branch:
        print("export ENVIRONMENT=cx-prod")
    elif "cx-stage" in branch:
        print("export ENVIRONMENT=cx-stage")
    elif "cx" in branch:
        print("export ENVIRONMENT=dev")


if __name__ == "__main__":
    main()
