###############################################################################
# Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial implementation
###############################################################################
#Requires -Version 7

$script_dir = Split-Path (Get-Variable MyInvocation).Value.MyCommand.Path
$common_path = Join-Path $script_dir docker-common.ps1

. $common_path

Write-Host "Opening Eclipse Kapua logs..."

docker-compose -f (Join-Path $script_dir .. compose docker-compose.yml) logs -f

Write-Host "Opening Eclipse Kapua logs... DONE!"
