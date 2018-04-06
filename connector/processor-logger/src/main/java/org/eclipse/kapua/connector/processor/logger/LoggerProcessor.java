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
package org.eclipse.kapua.connector.processor.logger;

import java.io.StringWriter;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.connector.KapuaConnectorException;
import org.eclipse.kapua.connector.Processor;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProcessor implements Processor<TransportMessage> {

    private static final Logger logger = LoggerFactory.getLogger(LoggerProcessor.class);

    @Override
    public void start() throws KapuaConnectorException {
        XmlUtil.setContextProvider( new LoggerProcessorJAXBContextProvider());
    }

    @Override
    public void process(TransportMessage message) throws KapuaConnectorException {

        StringWriter sw = new StringWriter();
        try {
            XmlUtil.marshal(message, sw);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.info(sw.toString());
    }

    @Override
    public void stop() throws KapuaConnectorException {
        // nothing to do
    }

}
