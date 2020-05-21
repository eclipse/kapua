# Setup SSO (OpenID Connect) in Minishift for Kapua with Keycloak

This directory contains a setup for enabling single-sign-on (OpenID Connect) with Kapua
using [Keycloak](http://www.keycloak.org/).

In a nutshell you will need to install Kapua into Minishift and then run the `deploy`script.
The Keycloak image is built through the `deploy` script.
`A set of environment variables can be used to provide SMTP settings in order to add new Keycloak users by 
sending out registration e-mails.

For more information see the [developer manual](https://download.eclipse.org/kapua/docs/develop/developer-guide/en/sso.html).

### Accessing Keycloak component

After deployment and startup of containers, the Keycloak container can be accessed at the following endpoint

| Application/Service | Endpoint                           | User  | Password                       | 
|---------------------|------------------------------------|-------|--------------------------------|
| Keycloak Console    | web-sso.<minishift-default-domain> | admin | <user-provided> (or generated) | 

To provide a custom password, please define the `KEYCLOAK_ADMIN_PASSWORD` variable before running the `deploy` script.
