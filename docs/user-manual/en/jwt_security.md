# JWT Security

To leverage JWT security features, a pair of RSA keys (a public one and a private one) must be loaded in Kapua.

## Use random generated keys

By default Kapua will look for keys in the path specified by `authentication.session.jwt.private.key` and `authentication.session.jwt.public.key` system properties at startup (see below). If such properties are not set, or if Kapua can't find those files, it will automatically generate new random private and public keys.

## Use custom keys

If you want to use custom keys you can generate an RSA key pair with [OpenSSL](https://www.openssl.org/), and from that generate both the private and the public key. In order to create those files you can use the following commands:

```bash
openssl genrsa -out rsa-2048bit-key-pair.pem 2048
openssl pkcs8 -topk8 -inform pem -in rsa-2048bit-key-pair.pem -outform pem -nocrypt -out private.key
openssl rsa -in rsa-2048bit-key-pair.pem -pubout -out public.key
```

Both keys MUST be in PEM PKCS8 non encrypted format (encrypted privte keys are currently not supported). 

Once created, the path must be specified in `authentication.session.jwt.private.key` and `authentication.session.jwt.public.key` system properties, either via -D startup parameter or by manually modifying `service/security/shiro/src/main/resources/kapua-authentication-setting.properties`.