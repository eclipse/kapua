/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Default {@link CamelKapuaDefaultRouter}
 *
 * @since 1.0.0
 */
public class CamelKapuaDefaultRouter {

    private static final Logger LOG = LoggerFactory.getLogger(CamelKapuaDefaultRouter.class);

    private EndPointContainer endPointContainer;

    public CamelKapuaDefaultRouter() {
        String configurationFileName = BrokerSetting.getInstance().getString(BrokerSettingKey.CAMEL_DEFAULT_ROUTE_CONFIGURATION_FILE_NAME);

        URL url;
        try {
            url = KapuaFileUtils.getAsURL(configurationFileName);
        } catch (KapuaSettingException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot find configuration file!");
        }

        LOG.info("Default Camel routing | Loading configuration from file {}...", url.getFile());

        try (FileReader configurationFileReader = new FileReader(url.getFile())) {
            endPointContainer = XmlUtil.unmarshal(configurationFileReader, EndPointContainer.class);
            LOG.info("Default Camel routing | Found {} parent endpoints in the route", endPointContainer.getEndPoints().size());
            logLoadedEndPoints(endPointContainer.getEndPoints());
        } catch (JAXBException | SAXException | IOException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load configuration!");
        }

        LOG.info("Default Camel routing | Loading configuration from file '{}'... DONE!", url.getFile());
    }

    public String defaultRoute(Exchange exchange, Object value, @Header(Exchange.SLIP_ENDPOINT) String previous, @Properties Map<String, Object> properties) {
        LOG.trace("Received message on topic {} - Previous slip endpoint {} - id {}",
                exchange.getIn().getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class),
                previous,
                exchange.getIn().getHeader(CamelConstants.JMS_CORRELATION_ID));

        for (EndPoint endPoint : endPointContainer.getEndPoints()) {
            if (endPoint.matches(exchange, value, previous, properties)) {
                return endPoint.getEndPoint(exchange, value, previous, properties);
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
