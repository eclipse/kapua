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
package org.eclipse.kapua.broker.core.route;

import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;

public interface OnException {

    void appendExceptionDefinition(RouteDefinition routeDefinition, CamelContext camelContext);

    void toLog(StringBuffer buffer, String prefix);

}
