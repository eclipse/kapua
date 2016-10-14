#!/bin/bash
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
cp develop/destroy-old-machine.sh .
sh destroy-old-machine.sh
rm destroy-old-machine.sh
cp develop/develop-Vagrantfile Vagrantfile
echo '======> kapua vagrant develop machine... starting'
vagrant up
echo '======> kapua vagrant develop machine... starting DONE'
echo "======> please type 'vagrant ssh' to connect to the machine."
echo "======> Follow the instructions to start the kapua components from the machine"