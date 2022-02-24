# Keycloack SSO Provider

### Keycloak Docker image build 

This directory contains scripts to build a new Docker image on top of the official one, provided with a dedicated 'Kapua'
realm and a dedicated client. 

Launch the `build` shell script in order to build the Keycloak docker image:

```bash
./build
```

### About the Keycloak version

**WARNING**: 
The Dockerfile is using Keycloak 7.0.0, since version 7.0.1 does not seem to work properly on OpenShift. 
However, if Keycloak is expected to run only in Docker, also newer versions can be used (e.g. 10.0.1).
