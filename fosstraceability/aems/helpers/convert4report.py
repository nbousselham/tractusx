#!/usr/bin/env python3

""" Convert4Report

Retrieve ECR docker image security scan report and convert it as JUNITXML
in order to make it readable from CodeBuild Report tool.

Parameters:
---------
failure_level: list
    List of security finding levels to be considered critical and raise a failure.

"""

import json
from xml.etree.ElementTree import Element, SubElement, Comment, tostring
import os
from xml.etree import ElementTree
from xml.dom import minidom
import logging

logging.basicConfig(format="%(levelname)s:%(message)s", level=logging.INFO)
base_dir = os.environ["CODEBUILD_SRC_DIR"]
root = Element("testsuites")

stage = os.environ["ENVIRONMENT"]
if stage == "dev":
    failure_level = []
elif stage == "int":
    failure_level = []
elif stage == "prod":
    failure_level = []
else:
    failure_level = []


def prettify_xml(elem):
    """Return a pretty-printed XML string for the Element.
    """
    rough_string = ElementTree.tostring(elem, "utf-8")
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="  ")


def prettify_json(elem):
    """Return a pretty-printed JSON string for the Elements"""
    json_string = json.dumps(elem, sort_keys=True, indent=4, separators=(",", ": "))
    return json_string


def load_json_scan_results(file):
    with open(file) as json_file:
        data = json.load(json_file)
    return data


def scan_failed(json_data, xml_root):
    attributes = {"errors": "1", "failure": "0", "skipped": "0", "tests": "1"}
    attributes.update(json_data)
    attributes.pop("imageScanFindings")
    logging.info("Test Summary %s" % attributes)
    testsuite_report = SubElement(xml_root, "testsuite", attributes)
    return testsuite_report


def build_test_cases(findings, xml_parent):

    for finding in findings:
        # logging.debug("Finding: %s ##############################" % finding)
        attributes = finding
        attributes["attributes"] = str(finding["attributes"])
        xml_child = SubElement(xml_parent, "testcase", name=attributes["name"])
        if finding["severity"] in failure_level:
            logging.info("Found High severity %s" % str(finding["description"]))
            SubElement(xml_child, "failure", message=str(prettify_json(attributes)))
        else:
            SubElement(xml_child, "skipped", message=str(prettify_json(attributes)))


def scan_completed(json_data, xml_root):
    finding_severity_counts = json_data["imageScanFindings"]["findingSeverityCounts"]
    logging.info(
        "Findings summary: %s"
        % prettify_json(json_data["imageScanFindings"]["findingSeverityCounts"])
    )
    total_findings: int = 0
    failures_findings: int = 0
    for severity in finding_severity_counts:
        total_findings += finding_severity_counts[severity]
        if severity in failure_level:
            failures_findings += finding_severity_counts[severity]

    attributes = {
        "errors": str(failures_findings),
        "failure": str(failures_findings),
        "skipped": str(total_findings - failures_findings),
        "tests": str(total_findings),
    }

    logging.info("Test Summary: %s" % prettify_json(attributes))

    attributes.update(json_data)
    attributes.pop("imageScanFindings")
    testsuite_report = SubElement(xml_root, "testsuite", attributes)
    testsuite_report.text = str(json_data["imageScanFindings"])
    logging.debug("Scan Findings %s" % json_data["imageScanFindings"])
    return testsuite_report, failures_findings


def main():
    data = load_json_scan_results(f"{base_dir}/reports/scan_results.json")
    report_path = f"{base_dir}/reports/scan_results.xml"
    failures_findings = 0

    if data["imageScanStatus"]["status"] == "COMPLETE":
        logging.info("Image scan completed successfully")
        xml_parent, failures_findings = scan_completed(data, root)
        build_test_cases(data["imageScanFindings"]["findings"], xml_parent)
    elif data["imageScanStatus"]["status"] == "FAILED":
        logging.warning(
            "Image scan FAILED. Please check the reports for more information."
        )
        scan_failed(data, root)
    else:
        logging.error("Scan Status not recognised")
        raise

    logging.info("Image Scan: printing the results under %s" % report_path)
    xml_response = prettify_xml(root)
    f = open(report_path, "a")
    f.write(xml_response)
    f.close()

    if failures_findings > 0:
        raise SystemExit("Findings in failure level %s" % failure_level)


if __name__ == "__main__":
    main()
