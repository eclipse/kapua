###############################################################################
# Copyright (c) 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial implementation
###############################################################################

$script_dir = Split-Path (Get-Variable MyInvocation).Value.MyCommand.Path
$common_path = $script_dir + '\docker-common.ps1'

. $common_path

Write-Host "Deploying Eclipse Kapua..."

docker-compose -f $script_dir'\..\compose\docker-compose.yml' up -d

Write-Host "Deploying Eclipse Kapua... DONE!"
Write-Host "Run `"docker-compose -f `"$script_dir\..\compose\docker-compose.yml`" logs -f`" for container logs"
