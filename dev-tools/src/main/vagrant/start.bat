@echo off
rem *******************************************************************************
rem Copyright (c) 2011, 2016 Eurotech and/or its affiliates
rem
rem All rights reserved. This program and the accompanying materials
rem are made available under the terms of the Eclipse Public License v1.0
rem which accompanies this distribution, and is available at
rem http://www.eclipse.org/legal/epl-v10.html
rem
rem Contributors:
rem     Eurotech - initial API and implementation
rem
rem ******************************************************************************
echo. 
echo.
echo.

if "%1" == "develop" GOTO destroy_old_machines
if "%1" == "demo" GOTO destroy_old_machines
if "%1" == "base-box" GOTO base_box 
echo Usage: "%0  help | base-box | develop | demo" 

GOTO all_done

:destroy_old_machines

    vagrant destroy -f demo
    vagrant destroy -f develop
if %1 == "develop" GOTO start_develop
if %1 == "demo" GOTO start_demo
GOTO all_done

:start_develop

    echo 'Kapua vagrant develop machine... starting'
    vagrant up
    echo 'Kapua vagrant develop machine... starting DONE'
    echo "Please type 'vagrant ssh' to connect to the machine."
    echo "Follow the instructions to start the kapua components from the machine"
GOTO all_done

:start_demo
    echo 'Kapua vagrant demo machine ... starting'

    vagrant up demo

    echo 'Kapua vagrant demo machine... starting DONE'
    echo "Please type 'vagrant ssh demo' to connect to the machine"
    echo "Follow the instructions to deploy the kapua components into the machine"
GOTO all_done

:base_box 
   baseBox\create.bat

: all_done
echo.
echo.
