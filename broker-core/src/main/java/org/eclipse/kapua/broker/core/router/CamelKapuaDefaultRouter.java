/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.router;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.core.listener.CamelConstants;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Default message router
 *
 * @since 1.0
 */
public class CamelKapuaDefaultRouter {

    private static final Logger LOG = LoggerFactory.getLogger(CamelKapuaDefaultRouter.class);

    private EndPointContainer endPointContainer;

    public CamelKapuaDefaultRouter() {
        String configurationFileName = BrokerSetting.getInstance().getString(BrokerSettingKey.CAMEL_DEFAULT_ROUTE_CONFIGURATION_FILE_NAME);
        URL url = null;
        try {
            url = KapuaFileUtils.getAsURL(configurationFileName);
        } catch (KapuaSettingException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot find configuration file!");
        }
        LOG.info("Default Camel routing... Loading configuration from file {}", url.getFile());
        FileReader configurationFileReader = null;
        try {
            configurationFileReader = new FileReader(url.getFile());
            endPointContainer = XmlUtil.unmarshal(configurationFileReader, EndPointContainer.class);
            LOG.info("Default Camel routing... Loading configuration from file {} Found {} parent endpoints in the route", configurationFileName,
                    (endPointContainer.getEndPoints() != null ? endPointContainer.getEndPoints().size() : 0));
            logLoadedEndPoints(endPointContainer.getEndPoints());
        } catch (XMLStreamException | JAXBException | SAXException | IOException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load configuration!");
        } finally {
            if (configurationFileReader != null) {
                try {
                    configurationFileReader.close();
                } catch (IOException e) {
                    LOG.warn("Cannot close configuration file '{}'!", configurationFileName, e);
                }
            }
        }
        LOG.info("Default Camel routing... Loading configuration '{}' from file '{}' DONE", configurationFileName, url.getFile());
    }

    public String defaultRoute(Exchange exchange, Object value, @Header(Exchange.SLIP_ENDPOINT) String previous, @Properties Map<String, Object> properties) {
        LOG.trace("Received message on topic {} - Previous slip endpoint {} - id {}",
                exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class),
                previous,
                exchange.getIn().getHeader(CamelConstants.JMS_CORRELATION_ID));
        for (EndPoint endPoint : endPointContainer.getEndPoints()) {
            if (endPoint.matches(exchange, value, previous, properties)) {
                return endPoint.getEndpoint(exchange, value, previous, properties);
            }
        }
        return null;
    }

    private void logLoadedEndPoints(List<EndPoint> endPoints) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        for (EndPoint endPoint : endPoints) {
            endPoint.toLog(buffer, "");
        }
        LOG.info(buffer.toString());
    }

}