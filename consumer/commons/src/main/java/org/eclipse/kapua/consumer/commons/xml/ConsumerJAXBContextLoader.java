/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.commons.xml;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.consumer.commons.setting.ConsumerSetting;
import org.eclipse.kapua.consumer.commons.setting.ConsumerSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jaxb context loader
 *
 */
public class ConsumerJAXBContextLoader {

    protected static final Logger logger = LoggerFactory.getLogger(ConsumerJAXBContextLoader.class);

    private static final String JAXB_CONTEXT_CLASS_NAME;

    static {
        ConsumerSetting config = ConsumerSetting.getInstance();
        JAXB_CONTEXT_CLASS_NAME = config.getString(ConsumerSettingKey.JAXB_CONTEXT_CLASS_NAME);
    }

    public ConsumerJAXBContextLoader() throws KapuaException {
    }

    public void init() throws KapuaException {
        logger.info(">>> Jaxb context loader... load context");
        JAXBContextProvider jaxbContextProvider = ClassUtil.newInstance(JAXB_CONTEXT_CLASS_NAME, null);
        XmlUtil.setContextProvider(jaxbContextProvider);
        logger.info(">>> Jaxb context loader... load context DONE");
    }

    public void reset() {
        XmlUtil.setContextProvider(null);
    }

}
