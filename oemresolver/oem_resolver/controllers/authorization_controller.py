from oem_resolver.util import _current_timestamp
from typing import List
from jose import JWTError, jwt
from werkzeug.exceptions import Unauthorized
from os import environ


JWT_ISSUER = environ.get('JWT_ISSUER')
JWT_SECRET = environ.get('JWT_SECRET')
JWT_LIFETIME_SECONDS = int(environ.get('JWT_LIFETIME_SECONDS'))
JWT_ALGORITHM = environ.get('JWT_ALGORITHM')

"""
controller generated to handled auth operation described at:
https://connexion.readthedocs.io/en/latest/security.html
"""
def check_bearerAuth(token):
    try:
        return jwt.decode(token, JWT_SECRET, algorithms=[JWT_ALGORITHM])
    except JWTError as e:
        raise Unauthorized from e
        
def generate_token(user_id):
    

    timestamp = _current_timestamp()
    payload = {
        "iss": JWT_ISSUER,
        "iat": int(timestamp),
        "exp": int(timestamp + JWT_LIFETIME_SECONDS),
        "sub": str(user_id),
    }

    return jwt.encode(payload, JWT_SECRET, algorithm=JWT_ALGORITHM)

