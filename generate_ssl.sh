#!/bin/sh

KEYSTORE_PATH="./web/src/main/resources/ssl/app_keystore.p12"
KEYSTORE_PASSWORD=$(openssl rand -base64 16)
ALIAS="ssl"
CERTIFICATE_PATH="./ssl/cert.cer"
PRIVATE_KEY_PATH="./ssl/private.key"
CA_CERT_PATH="./ssl/ca.cer"
CA_NAME="root"

# Check if the certificate file exists
if [ ! -f "$CERTIFICATE_PATH" ]; then
    echo "Certificate file ($CERTIFICATE_PATH) not found!"
    exit 1
fi

# Check if the private key file exists
if [ ! -f "$PRIVATE_KEY_PATH" ]; then
    echo "Private key file ($PRIVATE_KEY_PATH) not found!"
    exit 1
fi

# Check if CA certificate exists and set the CA option
if [ -f "$CA_CERT_PATH" ]; then
    CA_OPTION="-CAfile $CA_CERT_PATH -caname $CA_NAME"
else
    CA_OPTION=""
fi

# Ensure the directory for the keystore exists
mkdir -p "$(dirname "$KEYSTORE_PATH")"

# Creating PKCS12 keystore
echo "Creating PKCS12 keystore..."
openssl pkcs12 -export -in "$CERTIFICATE_PATH" -inkey "$PRIVATE_KEY_PATH" -out "$KEYSTORE_PATH" -name "$ALIAS" -password pass:$KEYSTORE_PASSWORD $CA_OPTION

# Check if the keystore was successfully created
if [ -f "$KEYSTORE_PATH" ]; then
    echo "Keystore successfully created at $KEYSTORE_PATH"
else
    echo "Failed to create keystore"
    exit 1
fi

# Export the keystore password as an environment variable
export KEYSTORE_PASSWORD

# Importing the PKCS12 keystore into a Java keystore (if needed)
# If you need to convert the PKCS12 to JKS format, use the following keytool command
JKS_KEYSTORE_PATH="./web/src/main/resources/ssl/app_keystore.jks"

# Pass the keystore and key passwords explicitly to avoid being prompted
keytool -importkeystore -srckeystore "$KEYSTORE_PATH" -srcstoretype PKCS12 -srcstorepass "$KEYSTORE_PASSWORD" -destkeystore "$JKS_KEYSTORE_PATH" -deststoretype JKS -storepass "$KEYSTORE_PASSWORD" -srcalias "$ALIAS" -keypass "$KEYSTORE_PASSWORD" -destalias "$ALIAS"

# Check if the Java keystore was successfully created
if [ -f "$JKS_KEYSTORE_PATH" ]; then
    echo "Java keystore successfully created at $JKS_KEYSTORE_PATH"
else
    echo "Failed to create Java keystore"
    exit 1
fi

echo "Keystore creation completed successfully with password: $KEYSTORE_PASSWORD"
