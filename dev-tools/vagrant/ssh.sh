#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2020 Eurotech and/or its affiliates
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
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