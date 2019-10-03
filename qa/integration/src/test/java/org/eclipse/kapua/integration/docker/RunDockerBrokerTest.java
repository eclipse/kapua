/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.integration.docker;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/docker/broker.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.qa.integration.steps"
        },
        plugin = { "pretty",
                "html:target/cucumber/DockerBroker",
                "json:target/DockerBroker_cucumber.json"
        },
        monochrome = true)
//@CucumberProperty(key="DOCKER_HOST", value= "127.0.0.1")
//@CucumberProperty(key="DOCKER_CERT_PATH", value= "...")
//@CucumberProperty(key="commons.db.schema.update", value= "true")
//@CucumberProperty(key="commons.db.connection.host", value= "db")
//@CucumberProperty(key="commons.db.connection.port", value= "3306")
//@CucumberProperty(key="datastore.elasticsearch.nodes", value= "es")
//@CucumberProperty(key="datastore.elasticsearch.port", value= "9200")
//@CucumberProperty(key="datastore.client.class", value= "org.eclipse.kapua.service.datastore.client.rest.RestDatastoreClient")
//@CucumberProperty(key="commons.eventbus.url", value= "failover:(amqp://events-broker:5672)?jms.sendTimeout=1000")
//@CucumberProperty(key="certificate.jwt.private.key", value= "file:///var/opt/activemq/key.pk8")
//@CucumberProperty(key="certificate.jwt.certificate", value= "file:///var/opt/activemq/cert.pem")
//@CucumberProperty(key="broker.ip", value= "broker")
public class RunDockerBrokerTest {
}
