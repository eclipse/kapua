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
package org.eclipse.kapua.processor.logger;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.apps.api.HealthCheckable;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.healthchecks.Status;

public class LoggerProcessor implements Processor<Message>, HealthCheckable {

    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Instantiate Jaxb Context... Done.");
        startFuture.complete();
    }

    @Override
    public void process(MessageContext<Message> message, Handler<AsyncResult<Void>> result) throws KapuaProcessorException {
        //avoid null check on message fields
        try {
            logger.info("Message (between #): #{}#", String.valueOf(message.getMessage().getBody()));
        }
        catch (NullPointerException e) {
            //TODO should switch to succeed since this is already a message that triggered errors during processing?
            result.handle(Future.failedFuture(e));
            logger.warn("Received malformed message!");
        }
        result.handle(Future.succeededFuture());

        //old code
//        import org.eclipse.kapua.commons.util.xml.XmlUtil;
//        XmlUtil.setContextProvider(new LoggerProcessorJAXBContextProvider());
//        StringWriter sw = new StringWriter();
//        try {
//            XmlUtil.marshalJson(message.getMessage(), sw);
//        } catch (Exception e) {
//            result.handle(Future.failedFuture(e));
//            logger.error("Exception while marshalling message: {}", e.getMessage(), e);
//        }
//
//        logger.info(sw.toString());
//        result.handle(Future.succeededFuture());
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        // nothing to do
        stopFuture.complete();
    }

    @Override
    public Status getStatus() {
        return Status.OK();
    }

    @Override
    public boolean isHealty() {
        return true;
    }
}
