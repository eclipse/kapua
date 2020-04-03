#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2018 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

export IMAGE_VERSION=${IMAGE_VERSION:=latest}

# parse ifconfig and take the first available IP address (should work for all *nix systems)
EXTERNAL_IP=$(ifconfig | grep -E "([0-9]{1,3}\.){3}[0-9]{1,3}" | grep -v '127.0.0.1' | awk '{ print $2 }' | cut -f2 -d: | head -n1)
export EXTERNAL_IP
