import connexion
import json
import os

from oem_resolver.models.error_response import ErrorResponse  # noqa: E501
from oem_resolver.models.resolve_bulk_request import ResolveBulkRequest  # noqa: E501
from oem_resolver.models.resolve_bulk_response import ResolveBulkResponse  # noqa: E501
from oem_resolver import util

basedir = os.path.dirname(__file__)
vinoemfile = os.path.join(basedir,'../config/OEMConnectorResolver.json')
vinoemfile = os.path.abspath(os.path.realpath(vinoemfile))


with open(vinoemfile) as json_file:
    VIN_OEM_DICT = json.load(json_file)

oemconnector = os.path.join(basedir,'../config/VINtoOEMResolver.json')
oemconnector = os.path.abspath(os.path.realpath(oemconnector))

with open(oemconnector) as json_file:
    OEM_Connector_Dict = json.load(json_file)

def resolve(body):  # noqa: E501
    """Resolves one or more VINs to the VIN/VAN Converter URL of OEMs.

    Creates a list of VIN/VAN converter addresses of OEMs based on a list of VINs. Those URLs could be pointing to simple REST interfaces, but most likely will point to a data asset URL of a connector (EDC). If there is no registered converter URL for a specific VIN, null will be returned for this value.  Currently only resolving of VIN17 and VIN23 is supported.  # noqa: E501

    :param body: 
    :type body: dict | bytes

    :rtype: ResolveBulkResponse
    """
    if connexion.request.is_json:
        body = ResolveBulkRequest.from_dict(connexion.request.get_json())  # noqa: E501

    resolved = []

    for identifier in body.identifiers:

        OEM = VIN_OEM_DICT.get(identifier[:3])
        address = OEM_Connector_Dict.get(OEM)
        resolved += [address]

    return resolved

