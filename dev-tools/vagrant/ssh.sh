#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2020, 2022 Eurotech and/or its affiliates
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
#
#******************************************************************************

set -e

print_usage_ssh(){
    echo "Usage: $(basename $0) help|develop" >&2
}

ssh() {
    echo 'SSH into Vagrant machine...'
    vagrant ssh $1 || { echo 'SSH into Vagrant machine... ERROR!'; exit 1; }
    echo 'SSH into Vagrant machine... DONE!'
}

if [[ "$1" == 'develop' ]]; then
    ssh $1
else
    print_usage_ssh
    exit 1
fi
