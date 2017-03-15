# JWT Security

To leverage JWT security features, a pair of RSA keys (a public one and a private one) must be loaded in Kapua.

## Use default Kapua keys

A pair of RSA keys is already shipped within Kapua, and can be used **ONLY FOR DEVELOPMENT PURPOSES SINCE THEY ARE PUBLICLY AVAILABLE IN THE KAPUA REPOSITORY**. These keys will be automatically loaded when the system properties `authentication.jwk.private.key` and `authentication.jwk.public.key` are not set, or if the file entered in said properties cannot be read.

## Use custom keys

If you want to use custom keys you can generate an RSA key paid with [OpenSSL](https://www.openssl.org/), and from that generate both the private and the public key. In order to create those files you can use the following commands:

```bash
openssl genrsa -out rsa-2048bit-key-pair.pem 2048
openssl pkcs8 -topk8 -inform pem -in rsa-2048bit-key-pair.pem -outform pem -nocrypt -out private.key
openssl rsa -in rsa-2048bit-key-pair.pem -pubout -out public.key
```

Both keys MUST be in PEM PKCS8 non encrypted format (encrypted privte keys are currently not supported). 

Once created, the path must be specified in `authentication.jwk.private.key` and `authentication.jwk.public.key` system properties, either via -D startup parameter or by manually modifying `service/security/shiro/src/main/resources/kapua-authentication-setting.properties`.