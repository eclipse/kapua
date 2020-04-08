# Keycloak Docker image build 

This directory contains script to build a new Docker image on top of the official one, provided with a dedicated 'Kapua'
realm and a dedicated client. Launch the `build` shell script in order to build the Keycloak docker image.

## About the Keycloak version

**WARNING**: 
Using Keycloak 7.0.0, since version 7.0.1 does not seem to work properly on OpenShift, 
even if it has been tested successfully with Docker compose (thus without Openshift) also with version 9.0.0.
As for Docker, tests with version 9.0.2 were not successful, since the OpenID Connect logout is not working correctly,
and redirects to a Keycloak page stating “Session not active”, without performing actual logout.
For more information see [here](https://keycloak.discourse.group/t/openid-connect-logout-endpoint-results-in-session-not-active-in-keycloak-9-0-2/)
