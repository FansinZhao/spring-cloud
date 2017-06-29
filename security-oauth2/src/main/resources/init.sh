#!/usr/bin/env bash

openssl <<EOF
genrsa -out rsa_private_key.pem 64
pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM –nocrypt
rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem
EOF