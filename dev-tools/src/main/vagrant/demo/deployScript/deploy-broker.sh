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
vagrant ssh -c "echo 'deploying the Kapua broker'
	cd /usr/local/kapua/apache-activemq-${ACTIVEMQ_VERSION}
	echo 'deleting different guava/shiro runtime dependency'
	sudo rm lib/optional/guava-*
	sudo rm lib/optional/shiro-*
	echo 'deleting old Kapua runtime dependency'
	find lib/extra ! -name 'mqtt-client*.jar' -type f -exec rm -f {} +
	echo 'copying Kapua runtime dependency'
	sudo cp /kapua/assembly/target/broker_dependency/* lib/extra
	echo 'copying Kapua arctifact'
	sudo cp /kapua/broker-core/target/kapua-*.jar lib/extra
	sudo cp /kapua/commons/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/locator/guice/target/kapua-*.jar lib/extra
	sudo cp /kapua/message/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/message/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/account/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/account/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/datastore/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/datastore/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/bundle/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/bundle/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/call/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/call/kura/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/command/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/command/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/commons/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/configuration/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/configuration/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/packages/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/packages/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/registry/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/device/registry/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/idgenerator/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/idgenerator/sequence/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/security/authentication/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/security/authorization/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/security/shiro/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/user/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/service/user/internal/target/kapua-*.jar lib/extra
	sudo cp /kapua/translator/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/translator/kapua/kura/target/kapua-*.jar lib/extra
	sudo cp /kapua/translator/kura/jms/target/kapua-*.jar lib/extra
	sudo cp /kapua/translator/kura/mqtt/target/kapua-*.jar lib/extra
	sudo cp /kapua/transport/api/target/kapua-*.jar lib/extra
	sudo cp /kapua/transport/jms/target/kapua-*.jar lib/extra
	sudo cp /kapua/transport/mqtt/target/kapua-*.jar lib/extra
	echo 'copying Kapua configuration'
	sudo cp /kapua/assembly/src/main/resources/conf/broker/activemq.xml conf/
	cd ..
	sudo chown -R vagrant:vagrant apache-activemq-${ACTIVEMQ_VERSION}"
