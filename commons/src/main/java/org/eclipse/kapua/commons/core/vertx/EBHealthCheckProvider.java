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


public class EBHealthCheckProvider implements HealthCheckProvider {

    private EventBus eventBus;
    private String healthCheckAddress;

    public static HealthCheckProvider create(EventBus eventBus, String healthCheckAddress) {
        return new EBHealthCheckProvider(eventBus, healthCheckAddress);
    }

    private EBHealthCheckProvider(EventBus eventBus, String healthCheckAddress) {
        this.eventBus = eventBus;
        this.healthCheckAddress = healthCheckAddress;
    }

    @Override
    public void registerHealthChecks(HealthCheckHandler handler) {
        handler.register("more-checks", hcm -> {
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
