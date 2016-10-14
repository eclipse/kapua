#*******************************************************************************
# Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#
#*******************************************************************************
echo 'VARGRANT UP......'
vagrant box list | grep -q trusty64/kapua-dev-box-0.1 || ../start.sh base-box
vagrant up
echo 'VARGRANT UP...... DONE'
