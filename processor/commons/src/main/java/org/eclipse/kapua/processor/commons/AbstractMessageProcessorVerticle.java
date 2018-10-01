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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public abstract class AbstractMessageProcessorVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Future<Void> theFuture = Future.future();
        internalStart(theFuture);
    }

    @Override
    public void stop() throws Exception {
        Future<Void> theFuture = Future.future();
        internalStop(theFuture);
    }

    protected abstract void internalStart(Future<Void> startFuture) throws Exception;

    protected abstract void internalStop(Future<Void> stopFuture) throws Exception;
 }
