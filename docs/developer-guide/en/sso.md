# Single sign-on (SSO)

This section describes the single sign-on integration of Eclipse Kapua.
Our single sign-on solution is based on the [OpenID Connect](https://openid.net/connect/) identity layer, 
on top of the [OAuth 2.0](https://oauth.net/2/) authorization framework. 
In this document we first describe how to enable SSO on Kapua. 
In a second step, we present two examples based on the Keycloak Authentication Server, using Docker and OpenShift.

**WARNING**: The current SSO implementation is intended as a _Proof-of-Concept_ and should not be used in a production 
environment.

## Enabling single sign-on

In order to enable single sign-on you will need to select an SSO provider. You can do this using the
configuration option `sso.provider`. Currently there are two default providers in Kapua. However additional
providers can be added to Kapua by using the Java service loader framework. 
The current default providers are:

* `generic` – A generic OpenID Connect provider
* `keycloak` – An OpenID Connect provider based on Keycloak's configuration patterns

Each provider will require additional configuration options. But there is a set of common configuration
options:

- **`sso.openid.client.id`** : 
    the "client id" used when communicating with the OpenID Connect server.
- **`sso.openid.client.secret` (optional)** : 
    the "client secret" used when communicating with the OpenID Connect server.
- **`sso.openid.jwt-processor-timeout` (optional)** : the JwtProcessor expiration time (the default value is 1 hour).

It is also necessary to configure the Web Console what its external endpoint address is.
Currently this is a required configuration, even if there is no difference between the servers
endpoint URL and its external URL, even if this may just be `http://localhost:8080`.

- **`site.home.uri`** : the URL to the web console, e.g. `http://localhost:8080`

The SSO Login will be available in the form of a dedicated button on the Kapua login page 
(the button can be enabled through the configuration option `sso.provider`).

### Generic provider

The follow values are specific to your OpenID Connection solution, please use its
documentation to look up the required values:

- **`sso.generic.openid.server.endpoint.auth`** : the endpoint URL to the authentication API.
- **`sso.generic.openid.server.endpoint.token`** : the endpoint URL to the token API.
- **`sso.generic.openid.jwt.audience.allowed`** : the JWT audience.
- **`sso.generic.openid.jwt.issuer.allowed`** : the base URL to the OpenID server provider.

### Keycloak provider

The Keycloak provider can be configured using the following configuration parameters:

- **`sso.keycloak.uri`** : the base URL to the Keycloak server.
- **`sso.keycloak.realm`** : the name of they realm to use.

Note that the _auth_ and _token_ endpoints are automatically computed by the Keycloak provider.

For more information see the [Keycloak Documentation](http://www.keycloak.org/documentation.html).

### Enabling users to SSO

In order to enable a user to login through an SSO provider, the user must first be created on the OpenID 
Connect server (e.g. using Keycloak, on the Keycloak Admin Console). 
Secondly, the user can be added to Kapua. 
Such user differs from a 'normal' one for its type (which is `EXTERNAL`, while a normal user is `INTERNAL`) and for 
not having any credentials (since his credentials are stored in the OpenID Provider).

Currently there are three methods to register an external user in Kapua: 
using the _SimpleRegistrationProcessor_ , using _REST API_ or using the _console_.

#### Insert the user through the SimpleRegistrationProcessor module

This module allows one to automatically create the user in Kapua at the first log-in attempt using the SSO.
More precisely, two users are created: an external user without credentials, representing the SSO user, and an internal 
user with credentials, representing a gateway user. 
Both users are placed under a new account with the same name of the SSO user.
If a user with the same name already exists in Kapua, the registration process will fail, 
and the user is simply logged in.

**WARNING**: The SimpleRegistrationProcessor is intended to be used only as a _Proof-of-Concept_ and should not be 
used in a real environment.
For instance, the credentials provided for the gateway user are hardcoded in the SimpleRegistrationProcessor code.
Note however that the SimpleRegistrationProcessor is disabled by default.
In order to enable it, the configuration option **`authentication.registration.service.enabled`** should be provided 
with value `true`.

#### Insert the user through REST API

After getting the session token using an authentication REST API, a user can be inserted using  
[userCreate](https://www.eclipse.org/kapua/docs/api/index.html?version=1.0.0#/Users/userCreate).
It is mandatory to provide teh following attributes: 
- **`scopeId`**: the scope id to which the user will belong in Kapua;
- **`name`**: represents the name in the OpenID Provider;
- **`userType`**: must always be set as **_EXTERNAL_**;
- **`externalId`**: represents the unique ID on the OpenID Provider.

#### Insert the user through the Console

External users can be inserted through the _Users_ module on the Console. 
Log in with administrator credentials in order to add a user.

1. Add the new user through the "Add" button.
2. The Add dialog allows to choose between an "Internal user" and an "External user"; 
choose the latter in order to add an external one.
3. Insert the Username and the External Id; all the other fields are optional.

An external user can also be modified through the button "Edit" 
(please note that the "Username" and "External Id" fields are not modifiable).
Note that the user has no assigned roles. In order to add a "Role", use the "Assign" button of the "Roles" tab.
Note also that the external user has no "Credentials" at all, since the credentials are established and stored in 
the external Provider.

Enabling the sso also allows one to see the User Type and, in case it is an external user, the user External Id in the 
"Description" tab.

### Logging out

If the SSO is enabled, logging out from Kapua also logs out from the external OpenID provider, invalidating the OpenID 
Connect session. 
This is implemented following the OpenID Connect specification for the 
[Relying Party Logout](https://openid.net/specs/openid-connect-session-1_0.html#RPLogout).
Note that logging out from the OpenID provider is also possible through the provider OpenID logout endpoint, 
but the user will remain logged into Kapua until also the logout from Kapua is performed.

## Keycloak Example (Docker based)

We detail here the steps to run an SSO Keycloak provider.
The example described here makes use of a Keycloak Serve Docker image 
(see [here](https://hub.docker.com/r/jboss/keycloak/) for more details). 

### Installing the Keycloak Server

In order to download and install the image, run `docker pull jboss/keycloak` on a bash terminal. 
Then, run `docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 9090:8080 jboss/keycloak` to start the 
docker container, setting up the "_admin_" user (with "_admin_" as password).
The Keycloak Server Admin Console will be available at the following URL: _http://<Keycloak-IP-address>:9090/_.

### Configuring the Keycloak Server

Open the Keycloak Admin Console on your preferred browser and follow the steps below in order to configure it.

1. Create a new realm on Keycloak, call it "_kapua_"
1. Create a new client for this realm, call it "_console_" (this name represents the "Client ID").
2. Configure the client "Settings" tab as follows: 
    - Client Protocol : "_openid-connect_"
    - Access : "_public_"
    - Standard Flow Enabled : _ON_
    - Direct Access Grants Enabled : _ON_
    - Valid Redirect URIs : _http://localhost:8080/*_  (user your IP address in place of localhost)
    - Base URL : _http://localhost:8080/_
3. Under the "Mappers" tab, create a new mapper called "console" with the following parameters:
    - Name : "_console_"
    - Mapper Type : "_Audience_"
    - Included Custom Audience : "_console_"
    - Add to access token : _ON_
4. On the "Scope" tab of the "_console_" client, check that "Full Scope Allowed" is set to _ON_
5. On the "Client Scopes" menu (see menu on the left) create a new Client Scope, called "_console_"
6. On the "Roles" menu (on the left) add a new role "_console_"
7. On the "Realm Settings", under the "Tokens" tab, set "Access Token Lifespan" to 10 minutes (the default time is too 
short)

### Configuring Kapua to use SSO with the Keycloak Server

The following properties must be passed (as VM options) in order to set up SSO on Kapua using Keycloak:

- `sso.provider=keycloak` : to set Keycloak as sso provider
- `sso.keycloak.realm=kapua` : the Keycloak Realm (we are using the "kapua" realm)
- `sso.keycloak.uri=http://<Keycloak-IP-address>:9090` : the Keycloak Server URI 
- `sso.openid.client.id=console` : the OpenID Client ID (the one set on Keycloak)
- `site.home.uri=http://localhost:8080` : the Kapua web console URI 

Using docker it is sufficient to provide the following docker environment variables (these ones will automatically set 
up the configuration properties described above):

- `KEYCLOAK_URL=http://<Keycloak-IP-address>:9090` : the Keycloak Server URI
- `KAPUA_URL=http://localhost:8080` : the Kapua web console URI

Note that these two variables can be also set up in the `docker-compose.yaml` file.
Moreover, even if the Keycloak server is running locally on a docker container, it is recommended to use your machine 
IP address instead of 'localhost', since this one can be misinterpreted by docker as the 'localhost' of the container 
in which the Kapua component or Keycloak are running. 

### Setting Up a user on the Keycloak server

1. From the "Users" tab on the left menu, click on "Add user"
2. Configure the user as follows:
    - Username : e.g. "_alice_"
    - Email : e.g. "_alice@heremailprovider.com_"
    - User Enabled : _ON_
3. Configure the user credentials under the "Credentials" tab

The ID assigned by Keycloak will be used as External ID on the Kapua side.
Note that the user must have an email set in the OpenID Provider server, otherwise the creation on Kapua through the 
SimpleRegistrationProcessor will fail. It is also possible to use the "_admin_" user to log in 
(remind to add an email address). 

### Setting Up a user on Kapua

Using the SimpleRegistrationProcessor, the user "_alice_" in Keycloak will generate "_alice_" 
and "_alice-broker_" in Kapua, in a dedicated "_alice_" account.

Using the userCreate REST API with the following body (using the _scopeId_ of the desired account 
and the ID of the user "_admin_" in Keycloak as _externalId_):
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

Logging out from the Keycloak provider is also possible through the Keycloak logout endpoint: 

`{sso.keycloak.uri}/auth/realms/{realm_name}/protocol/openid-connect/logout`

In our example the endpoint is the following: 

`http://<Keycloak-IP-address>:9090/auth/realms/kapua/protocol/openid-connect/logout`

## Keycloak Example (OpenShift based)

This project provides a template to bootstrap single sign-on based on [Keycloak](http://keycloak.org).
The scripts for this are located in the directory `kapua/deployment/openshift/sso` 
(please refer to `kapua/deployment/minishift/sso` if you are using Minishift).

Assuming you have already installed Kapua into OpenShift, it is possible to run the script `deploy`, which
will create a new build and deployment configuration in OpenShift. This is based on the official Keycloak Docker
image `jboss/keycloak`, adding a few steps for initial provisioning.

{% hint style='danger' %}
The default setup uses an ephemeral storage. So re-starting the Keycloak pod will delete the configuration unless
you re-configure the setup with a persistent volume.
{% endhint %} 

After the build and deployment configuration has been created, the script will also re-configure the Kapua OpenShift 
project to use the newly created Keycloak instance. This is done by calling the script `activate`. 
The `activate` script can be called at a later time to re-configure Kapua (e.g. when re-installing Kapua).

Both scripts (`deploy` and `activate`) require both Kapua and Keycloak URLs. 
Keycloak requires the Kapua web console URL in order to allow request from this source, 
while Kapua requires the Keycloak URL in order to forward requests to Keyloak.
The URLs are being constructed from OpenShift routes, which are configured for both Kapua and Keycloak. 
However this requires that Kapua is set up before Keycloak and that the `activate` script can only be called after 
the `deploy` script has been successfully run.

Please refer to the [Keycloak Example (Docker based)](#keycloak-example-docker-based) section for the user creation, 
or follow the next section in order to perfrom email-based user registration.

### Email-server based user registration

For this configuration to work, you will need some existing SMTP server which is capable of sending e-mails.
This is required so that Keycloak can send user verification and password recovery e-mails. If you don't have
and local SMTP server it is also possible to use some cloud based service like Mailgun, SendGrid or any other
provider.

The deployment is triggered by running the `deploy` script with a set of environment variables. Assuming your
are using `bash` as shell, this can be done like this:

    SMTP_HOST=smtp.server.org SMTP_USER=user SMTP_PASSWORD=secret SMTP_FROM=sender@my.domain ./deploy

The following environment variables are being used:

- **`SMTP_HOST` (required)** : The host name or IP address of the SMTP server.
- **`SMTP_PORT` (optional)** : The port number of the SMTP service.
- **`SMTP_FROM` (required)** : The sender e-mail used in the e-mail.
- **`SMTP_USER` (required)** : The user name used to authenticate with the SMTP server.
- **`SMTP_PASSWORD` (required)** : The password used to authenticate with the SMTP server.
- **`SMTP_ENABLE_SSL` (optional)** : If SSL should be used instead of STARTTLS.
- **`KEYCLOAK_ADMIN_PASSWORD` (optional)** : The password which will be assigned to the Keycloak admin user. 
The default is to generate a password.
