# Single sign-on (SSO)

This section describes the single sign-on integration of Eclipse Kapua. Our single sign-on solution is based on the
[OpenID Connect](https://openid.net/connect/) identity layer, on top of the [OAuth 2.0](https://oauth.net/2/)
authorization framework. Please note that the OpenID Connect provider is unique for the same Kapua instance, thus it is
common to all the accounts in the instance.

In this document we first describe how to enable SSO on Kapua. In a second step we present two examples based on the
Keycloak Authentication Server, using Docker and OpenShift.

## Enabling single sign-on

In order to enable single sign-on you will need to select an OpenID provider. You can do this using the configuration
property `sso.openid.provider`. Currently, there are two default providers in Kapua. However, additional providers can
be added to Kapua by using the Java service loader framework. The current default providers are:

* `generic` – A generic OpenID Connect provider
* `keycloak` – An OpenID Connect provider based on Keycloak's configuration patterns

Each provider will require additional configuration options. But there is a set of common configuration options:

- **`sso.openid.client.id`** : the "client id" used when communicating with the OpenID Connect server. This represents
  also the JWT audience to search for in the OpenID Connect ID Token
  (for more information see [here](https://openid.net/specs/openid-connect-core-1_0.html#IDToken) )
- **`sso.openid.client.secret` (optional)** : the "client secret" used when communicating with the OpenID Connect
  server.
- **`sso.openid.conf.path` (optional)** : to provide a custom OpenID well-known suffix (the default one
  is `.well-known/openid-configuration` and it's attached as suffix to the issuer).
- **`sso.openid.jwt_processor_timeout` (optional)** : the JwtProcessor expiration time (the default value is 1 hour).

It is also necessary to configure the Web Console external endpoint address.

- **`console.sso.openid.home.uri`** : the URL to the web console, e.g. `http://localhost:8080`

The SSO Login will be available in the form of a dedicated button on the Kapua login page
(the button can be enabled through the configuration option `sso.openid.provider`).

### Generic provider

The generic provider configuration values are retrieved through the `well-known` OpenID configuration document provided
by the OpenID Provider
(see the official [OpenID Connect Discovery specification](https://openid.net/specs/openid-connect-discovery-1_0.html)
for further information). The `issuer` is the only required parameter. However, custom parameters can be added through
the following properties, and are used in case the automatic configuration through the `well-known` document fails. The
required values are specific to your OpenID Connect solution, please use its documentation to look up the required
values:

- **`sso.openid.generic.jwt.issuer.allowed`** : the base URL to the OpenID server provider.
- **`sso.openid.generic.jwt.audience.allowed`** : the JWT audience.
- **`sso.openid.generic.server.endpoint.auth` (optional)** : the endpoint URL to the authentication API.
- **`sso.openid.generic.server.endpoint.logout`(optional)** : the logout endpoint of the OpenID provider.
- **`sso.openid.generic.server.endpoint.token` (optional)** : the endpoint URL to the token API.
- **`sso.openid.generic.server.endpoint.userinfo` (optional)** : the endpoint URL to the userinfo API.

Note that these properties, in combination with the ones defined in the previous paragraph, can be set via environment
variables thanks to the `run-console` bash script included in the Console docker container. Please refer to
the [assembly module README file](assembly/README.md) for detailed information about those properties.

#### Note about 'client id' and 'audience' values

Properties `sso.openid.client.id` and `sso.openid.generic.jwt.audience.allowed` (the second property is used only for
the `generic` provider)
basically represent the same value. More precisely, `sso.openid.client.id` is used as parameter in the requests to the
OpenID Provider, while `sso.openid.generic.jwt.audience.allowed` is used by the `JwtProcessor` in order to validate the
token received from the OpenID Provider. These two should correspond to the same `clientId`.

According to the official OpenID Connect specification (
see [here](https://openid.net/specs/openid-connect-core-1_0.html#IDToken) and
[here](https://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation)), the `audience` contains a list of
allowed `client_id`. The token can contain a list of audiences (described by the `aud` claim). When the token is
received, we need to validate that the audience contains the `client_id` corresponding to the Kapua Console. Only
one `client_id` corresponds to the Kapua Console (if multiple client_ids are present in the audience, they correspond to
other clients).

However, some OpenID Provider implementations are using different values between clientId and audience, thus the only
way to make them work is to use two different values for these properties (also, please note that the `generic-provider`
should be the most 'permissive' provider).

### Keycloak provider

The Keycloak provider can be configured using the following configuration parameters:

- **`sso.openid.keycloak.uri`** : the base URL to the Keycloak server.
- **`sso.openid.keycloak.realm`** : the name of the realm to use.

Note that the _auth_ and _token_ endpoints are automatically computed by the Keycloak provider. For more information
about Keycloak, see the [Keycloak Documentation](http://www.keycloak.org/documentation.html).

Similarly to the 'generic' provider, these properties, in combination with the common properties defined previously, can
be set via environment variables thanks to the `run-console` bash script included in the Console docker container.
Please refer to the [assembly module README file](assembly/README.md) for detailed information about these environment
variables.

### Enabling users to SSO

In order to enable a user to login through an OpenID provider, the user must first be created on the OpenID Connect
server (e.g. using Keycloak, on the Keycloak Admin Console). Secondly, the user can be added to Kapua. Such user differs
from a 'normal' one for its type (which is `EXTERNAL`, while a normal user is `INTERNAL`) and for not having any
credentials (since his credentials are stored in the OpenID Provider).

Currently there are three methods to register an external user in Kapua:
using the _SimpleRegistrationProcessor_ , using _REST API_ or using the _console_.

#### Insert the user through the SimpleRegistrationProcessor module

This module allows one to automatically create the user in Kapua at the first log-in attempt using the SSO. More
precisely, two users are created: an external user without credentials, representing the SSO user, and an internal user
with credentials, representing a gateway user. Both users are placed under a new account with the same name of the SSO
user. If a user with the same name already exists in Kapua, the registration process will fail, and the user is simply
logged in.

**WARNING**: The SimpleRegistrationProcessor is intended to be used only as a _Proof-of-Concept_ and should not be used
in a real environment. For instance, the credentials provided for the gateway user are hardcoded in the
SimpleRegistrationProcessor code. Note however that the SimpleRegistrationProcessor is disabled by default. In order to
enable it, the configuration option **`authentication.registration.service.enabled`** should be provided with
value `true`.

#### Insert the user through REST API

After getting the session token using an authentication REST API, a user can be inserted using  
[userCreate](https://www.eclipse.org/kapua/docs/api/index.html?version=1.0.0#/Users/userCreate). It is mandatory to
provide the following attributes:

- **`scopeId`**: the scope id to which the user will belong in Kapua;
- **`name`**: represents the name in the OpenID Provider;
- **`userType`**: must always be set as **_EXTERNAL_**;
- **`externalId`**: represents the unique ID on the OpenID Provider.

#### Insert the user through the Console

External users can be inserted through the _Users_ module on the Console. Log in with administrator credentials in order
to add a user.

1. Add the new user through the "Add" button.
2. The Add dialog allows to choose between an "Internal user" and an "External user"; choose the latter in order to add
   an external one.
3. Insert the Username and the External Id; all the other fields are optional.

An external user can also be modified through the button "Edit"
(please note that the "Username" and "External Id" fields are not modifiable). Note that the user has no assigned roles.
In order to add a "Role", use the "Assign" button of the "Roles" tab. Note also that the external user has no "
Credentials" at all, since the credentials are established and stored in the external Provider.

Enabling the sso also allows one to see the User Type and, in case it is an external user, the user External Id in the
"Description" tab.

### Logging out

If the SSO is enabled, logging out from Kapua also logs out from the external OpenID provider, invalidating the OpenID
Connect session. This is implemented following the OpenID Connect specification for the
[Relying Party Logout](https://openid.net/specs/openid-connect-session-1_0.html#RPLogout). Note that logging out from
the OpenID provider is also possible through the provider OpenID logout endpoint, but the user will remain logged into
Kapua until also the logout from Kapua is performed.

The OpenID Connect logout can be disabled by setting the `console.sso.openid.user.logout.enabled`
and `console.sso.openid.session_listener.logout.enabled`
properties to `false` (these properties are always set to `true` by default). The first property allows disabling the
user-initiated logout, while the second one allows disabling the `HttpSessionListener` managed logout (see
class `OpenIDLogoutListener`), which is triggered at session invalidation. Be careful if you choose to disable the
OpenID logout, since this will allow the user to login again into the Kapua Console without the need to provide any
credentials.

## Keycloak Example (Docker based)

We detail here the steps to run an OpenID Keycloak provider. The example described here makes use of a Keycloak Server
Docker image
(see [here](https://hub.docker.com/r/jboss/keycloak/) for more details).

### Installing the Keycloak Server (Docker image)

In order to deploy automatically the Keycloak image, is sufficient to add the `--sso` option to the `docker-deploy.sh`
script inside the directory of the docker deployment scripts. In such a way the environment is ready to be used without
the need for further configuration.

However, if you want to use a stand alone Keycloak image, please follow the instruction below in order to configure it.

#### Manual installation of the Keycloak server.

In order to download and install the image, run `docker pull jboss/keycloak` on a bash terminal. Then,
run `docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 9090:8080 jboss/keycloak` to start the docker
container, setting up the "_admin_" user (with "_admin_" as password). The Keycloak Server Admin Console will be
available at the following URL: _http://<Keycloak-IP-address>:9090/_.

#### SSL configuration

_Following section needs to be updated_

The Keycloak provider can be configured to use SSL, which is enabled by setting the 9443 port for the `KEYCLOAK_URL`
in the `docker-common-sso.sh` file. A self-signed certificate and a key are produced through `sso-docker-deploy.sh`
script and passed via the volume based on the `./certs:/etc/x509/https` directory. The script also installs the
certificate in the Kapua Console docker image (which is tagged with the 'sso' tag).

**WARNING**: This SSL configuration is intended to be used only for testing purposes and should not be used in a
production environment. If you want to use Keycloak in a production environment and provide your own TLS certificate,
please refer to the official
[Keycloak documentation](https://www.keycloak.org/docs/latest/server_installation/#_setting_up_ssl).

### Manually configuring the Keycloak Server

The Keycloak instance provided with the docker deployment is already configured with a dedicated
"Kapua" realm and a client when using the script `docker-deploy.sh` with `--sso` option.

However, if you already have a running Keycloak instance, you can follow the instructions below in order to configure it
manually. Open the Keycloak Admin Console on your preferred browser and follow the steps below in order to configure it.

1. Create a new realm on Keycloak, call it "_kapua_"
1. Create a new client for this realm, call it "_console_" (this name represents the "Client ID").
2. Configure the client "Settings" tab as follows:
    - Client Protocol : "_openid-connect_"
    - Access : "_public_"
    - Standard Flow Enabled : _ON_
    - Direct Access Grants Enabled : _ON_
    - Valid Redirect URIs : _http://localhost:8080/*_  (use your IP address in place of localhost)
    - Base URL : _http://localhost:8080/_
3. Under the "Mappers" tab, create a new mapper called "console" with the following parameters:
    - Name : "_console_"
    - Mapper Type : "_Audience_"
    - Included Custom Audience : "_console_"
    - Add to access token : _ON_
4. On the "Realm Settings", under the "Tokens" tab, set "Access Token Lifespan" to 10 minutes (the default time is too
   short)

### Configuring Kapua to use SSO with the Keycloak Server

The Kapua console docker image is already configured and deployed in docker without further configuration using the
script `docker-deploy.sh` with `--sso` option.

If you need to configure it manually, the following properties must be passed (as VM options) in order to set up the SSO
on Kapua using Keycloak (you can login using the default `admin` user with `admin` password):

- `sso.openid.provider=keycloak` : to set Keycloak as OpenID provider
- `sso.openid.keycloak.realm=kapua` : the Keycloak Realm (we are using the "kapua" realm)
- `sso.openid.keycloak.uri=http://<Keycloak-IP-address>:9090` : the Keycloak Server URI
  (use `https://<Keycloak-IP-address>:9443` in case TLS is enabled - see below for further details)
- `sso.openid.client.id=console` : the OpenID Client ID (the one set on Keycloak)
- `console.sso.openid.home.uri=http://localhost:8080` : the Kapua web console URI

If you need to start the console docker container alone, it is sufficient to provide the following docker environment
variables (these will automatically set up the configuration properties described above):

- `KEYCLOAK_URL=http://<Keycloak-IP-address>:9090` : the Keycloak Server URI
  (use `https://<Keycloak-IP-address>:9443` in case TLS is enabled - see below for further details)
- `KAPUA_CONSOLE_URL=http://localhost:8080` : the Kapua web console URI

When using `docker-compose`, these two variables are bound through the `docker-compose.yaml` file. Note that even if the
Keycloak server is running locally on a docker container, it is recommended to use your machine IP address instead of '
localhost', since this one can be misinterpreted by docker as the 'localhost' of the container in which the Kapua
component or Keycloak are running (this is automatically done through the `sso-docker-deploy.sh`
script).

Please refer to the [assembly module README file](assembly/README.md) for detailed information about the Console docker
container and related environment variables.

### Setting Up a user on the Keycloak server

A test user is already created inside the Keycloak server, with username `sso-user` and password `sso-password`. The ID
assigned by Keycloak must be used as External ID on the Kapua side (see the next section).

If you want to add a new user, please follow the instructions below (remember to use the `admin` user with `admin`
password to log in):

1. From the "Users" tab on the left menu, click on "Add user"
2. Configure the user as follows:
    - Username : e.g. "_alice_"
    - Email : e.g. "_alice@heremailprovider.com_"
    - User Enabled : _ON_
3. Configure the user credentials under the "Credentials" tab

Note that the user must have an email set in the OpenID Provider server, otherwise the creation on Kapua through the
SimpleRegistrationProcessor will fail. It is also possible to use the "_admin_" or the "_sso-user_" the users to log in
(remind to add an email address).

### Setting Up a user on Kapua

To add a new user in Kapua, it is sufficient to add it through the console as described in the
[Insert the user through the Console](#insert-the-user-through-the-console) section. If you want to use the
SimpleRegistrationProcessor or the REST API, please follow the examples below.

Using the SimpleRegistrationProcessor, the user "_alice_" in Keycloak will generate "_alice_"
and "_alice-broker_" in Kapua, in a dedicated "_alice_" account.

Using the userCreate REST API with the following body (using the _scopeId_ of the desired account and the ID of the
user "_admin_" in Keycloak as _externalId_):

``` 
{
  "scopeId": "...",
  "name": "admin",
  "userType": "EXTERNAL",
  "externalId" : "5726876c-...."
}
```

will create the "_admin_" user without the need of the SimpleRegistrationProcessor.

### Keycloak logout endpoint

Logging out from the Keycloak provider is possible through the Keycloak OpenID Connect logout endpoint:

`{sso.openid.keycloak.uri}/auth/realms/{realm_name}/protocol/openid-connect/logout`

In our example the endpoint is the following:

`http://<Keycloak-IP-address>:9090/auth/realms/kapua/protocol/openid-connect/logout`

## Keycloak Example (OpenShift based)

This project provides a template to bootstrap single sign-on based on [Keycloak](http://keycloak.org). The scripts for
this are located in the directory `kapua/deployment/openshift/sso`
(please refer to `kapua/deployment/minishift/sso` if you are using Minishift).

Assuming you have already installed Kapua into OpenShift, it is possible to run the script `deploy`, which will create a
new build and deployment configuration in OpenShift. This is based on the official Keycloak Docker
image `jboss/keycloak`, adding a few steps for initial provisioning.

{% hint style='danger' %} The default setup uses an ephemeral storage. So re-starting the Keycloak pod will delete the
configuration unless you re-configure the setup with a persistent volume. {% endhint %}

After the build and deployment configuration has been created, the script will also re-configure the Kapua OpenShift
project to use the newly created Keycloak instance. This is done by calling the script `activate`. The `activate` script
can be called at a later time to re-configure Kapua (e.g. when re-installing Kapua).

Both scripts (`deploy` and `activate`) require both Kapua and Keycloak URLs. Keycloak requires the Kapua web console URL
in order to allow requests from this source, while Kapua requires the Keycloak URL in order to forward requests to
Keycloak. The URLs are being constructed from OpenShift routes, which are configured for both Kapua and Keycloak.
However this requires that Kapua is set up before Keycloak and that the `activate` script can only be called after
the `deploy` script has been successfully run.

Please refer to the [Keycloak Example (Docker based)](#keycloak-example-docker-based) section for the user creation, or
follow the next section in order to perform email-based user registration.

### Email-server based user registration

For this configuration to work, you will need some existing SMTP server which is capable of sending e-mails. This is
required so that Keycloak can send user verification and password recovery e-mails. If you don't have and local SMTP
server it is also possible to use some cloud based service like Mailgun, SendGrid or any other provider.

The deployment is triggered by running the `deploy` script with a set of environment variables. Assuming you are
using `bash` as shell, this can be done like this:

    SMTP_HOST=smtp.server.org SMTP_USER=user SMTP_PASSWORD=secret SMTP_FROM=sender@my.domain ./deploy

The following environment variables are being used:

- **`SMTP_HOST` (required)** : The host name or IP address of the SMTP server.
- **`SMTP_PORT` (optional)** : The port number of the SMTP service.
- **`SMTP_FROM` (required)** : The sender e-mail used in the e-mail.
- **`SMTP_USER` (required)** : The user name used to authenticate with the SMTP server.
- **`SMTP_PASSWORD` (required)** : The password used to authenticate with the SMTP server.
- **`SMTP_ENABLE_SSL` (optional)** : If SSL should be used instead of STARTTLS.
- **`KEYCLOAK_ADMIN_PASSWORD` (optional)** : The password which will be assigned to the Keycloak admin user. The default
  is to generate a password.
