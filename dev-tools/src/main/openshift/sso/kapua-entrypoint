#!/bin/bash

set -e

if [ ! -f /opt/jboss/keycloak/standalone/data/first-run ]; then
  echo "Initializing Kapua setup ..."
  echo "   Kapua Console: $KAPUA_CONSOLE_URL"

  /opt/jboss/docker-entrypoint.sh -b localhost &

  while ! curl -sf http://localhost:8080/auth; do
    echo Waiting for keycloak to come up ...
    sleep 5
  done

  echo Keycloak is up

  KC=/opt/jboss/keycloak/bin/kcadm.sh
  $KC config credentials --server http://localhost:8080/auth --realm master --user "$KEYCLOAK_USER" --password "$KEYCLOAK_PASSWORD"
  
  $KC create realms -s realm=kapua \
    -s enabled=true \
    -s "smtpServer.host=${SMTP_HOST}" \
    ${SMTP_PORT:+-s "smtpServer.port=${SMTP_PORT}"} \
    -s "smtpServer.user=${SMTP_USER}" \
    -s "smtpServer.password=${SMTP_PASSWORD}" \
    -s "smtpServer.from=${SMTP_FROM}" \
    ${SMTP_ENABLE_SSL:+-s "smtpServer.ssl=${SMTP_ENABLE_SSL}"} \
    -f - << EOF
    {
        "displayName": "Eclipse Kapua",
        "registrationAllowed": true,
        "registrationEmailAsUsername": true,
        "rememberMe": true,
        "verifyEmail": true,
        "resetPasswordAllowed": true,
        "smtpServer": {
           "auth": true,
           "starttls": true
        }
     }
EOF

  $KC create clients -r kapua \
    -s "redirectUris=[\"${KAPUA_CONSOLE_URL}/sso/callback\"]" \
    -f - << EOF
    {
        "clientId": "console",
        "name": "Web Console"
    }
EOF

  touch /opt/jboss/keycloak/standalone/data/first-run

  sleep 1

  kill %1
  wait %1

  echo "Keycloak shut down ... commencing normal startup"
fi

exec /opt/jboss/docker-entrypoint.sh $@
exit $?