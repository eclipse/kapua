/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.consumer.lifecycle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.ServiceStatus;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

public class LifecycleRouteHealthIndicator extends AbstractHealthIndicator {

    //TODO move to a configuration parameter instead of seeding it from Spring? (anyway Spring can get the value from environment variable)
    private int routeCount;

    private CamelContext camelContext;

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        List<Route> routes = camelContext.getRoutes();
        Map<String, Object> details = new HashMap<>();
        details.put("routes", new Integer(routes.size()));
        int running = 0;
        for (Route route : routes) {
            ServiceStatus routeStatus = camelContext.getRouteController().getRouteStatus(route.getId());
            details.put(route.getId(), routeStatus.name());
            if (routeStatus.isStarted()) {
                running++;
            }
        };
        details.put("running", running);
        if (routeCount <= running) {
            builder.up().withDetails(details).build();
        }
        else {
            builder.down().withDetails(details).build();
        }
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }
}
