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

Param(
    [switch]$dev = $false,
    [switch]$debug = $false,
    [switch]$jmx = $false,
    [switch]$logs = $false
)

$script_dir = Split-Path (Get-Variable MyInvocation).Value.MyCommand.Path
$common_path = Join-Path $script_dir docker-common.ps1

. $common_path

Write-Host "Deploying Eclipse Kapua..."

[String[]]$compose_files = @()

# Debug Mode
If ($debug) {
    Write-Host "Debug mode enabled!"
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.broker-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.console-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.consumer-lifecycle-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.consumer-telemetry-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.service-authentication-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.job-engine-debug.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.rest-debug.yml)
}

# JMX Mode
If ($jmx) {
    Write-Host "JMX mode enabled!"
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.broker-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.console-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.consumer-lifecycle-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.consumer-telemetry-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.service-authentication-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.job-engine-jmx.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.rest-jmx.yml)
}

# Dev Mode
If($dev) {
    Write-Host "Dev mode enabled!"
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.db-dev.yml)
    $compose_files+="-f"
    $compose_files+=$(Join-Path $script_dir .. compose extras docker-compose.es-dev.yml)
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
