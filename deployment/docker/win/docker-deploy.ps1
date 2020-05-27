###############################################################################
# Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial implementation
###############################################################################
#Requires -Version 7

Param(
    [switch]$logs = $false
)

$script_dir = Split-Path (Get-Variable MyInvocation).Value.MyCommand.Path
$common_path = Join-Path $script_dir docker-common.ps1

. $common_path

Write-Host "Deploying Eclipse Kapua..."

[String[]]$compose_files = @()

If (Test-Path env:KAPUA_BROKER_DEBUG_PORT)
{
    If ($env:KAPUA_BROKER_DEBUG_SUSPEND -eq "true")
    {
        $env:KAPUA_BROKER_DEBUG_SUSPEND = "y"
    }
    Else
    {
        $env:KAPUA_BROKER_DEBUG_SUSPEND = "n"
    }
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.broker-debug.yml)
}

If (Test-Path env:KAPUA_ELASTICSEARCH_DATA_DIR)
{
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.es-storage-dir.yml)
}

docker-compose -f $(Join-Path $script_dir .. compose docker-compose.yml) $compose_files up -d

Write-Host "Deploying Eclipse Kapua... DONE!"

If ($logs)
{
    Write-Host "Opening Eclipse Kapua logs..."
    docker-compose -f $( Join-Path $script_dir .. compose docker-compose.yml ) logs -f
    Write-Host "Opening Eclipse Kapua logs... DONE!"
}
Else
{
    Write-Host "Run `"docker-compose -f $( Join-Path $script_dir .. compose docker-compose.yml ) logs -f`" for container logs"
}
