#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

set -e

OC=oc

: KAPUA_PROJECT_NAME=${KAPUA_PROJECT_NAME:=eclipse-kapua}
: KEYCLOAK_PROJECT_NAME=${KEYCLOAK_PROJECT_NAME:=sso}

# Functions

function die() { printf "$@" "\n" 1>&2 ; exit 1; }

function route_scheme () {
	local project="$1"
	local route="$2"
	
	local tls=$($OC -n "$project" get "route/$route" -ojsonpath={.spec.tls})
	test "$tls" = "nil" && echo "http" || echo "https"
}

function route_host () {
	local project="$1"
	local route="$2"
	
	local host=$($OC -n "$project" get "route/$route" -ojsonpath={.spec.host})
	echo "$host"
}

function route_url () {
	local project="$1"
	local route="$2"
	
	echo "$(route_scheme "$project" "$route")://$(route_host "$project" "$route")"
}

# Check for mail server configuration

test -n "$SMTP_HOST" || die "'SMTP_HOST' not set"
test -n "$SMTP_USER" || die "'SMTP_USER' not set"
test -n "$SMTP_PASSWORD" || die "'SMTP_PASSWORD' not set"
test -n "$SMTP_FROM" || die "'SMTP_FROM' not set"

# Get Kapua URL

KAPUA_URL="$(route_url "$KAPUA_PROJECT_NAME" console)"
echo KAPUA_URL    = $KAPUA_URL

# Create

$OC process -n "$KEYCLOAK_PROJECT_NAME" -f keycloak-template.yml \
    "GIT_REPO=${GIT_REPO:-https://github.com/eclipse/kapua}" \
    "GIT_REF=${GIT_REF:-develop}" \
    "KAPUA_CONSOLE_URL=$KAPUA_URL" \
    "SMTP_HOST=$SMTP_HOST" \
    "SMTP_PORT=$SMTP_PORT" \
    "SMTP_USER=$SMTP_USER" \
    "SMTP_PASSWORD=$SMTP_PASSWORD" \
    "SMTP_FROM=$SMTP_FROM" \
    "SMTP_ENABLE_SSL=$SMTP_ENABLE_SSL" \
    $@ | $OC create -n "$KEYCLOAK_PROJECT_NAME" -f -

# Get Keycloak URL

KEYCLOAK_URL="$(route_url "$KEYCLOAK_PROJECT_NAME" web)"
echo KEYCLOAK_URL = $KEYCLOAK_URL

# Set Keycloak URL with Kapua

$OC set env -n "$KAPUA_PROJECT_NAME" dc/kapua-console "KEYCLOAK_URL=$KEYCLOAK_URL" "KAPUA_URL=$KAPUA_URL" 
