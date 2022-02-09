# coding: utf-8

from __future__ import absolute_import

from flask import json
from six import BytesIO

from oem_resolver.models.error_response import ErrorResponse  # noqa: E501
from oem_resolver.models.resolve_bulk_request import ResolveBulkRequest  # noqa: E501
from oem_resolver.models.resolve_bulk_response import ResolveBulkResponse  # noqa: E501
from oem_resolver.test import BaseTestCase


class TestBusinessAPIController(BaseTestCase):
    """BusinessAPIController integration test stubs"""

    def test_resolve(self):
        """Test case for resolve

        Resolves one or more VINs to the VIN/VAN Converter URL of OEMs.
        """
        body = ResolveBulkRequest()
        response = self.client.open(
            '/v1/resolve',
            method='POST',
            data=json.dumps(body),
            content_type='application/json')
        self.assert200(response,
                       'Response body is : ' + response.data.decode('utf-8'))


if __name__ == '__main__':
    import unittest
    unittest.main()
