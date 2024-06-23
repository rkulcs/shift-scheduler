#! /bin/bash
# Generates a key pair to be used by the application.

# Change these
STOREPASS=secret
KEYPASS=secret

keytool -genkeypair -alias shiftscheduler -keyalg RSA -keysize 4096 \
  -validity 3650 -dname "CN=localhost" -keypass $STOREPASS -keystore ./src/main/resources/keystore.p12 \
  -storeType PKCS12 -storepass $KEYPASS
