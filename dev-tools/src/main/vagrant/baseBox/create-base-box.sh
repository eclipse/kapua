#!/bin/bash
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

CURRDIR=$(pwd)
BASEDIR=$(dirname "$0")
KAPUA_BOX_TMP_DIR=$BASEDIR/kapua-box-tmp
KAPUA_BOX_NAME=trusty64/kapua-dev-box-0.1

KAPUA_BOX_EXISTS=$(vagrant box list | grep $KAPUA_BOX_NAME)

# Creating a new box may require to remove an existing one with the same name.
# Ask the user to confirm the operation before to proceed.

if [[ ! -z $KAPUA_BOX_EXISTS ]]

then

   echo
   echo 'The following box was found: '$KAPUA_BOX_EXISTS
   echo 'If you proceed it will be replaced. This operation requires'
   echo 'access to the internet. Depending on your internet connection'
   echo 'this operation may take some time.'
   echo
   
   read -p "Proceed with replacement [y/N] ? " -r

   echo

   if [[ $REPLY =~ ^[Yy]$ ]]

   then

      echo 'Removing base kapua box named: ' $KAPUA_BOX_NAME ' ...'

      vagrant box remove $KAPUA_BOX_NAME
      
   fi

fi

# If the box has been removed or it wasn't there before, then proceed 
# with the creation. Otherwise the user hasn't confirmed the removal 
# so skip.

KAPUA_BOX_EXISTS=$(vagrant box list | grep $KAPUA_BOX_NAME)

if [[ -z "$KAPUA_BOX_EXISTS" ]]

then

   echo 'Creating base kapua box named ' $KAPUA_BOX_NAME ' ...'
   
   mkdir -p $KAPUA_BOX_TMP_DIR

   cp $BASEDIR/baseBox-Vagrantfile $KAPUA_BOX_TMP_DIR/Vagrantfile

   cd $KAPUA_BOX_TMP_DIR

   echo '========================'
      
   pwd
      
   vagrant up

   vagrant package --output trusty64-kapua-dev-0.1.box

   vagrant box add $KAPUA_BOX_NAME trusty64-kapua-dev-0.1.box

   vagrant destroy --force 

   # go up one level to allow the removal of the tmp dir
    
   cd ../..

   rm -rf $KAPUA_BOX_TMP_DIR

   cd $CURRDIR

   echo '... done.'

fi