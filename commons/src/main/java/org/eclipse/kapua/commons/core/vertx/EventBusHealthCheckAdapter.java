/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core.vertx;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;


public class EventBusHealthCheckAdapter implements HealthCheckAdapter {

    private EventBus eventBus;
    private String healthCheckName;
    private String healthCheckAddress;

    public static HealthCheckAdapter create(EventBus eventBus, String healthCheckName, String healthCheckAddress) {
        return new EventBusHealthCheckAdapter(eventBus, healthCheckName, healthCheckAddress);
    }

    private EventBusHealthCheckAdapter(EventBus eventBus, String healthCheckName, String healthCheckAddress) {
        this.eventBus = eventBus;
        this.healthCheckName = healthCheckName;
        this.healthCheckAddress = healthCheckAddress;
    }

    @Override
    public void register(HealthCheckHandler handler) {
        handler.register(healthCheckName, hcm -> {
            eventBus.<JsonObject>send(healthCheckAddress, "", reply -> {

                if (reply.succeeded()) {
                    System.out.println(reply.result().body().toString());
                    String status = reply.result().body().getString("outcome");
                    if (status.equals("UP")) {
                        hcm.complete(Status.OK(reply.result().body()));
                    } else {
                        hcm.complete(Status.KO(reply.result().body()));
                    }
                } else {
                    hcm.complete(Status.KO());
                }
            });
        });
    }

}
