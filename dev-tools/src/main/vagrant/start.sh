#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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

destroy_old_machines(){
    vagrant destroy -f demo
    vagrant destroy -f develop
}

start_develop(){
    echo 'Kapua vagrant develop machine... starting'

    vagrant up

    echo 'Kapua vagrant develop machine... starting DONE'
    echo "Please type 'vagrant ssh' to connect to the machine."
    echo "Follow the instructions to start the kapua components from the machine"
}

start_demo(){
    echo 'Kapua vagrant demo machine ... starting'

    vagrant up demo

    echo 'Kapua vagrant demo machine... starting DONE'
    echo "Please type 'vagrant ssh demo' to connect to the machine"
    echo "Follow the instructions to deploy the kapua components into the machine"
}

print_usage(){
    echo "Usage: $(basename $0) help|base-box|develop|demo" >&2
}

if [ "$1" == 'develop' ]; then
    destroy_old_machines
    start_develop
elif [ "$1" == 'demo' ]; then
    destroy_old_machines
    start_demo
elif [ "$1" == 'base-box' ]; then
    sh baseBox/create.sh
else
    print_usage
    exit 1
fi