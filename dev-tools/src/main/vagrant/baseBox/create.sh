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

BASEDIR=$(dirname "$0")
KAPUA_BOX_VERSION=0.6
KAPUA_BOX_NAME="kapua-dev-box/${KAPUA_BOX_VERSION}"
KAPUA_BOX_TMP_DIR="/tmp/kapua-dev-box"

# Creating a new box may require to remove an existing one with the same name.
# Ask the user to confirm the operation before to proceed.
if vagrant box list | grep -q "${KAPUA_BOX_NAME}"; then
   echo
   echo "The following box was found: ${KAPUA_BOX_EXISTS}"
   echo "If you proceed it will be replaced. This operation requires"
   echo "access to the internet. Depending on your internet connection"
   echo "this operation may take some time."
   echo
   read -p "Proceed with replacement [y/N] ? " -r
   echo

   if [[ ${REPLY} =~ ^[Yy]$ ]]; then
      echo 'Removing base kapua box named: ' ${KAPUA_BOX_NAME} ' ...'
      vagrant box remove ${KAPUA_BOX_NAME}
   fi
fi

# If the box has been removed or it wasn't there before, then proceed
# with the creation. Otherwise the user hasn't confirmed the removal
# so skip.
if ! vagrant box list | grep -q "${KAPUA_BOX_NAME}"; then
   echo "Creating base kapua box named ${KAPUA_BOX_NAME} ..."

   rm -rf ${KAPUA_BOX_TMP_DIR}
   mkdir -p ${KAPUA_BOX_TMP_DIR}

   cp ${BASEDIR}/Vagrantfile ${KAPUA_BOX_TMP_DIR}/Vagrantfile

   pushd ${KAPUA_BOX_TMP_DIR}

   vagrant up
   vagrant package --output kapua-dev-${KAPUA_BOX_VERSION}.box
   vagrant box add ${KAPUA_BOX_NAME} kapua-dev-${KAPUA_BOX_VERSION}.box
   vagrant destroy --force

   popd

   rm -rf ${KAPUA_BOX_TMP_DIR}

   echo '... done.'
fi