## Docker

The section describes how Eclipse Kapua docker images can be used.

### Build

To learn how to build Kapua Docker images, please consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/building.md#docker-images).

### Run

To learn how to run Kapua in Docker, please consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#docker-containers).

### Access

Navigate your browser to http://localhost:8080 and log in using the following credentials:
`kapua-sys` : `kapua-password`

You can access the API using: http://localhost:8081

**Note**: If you are using Docker on Windows the hostname will most likely not be `localhost` but
the IP address of your docker instance.

### SSO (OpenID Connect) testing

The following paragraphs describe how to set up an SSO OpenID Connect Provider in Kapua via environment variables.
For further information, please see the [SSO Developer Guide](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/sso.md).

#### Keycloak Provider

[Here](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/sso.md#keycloak-example-docker-based) you can find a detailed guide for how to run a OpenID keycloak provider. In particular, it is possible to
test the sso with a pre-defined Keycloak image following [this](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/sso.md#installing-the-keycloak-server-docker-image) (An instance already configured with a dedicated realm and client)
or, by manually providing a stand-alone Keycloak image, following [that](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/sso.md#manual-installation-of-the-keycloak-server) instructions .

#### Generic Provider

It is also possible to use a generic OpenID Connect provider, by providing to the console the following environment 
variables:

- `KAPUA_CONSOLE_URL` : the `kapua-console` URL;
- `KAPUA_OPENID_JWT_ISSUER` : the base URL to the OpenID Connect server provider;
- `KAPUA_OPENID_JWT_AUDIENCE` : the JWT audience (the default value is `console`);
- `KAPUA_OPENID_CLIENT_ID` : the client id (the default value is `console`);
- `KAPUA_OPENID_CLIENT_SECRET` : the client secret (optional);
- `KAPUA_OPENID_AUTH_ENDPOINT` : the endpoint URL to the authentication API (optional, already retrieved via well-known document);
- `KAPUA_OPENID_TOKEN_ENDPOINT` : the endpoint URL to the token API (optional, already retrieved via well-known document);
- `KAPUA_OPENID_LOGOUT_ENDPOINT` : the URL to the logout endpoint (optional, already retrieved via well-known document).

Note that `OPENID_CLIENT_ID` and `JWT_AUDIENCE` are usually mapped with the same value,
(see the [SSO Developer Guide generic provider section](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/sso.md#generic-provider) for further information).

### Tomcat images

It is also possible to use Tomcat as a web container. For this use the following run commands instead:

    docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es -p 8080:8080 kapua/kapua-console
    docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es -p 8081:8080 kapua/kapua-api

Please note that in this case you also have to append `/admin` and `/api` to the URL.

