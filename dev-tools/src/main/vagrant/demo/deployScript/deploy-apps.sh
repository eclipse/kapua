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
vagrant ssh -c "echo 'deploying the Kapua broker'
	cd /usr/local/kapua/
	rm -rf apache-tomcat-${TOMCAT_VERSION}
	echo 'uncompressing Kapua console web application'
	tar -xvzf /kapua/assembly/target/kapua-console*
	mv kapua-console* apache-tomcat-${TOMCAT_VERSION}
	cp /kapua/rest-api/target/api*.war apache-tomcat-${TOMCAT_VERSION}/webapps
	sudo chown -R vagrant:vagrant apache-tomcat-${TOMCAT_VERSION}"
