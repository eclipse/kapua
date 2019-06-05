rm ca* client* server*

echo 'Server'
openssl genrsa -out ca.key 4096 -passin changeit -passout changeit
openssl req -new -x509 -days 1826 -key ca.key -out ca.crt -subj '/C=IT/ST=Udine/L=Amaro/O=Kapua-cloud/OU=EC/CN=localhost/emailAddress=info@eurotech.com'
openssl genrsa -out server.key 4096 -passin changeit -passout changeit
openssl req -new -out server.csr -key server.key -subj '/C=IT/ST=Udine/L=Amaro/O=Eurotech/OU=EC/CN=localhost/emailAddress=info@eurotech.com'
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 360 -sha256

echo 'Client'
openssl genrsa -out client.key 4096 -passin changeit -passout changeit
openssl req -new -out client.csr -key client.key -subj '/C=IT/ST=Udine/L=Amaro/O=Kapua-client/OU=EC/CN=localhost/emailAddress=info@eurotech.com'
openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 360 -sha256

echo 'Creation of server keystore and truststore'
keytool -import -file ca.crt -alias myCA -keystore server.ts -keypass changeit -storepass changeit
openssl pkcs12 -export -in server.crt -inkey server.key -out serverStore.p12 -CAfile ca.crt -passin pass:changeit -passout pass:changeit
keytool -importkeystore -srckeystore serverStore.p12 -destkeystore server.ks -srcstoretype pkcs12 -srcstorepass changeit -deststorepass changeit

echo 'Creation of client keystore and truststore'
keytool -import -file ca.crt -alias myCA -keystore client.ts -keypass changeit -storepass changeit
openssl pkcs12 -export -in client.crt -inkey client.key -out clientStore.p12 -CAfile ca.crt -passin pass:changeit -passout pass:changeit
keytool -importkeystore -srckeystore clientStore.p12 -destkeystore client.ks -srcstoretype pkcs12 -srcstorepass changeit -deststorepass changeit

