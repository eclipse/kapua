#*******************************************************************************
# Copyright (c) 2011, 2016 Eurotech and/or its affiliates
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#      Eurotech - initial API and implementation
#*******************************************************************************
vagrant ssh demo -c "echo 'deploying the Kapua broker'
	cd /usr/local/kapua/apache-tomcat-${TOMCAT_VERSION}
	sudo rm -rf webapp/admin*
	sudo rm -rf webapp/api*
	echo 'copying Kapua console web application'
	sudo cp /kapua/console/web/target/admin.war webapps/admin.war
	echo 'copying Kapua api web application'
	sudo cp /kapua/rest-api/web/target/api.war webapps/api.war
	cd ..
	sudo chown -R vagrant:vagrant apache-tomcat-${TOMCAT_VERSION}"