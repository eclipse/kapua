# JWT Security

To leverage JWT security features, an X509 Certificate with the related private key must be loaded in Kapua.

## Use random generated certificate and private key

By default Kapua will look for keys in the path specified by `certificate.jwt.private.key` and `certificate.jwt.certificate` system properties at startup (see below). Such properties MUST be set, otherwise an error will be thrown.

In both the Vagrant develop machine and the Docker deployment, a certificate and its private key are dynamically generated in the Vagrant box / Docker image build. They are then automatically loaded at startup by the environment. 

## Use a custom certificate

If you want to use a custom certificate you can generate it, along with its private key, with [OpenSSL](https://www.openssl.org/). In order to create those files you can use the following commands:

```bash
openssl req -x509 -newkey rsa:4096 -keyout <path_to_key> -out <path_to_certificate> -days 365 -nodes -subj '/O=Eclipse Kapua/C=XX'
openssl pkcs8 -topk8 -in <path_to_key> -out <path_to_pkcs8_key> -nocrypt
rm <path_to_key>
```

The private key must be in PKCS8 non encrypted format (encrypted private keys are currently not supported). 
