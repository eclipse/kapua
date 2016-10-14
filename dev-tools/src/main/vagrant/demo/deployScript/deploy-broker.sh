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
    sudo cp /kapua/org.eclipse.kapua.assembly/target/broker_dependency/* lib/extra
	echo 'copying Kapua arctifact'
	sudo cp /kapua/commons/target/kapua-commons*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service/target/org.eclipse.kapua.service*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.locator.guice/target/org.eclipse.kapua.locator.guice*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.broker.core/target/org.eclipse.kapua.broker.core*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.message/target/org.eclipse.kapua.message*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.message.internal/target/org.eclipse.kapua.message.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.account.internal/target/org.eclipse.kapua.service.account.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.authentication/target/org.eclipse.kapua.service.authentication*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.authorization/target/org.eclipse.kapua.service.authorization*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.auth.shiro/target/org.eclipse.kapua.service.auth.shiro*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.call/target/org.eclipse.kapua.service.device.call*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.call.kura/target/org.eclipse.kapua.service.device.call.kura*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management/target/org.eclipse.kapua.service.device.management*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.bundle/target/org.eclipse.kapua.service.device.management.bundle*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.bundle.internal/target/org.eclipse.kapua.service.device.management.bundle.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.command/target/org.eclipse.kapua.service.device.management.command*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.command.internal/target/org.eclipse.kapua.service.device.management.command.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.commons/target/org.eclipse.kapua.service.device.management.commons*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.configuration/target/org.eclipse.kapua.service.device.management.configuration*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.configuration.internal/target/org.eclipse.kapua.service.device.management.configuration.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.deploy/target/org.eclipse.kapua.service.device.management.deploy*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.management.deploy.internal/target/org.eclipse.kapua.service.device.management.deploy.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.registry/target/org.eclipse.kapua.service.device.registry*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.device.registry.internal/target/org.eclipse.kapua.service.device.registry.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.generator.id/target/org.eclipse.kapua.service.generator.id*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.generator.id.sequence/target/org.eclipse.kapua.service.generator.id.sequence*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.user/target/org.eclipse.kapua.service.user*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.user.internal/target/org.eclipse.kapua.service.user.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.datastore/target/org.eclipse.kapua.service.datastore*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.datastore.internal/target/org.eclipse.kapua.service.datastore.internal*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.service.account/target/org.eclipse.kapua.service.account*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.translator/target/org.eclipse.kapua.translator*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.translator.kapua.kura/target/org.eclipse.kapua.translator.kapua.kura*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.translator.kura.jms/target/org.eclipse.kapua.translator.kura.jms*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.translator.kura.mqtt/target/org.eclipse.kapua.translator.kura.mqtt*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.transport/target/org.eclipse.kapua.transport*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.transport.jms/target/org.eclipse.kapua.transport.jms*.jar lib/extra
	sudo cp /kapua/org.eclipse.kapua.transport.mqtt/target/org.eclipse.kapua.transport.mqtt*.jar lib/extra
	echo 'copying Kapua configuration'
	sudo cp /kapua/org.eclipse.kapua.assembly/src/main/resources/conf/broker/activemq.xml conf/
	cd ..
	sudo chown -R vagrant:vagrant apache-activemq-${ACTIVEMQ_VERSION}"
