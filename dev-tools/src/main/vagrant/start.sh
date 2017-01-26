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
if [ "$#" -lt 1 ]; 
then
	echo '======> missing input parameter. please specify develop/demo/base-box'
 	exit 2
fi
if [ $1 = 'develop' ]
then
	echo '======> start develop vagrant machine'
	sh develop/start-develop.sh
elif [ $1 = 'demo' ] 
then
	echo '======> start demo vagrant machine'
	sh demo/start-demo.sh
elif [ $1 = 'base-box' ]
then
	echo '======> create/update base box'
	sh baseBox/create-base-box.sh
else 
	echo 'unknown parameter value'
	exit 1
fi