/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.microservice.jobengine;

import java.util.Collections;
import java.util.List;

import org.eclipse.kapua.microservice.commons.AbstractHttpServerVerticle;
import org.eclipse.kapua.microservice.commons.HttpEndpoint;

import io.vertx.core.http.HttpServerOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineHttpServerVerticle extends AbstractHttpServerVerticle {

    @Autowired
    private JobEngineHttpEndpoint jobEngineHttpEndpoint;

    @Override
    protected List<HttpEndpoint> getHttpEndpoint() {
        return Collections.singletonList(jobEngineHttpEndpoint);
    }

    @Override
    protected HttpServerOptions getHttpServerOptions() {
        return new HttpServerOptions().setPort(8090);
    }
}
