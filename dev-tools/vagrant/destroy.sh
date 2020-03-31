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

destroy() {
    echo 'Destroying Vagrant machine...'
    vagrant destroy -f $1 || { echo 'Destroying Vagrant machine... ERROR!'; exit 1; }
    echo 'Destroying Vagrant machine... DONE!'
}

print_usage_destroy(){
    echo "Usage: $(basename $0) help|base-box|develop" >&2
}

if [[ "$1" == 'develop' ]]; then
    destroy $1
elif [[ "$1" == 'base-box' ]]; then
    destroy $1
else
    print_usage_destroy
    exit 1
fi