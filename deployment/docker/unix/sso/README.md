 # SSO Keycloak Docker image build 

This set up is already configured to deploy the Single Sign On Keycloak provider.
The `sso-docker-deploy.sh` script deploys Kapua and Keycloak, by performing the following steps:

- builds the Keycloak image;
- generates a self-signed certificate (using openssl), if you need to connect to keycloak via SSL;
- generates a new console image with the certificate installed;
- the docker compose is finally called.

## Running
Just simply run:
```bash
./sso-docker-deploy.sh
```

## Tear down
To stop and remove all containers, simply run:
```bash
./sso-docker-undeploy.sh
```

## Enabling SSL on Kecyloak

In order to enable SSL, modify the `sso-deploy-common.sh` file by providing `https://${EXTERNAL_IP}:9443}` as 
`KEYCLOAK_URL`.

## Accessing Keycloak
After deployment and startup of containers, Keycloak can be accessed at the following endpoint.

| Application/Service | Endpoint       | User  | Password | Notes             |
|---------------------|----------------|-------|----------|-------------------|
| Keycloak Console    | localhost:9090 | admin | admin    |                   |
| Keycloak Console    | localhost:9443 | admin | admin    | if TLS is enabled |

## Using other OpenID SSO providers (other than Keycloak)

If you want to use another SSO OpenID provider, please first modify the `sso-docker-compose.yml` file.
Please ensure to remove Keycloak, add the desired OpenId provider and add the correct environment variables to 
connect the console via the `generic` OpenID provider.

For more details about the SSO deployment, please check the `docs/developer-guide/en/sso.md` SSO developer guide.
