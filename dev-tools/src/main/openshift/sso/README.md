# Setup SSO for Kapua with Keycloak

This directory contains a setup for enabling single-sign-on (SSO) with Kapua
using [Keycloak](http://www.keycloak.org/).

In a nutshell you will need to install Kapua into OpenShift and then run the `deploy`
script with a set of environment variables which provide SMTP settings for sending out
user registration e-mails.

For more information see the [developer manual](https://download.eclipse.org/kapua/docs/develop/developer-guide/en/sso.html#openshift).
