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
package org.eclipse.kapua.connector.logger;

import org.apache.qpid.proton.message.Message;
import org.eclipse.kapua.connector.KapuaProcessorException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.MessageTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public abstract class LoggerTarget implements MessageTarget<Message> {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTarget.class);

    public static LoggerTarget getProcessorWithNoFilter() {
        return new LoggerTarget() {
            @Override
            public boolean isProcessDestination(MessageContext<Message> message) {
                return true;
            }
        };
    }

    @Override
    public void start(Future<Void> startFuture) {
        logger.info("Instantiate Jaxb Context... Done.");
        startFuture.complete();
    }

    @Override
    public void process(MessageContext<Message> message, Handler<AsyncResult<Void>> result) throws KapuaProcessorException {
        //avoid null check on message fields
        logger.info("Message (between #): #{}#", message.getMessage() != null ? String.valueOf(message.getMessage().getBody()) : "NULL");
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
}
