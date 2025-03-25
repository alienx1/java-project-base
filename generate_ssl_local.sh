#!/bin/bash

# ตั้งค่าตำแหน่งที่เก็บไฟล์ keystore และรหัสผ่าน
KEYSTORE_PATH="./web/src/main/resources/ssl/app_keystore.p12"
KEYSTORE_PASSWORD="b6+aT/+E86N9FiFCSLOyJw=="
ALIAS="ssl"
KEY_ALG="RSA"
KEY_SIZE="2048"
VALIDITY="3650"

# กำหนด Distinguished Name (DN)
DN="CN=TH, OU=Thailand, O=Bangkok, L=LocalHost, ST=Pathum Thani, C=TH"

# สร้าง keystore โดยไม่ต้องให้ผู้ใช้กรอกข้อมูล
keytool -genkeypair -alias $ALIAS -keyalg $KEY_ALG -keysize $KEY_SIZE -keystore $KEYSTORE_PATH -storetype PKCS12 -validity $VALIDITY -storepass $KEYSTORE_PASSWORD -dname "$DN"

if [ -f "$KEYSTORE_PATH" ]; then
    echo "Keystore successfully created at $KEYSTORE_PATH"
else
    echo "Failed to create keystore"
fi
