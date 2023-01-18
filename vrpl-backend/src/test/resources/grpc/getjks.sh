#!/bin/bash

HOST=$1
KEYSTORE=$2
STOREPASS=$3
ALIAS=$4

if [ ! -e $KEYSTORE ]; then
    printf '\n\n\n\n\n\nyes\n\n' | keytool -genkey -keyalg RSA -alias alias1 -keystore $KEYSTORE -storepass $STOREPASS
    keytool -delete -alias alias1 -keystore $KEYSTORE -storepass $STOREPASS
fi

if [ -z $ALIAS ]; then
    ALIAS="alias0"
fi

A=1
TEMP_FILENAME="$(basename `mktemp -u`)"

echo "" | openssl s_client -connect $HOST -showcerts 2>/dev/null | awk '/BEGIN CERT/ {p=1} ; p==1; /END CERT/ {p=0}' | sed ':a;N;$!ba;s/\n//g' | sed -e 's/-----END CERTIFICATE-----/-----END CERTIFICATE-----\n/g' | sed \$d | while read -r line; do    
    echo "$line" | sed -e 's/-----BEGIN CERTIFICATE-----/-----BEGIN CERTIFICATE-----\n/g;s/-----END CERTIFICATE-----/\n-----END CERTIFICATE-----\n/g' > $TEMP_FILENAME.$A; 
    openssl x509 -outform der -in $TEMP_FILENAME.$A -out $TEMP_FILENAME.$A.der;
    printf 'yes\n' | keytool -import -alias $ALIAS$A -keystore $KEYSTORE -storepass $STOREPASS -file $TEMP_FILENAME.$A.der -noprompt;
    rm -f $TEMP_FILENAME.$A
    rm -f $TEMP_FILENAME.$A.der
    let A=A+1;
done