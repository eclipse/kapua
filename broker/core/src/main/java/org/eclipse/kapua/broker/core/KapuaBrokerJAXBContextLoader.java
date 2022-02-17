/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jaxb context loader
 *
 */
public class KapuaBrokerJAXBContextLoader {

    protected static final Logger logger = LoggerFactory.getLogger(KapuaBrokerJAXBContextLoader.class);

    private static final String BROKER_JAXB_CONTEXT_CLASS_NAME;

    static {
        BrokerSetting config = BrokerSetting.getInstance();
        BROKER_JAXB_CONTEXT_CLASS_NAME = config.getString(BrokerSettingKey.BROKER_JAXB_CONTEXT_CLASS_NAME);
    }

    public void init() throws KapuaException {
        logger.info(">>> Broker jaxb context loader... load context");
        JAXBContextProvider jaxbContextProvider = ClassUtil.newInstance(BROKER_JAXB_CONTEXT_CLASS_NAME, BrokerJAXBContextProvider.class);
        XmlUtil.setContextProvider(jaxbContextProvider);
        logger.info(">>> Broker jaxb context loader... load context DONE");
    }

    public void reset() {
        XmlUtil.setContextProvider(null);
    }

}
