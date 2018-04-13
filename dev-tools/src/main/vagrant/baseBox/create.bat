@echo off
rem *******************************************************************************
rem  Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
rem 
rem  All rights reserved. This program and the accompanying materials
rem  are made available under the terms of the Eclipse Public License v1.0
rem  which accompanies this distribution, and is available at
rem  http://www.eclipse.org/legal/epl-v10.html
rem 
rem  Contributors:
rem      Eurotech - initial API and implementation
rem 
rem *******************************************************************************

set BASEDIR=%~dp0
set KAPUA_BOX_VERSION=0.6
set KAPUA_BOX_NAME="kapua-dev-box/%KAPUA_BOX_VERSION%"
set KAPUA_BOX_TMP_DIR="%temp%\kapua-dev-box"
rem  Creating a new box may require to remove an existing one with the same name.
rem  Ask the user to confirm the operation before to proceed.

vagrant box list | find %KAPUA_BOX_NAME%
	if %errorlevel%==0 GOTO found_one
	GOTO create
:found_one
		rem found a kapua box of the same version (0.6)
		echo.
		echo The following box was found: %KAPUA_BOX_NAME%
		echo If you proceed it will be replaced. This operation requires
		echo access to the internet. Depending on your internet connection
		echo this operation may take some time.
		echo.
		echo Please enter Y to create remove the above box and create a new one, 
		echo or N to leave the existing bos in place
		set /p answer=Continue [Y/N] 
		if "%answer:~,1%" EQU "Y" GOTO remove
		if "%answer:~,1%" EQU "Y" GOTO all_done
		if "%answer:~,1%" EQU "y" GOTO remove
		if "%answer:~,1%" EQU "n" GOTO all_done
		echo Please enter either Y for yes or N for No
		rem don't delete the box and don't create anything 
		GOTO found_one


:remove
 echo Removing base kapua box named: '%KAPUA_BOX_NAME%'...
 
 vagrant box remove %KAPUA_BOX_NAME%


rem  If the box has been removed or it wasn't there before, then proceed
rem  with the creation. Otherwise the user hasn't confirmed the removal
rem  so skip.
:create
vagrant box list | find %KAPUA_BOX_NAME%
	if %errorlevel%==0 GOTO found_one
	echo "Creating base kapua box named %KAPUA_BOX_NAME% ...
	del /s /q %KAPUA_BOX_TMP_DIR%
	rmdir /s /q %KAPUA_BOX_TMP_DIR%
	mkdir %KAPUA_BOX_TMP_DIR%
	copy %BASEDIR%/Vagrantfile %KAPUA_BOX_TMP_DIR%/Vagrantfile

   pushd %KAPUA_BOX_TMP_DIR%

   vagrant up
   vagrant package --output kapua-dev-%KAPUA_BOX_VERSION%.box
   vagrant box add %KAPUA_BOX_NAME% kapua-dev-%KAPUA_BOX_VERSION%.box
   vagrant destroy --force

   popd

	del /s /q %KAPUA_BOX_TMP_DIR%
	rmdir /s /q %KAPUA_BOX_TMP_DIR%
:all_done
   echo ... done