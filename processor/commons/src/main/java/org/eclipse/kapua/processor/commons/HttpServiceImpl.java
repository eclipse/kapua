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
package org.eclipse.kapua.processor.commons;

import org.eclipse.kapua.commons.core.vertx.AbstractHttpService;
import org.eclipse.kapua.commons.core.vertx.HttpServiceConfig;

import io.vertx.core.Vertx;

public class HttpServiceImpl extends AbstractHttpService {

    protected HttpServiceImpl(Vertx aVertx, HttpServiceConfig aConfig) {
        super(aVertx, aConfig);
    }

    public static HttpServiceImpl create(Vertx aVertx, HttpServiceConfig aConfig) {
        return new HttpServiceImpl(aVertx, aConfig);
    }
}
