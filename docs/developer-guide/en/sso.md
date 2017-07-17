# Single sign-on (SSO)

This section describes the single sign-on integration of Eclipse Kapua.

## OpenShift

This project provides a template to bootstrap single sign-on based on [Keycloak](http://keycloak.org).
The scripts for this are located in the director `kapua.git/dev-tools/src/main/openshift/sso`.

Assuming you have already installed Kapua into OpenShift, it is possible to run the script `deploy`, which
will create a new build and deployment configuration in OpenShift. This is based on the official Keycloak Docker
image `jboss/keycloak`, adding a few steps for initial provisioning.

{% hint style='danger' %}
The default setup uses an ephemeral storage. So re-starting the Keycloak pod will delete the configuration unless
you re-configure the setup with a persistent volume.
{% endhint %} 

For this configuration to work, you will need some existing SMTP server which is capable of sending e-mails.
This is required so that Keycloak can send user verification and password recovery e-mails. If you don't have
and local SMTP server it is also possible to use some cloud based service like Mailgun, SendGrid or any other
provider.

The deployment is triggered by running the `deploy` script with a set of environment variables. Assuming your
are using `bash` as shell, this can be done like this:

    SMTP_HOST=smtp.server.org SMTP_USER=user SMTP_PASSWORD=secret SMTP_FROM=sender@my.domain ./deploy

The following environment variables are being used:

<dl>

<dt>SMTP_HOST (required)</dt>
<dd>The host name or IP address of the SMTP server</dd>

<dt>SMTP_PORT (optional)</dt>
<dd>The port number of the SMTP service</dd>

<dt>SMTP_FROM (required)</dt>
<dd>The sender e-mail used in the e-mail</dd>

<dt>SMTP_USER (required)</dt>
<dd>The user name used to authenticate with the SMTP server</dd>

<dt>SMTP_PASSWORD (required)</dt>
<dd>The password used to authenticate with the SMTP server</dd>

<dt>SMTP_ENABLE_SSL (optional)</dt>
<dd>If SSL should be used instead of STARTTLS</dd>

<dt>KEYCLOAK_ADMIN_PASSWORD (optional)</dt>
<dd>The password which will be assigned to the Keycloak admin user. The default is to generate a password.</dd>

</dl>

After the build and deployment configuration was creates the script will also re-configure the Kapua OpenShift project
to use the newly created Keycloak instance. This is done by calling the script `activate`. The `activate` script
can be called at a later time to re-configure Kapua (e.g. when re-installing Kapua).

Both scripts (`deploy` and `activate`) require both Kapua and Keycloak URLs. Keycloak requires the Kapua web console
URL in order to allow request from this source, while Kapua requires the Keycloak URL in order to forward requests to Keyloak.

The URLs are being constructed from OpenShift routes, which are configured for both Kapua and Keycloak. However this requires
that Kapua is set up before Keycloak and that the `activate` script can only be called after the `deploy` script
has been successfully run.

## Enabling single sign-on

In order to enable single sign-on you will need to select an SSO provider. You can do this using the
configuration option `sso.provider`. Currently there are two default providers in Kapua. However additional
providers an be added to Kapua by using the Java service loader framework.

The current default providers are:

* `generic` – A generic OpenID Connect provider
* `keycloak` – An OpenID Connect provider based on Keycloak's configuration patterns

Each provider will require additional configuration options. But there is a set of common configuration
options:

<dl>
  <dt><code>sso.openid.client.id</code></dt>
  <dd>
  The "client id" used when communicating with the OpenID Connect server.
  </dd>

  <dt><code>sso.openid.client.secret</code> (optional)</dt>
  <dd>
  The "client secret" used when communicating with the OpenID Connect server.
  </dd>
</dl>

It is also necessary to configure the Web Console what its external endpoint address is.
Currently this is a required configuration, even if there is no difference between the servers
endpoint URL and its external URL, even if this may just be `http://localhost:8080`.

<dl>
  <dt><code>site.home.uri</code></dt>
  <dd>The URL to the web console, e.g. <code>http://localhost:8080</code></dd>
</dl>

### Generic provider

The follow values are specific to your OpenID Connection solution, please use its
documentation to look up the required values:

<dl>
  <dt><code>sso.generic.openid.server.endpoint.auth</code></dt>
  <dd>The endpoint URL to the authentication API</dd>
  <dt><code>sso.generic.openid.server.endpoint.token</code></dt>
  <dd>The endpoint URL to the token API</dd>
</dl>

### Keycloak provider

The keycloak provider can be configured using the following configuration parameters:

<dl>
  <dt><code>sso.keycloak.uri</code></dt>
  <dd>The base URL to the Keycloak server</dd>
  
  <dt><code>sso.keycloak.realm</code></dt>
  <dd>The name of they realm to use</dd>
</dl>

For more information see the [Keycloak Documentation](http://www.keycloak.org/documentation.html).
