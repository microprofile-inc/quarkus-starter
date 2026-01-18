#!/usr/bin/env bash

ROOT_PATH=META-INF/resources

mkdir -p $ROOT_PATH
openssl genrsa -out rsaPrivateKey.pem 2048
openssl rsa -pubout -in rsaPrivateKey.pem -out $ROOT_PATH/publicKey.pem

openssl pkcs8 -topk8 -nocrypt -inform pem -in rsaPrivateKey.pem -outform pem -out $ROOT_PATH/privateKey.pem

rm rsaPrivateKey.pem
