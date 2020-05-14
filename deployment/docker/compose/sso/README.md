# SSO docker compose

The `sso-docker-compose.yml` file is used only for SSO testing, and its already provided with a Keycloak service.
This file is meant to be called by the scripts placed in the `deployment/docker/unix/sso` directory.
The Keycloak service is built using scripts in the `deployment/commons/sso/keycloak` directory.

In order to use other SSO services, replace the Keycloak service with the desired one.
