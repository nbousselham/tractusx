#!/usr/bin/env python3
 
import requests
import json
import os
import base64

url = "https://itgov-ci.bmwgroup.net/sonar/api/measures/component"

projectKey = os.environ["SONARPROJECTKEY"]
loginToken = os.environ["SONARLOGIN"]
loginToken = f"{loginToken}:"
loginToken = base64.b64encode(bytes(loginToken, encoding= 'utf-8')).decode('utf-8')

querystring = {"metricKeys":"ncloc,complexity,violations,new_violations,bugs,new_bugs,reliability_rating,vulnerabilities,new_vulnerabilities,security_rating,code_smells,new_code_smells,sqale_rating","component":projectKey}

payload = ""
headers = {"Authorization": f"Basic {loginToken}"}

response = requests.request("GET", url, data=payload, headers=headers, params=querystring)

result = json.loads(response.text)
measures = result['component']['measures']

# get the number and rating for each issuetype
vulnerabilities = int([x['value'] for x in measures if x['metric'] == 'vulnerabilities'][0])
new_bugs = int([x['periods'][0]['value'] for x in measures if x['metric'] == 'new_bugs'][0])
# bugs = int([x['value'] for x in measures if x['metric'] == 'bugs'][0])
# new_codeSmells = int([x['periods'][0]['value'] for x in measures if x['metric'] == 'new_code_smells'][0])

# securityRating = float([x['value'] for x in measures if x['metric'] == 'security_rating'][0])
reliabilityRating = float([x['value'] for x in measures if x['metric'] == 'reliability_rating'][0])
sqaleRating = float([x['value'] for x in measures if x['metric'] == 'sqale_rating'][0])


def main():
    # check if the number and rating of measures allows us to deploy the new code
    if (new_bugs == 0) & (reliabilityRating <= 2.0) & (vulnerabilities == 0) & (sqaleRating <= 2.0):
        print("No new critical measures were found. Code can be deployed.")
    else: 
        raise SystemExit("Code can't be deployed. The following measures were found: " + str(new_bugs) + " new bugs, " + str(vulnerabilities) + " vulnerabilities, a reliabiliy rating of " + str(reliabilityRating) + " and a scale rating of " + str(sqaleRating))