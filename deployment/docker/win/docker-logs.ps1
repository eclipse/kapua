###############################################################################
# Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
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
