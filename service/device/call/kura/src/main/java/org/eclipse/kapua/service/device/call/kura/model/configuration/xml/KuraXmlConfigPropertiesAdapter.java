/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration.xml;

import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.xml.adapters.BooleanPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.BytePropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.CharPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.DoublePropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.FloatPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.IntegerPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.LongPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.ShortPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.StringPropertyAdapter;
import org.eclipse.kapua.model.xml.adapters.XmlPropertiesAdapter;
import org.eclipse.kapua.model.xml.adapters.XmlPropertyAdapter;
import org.eclipse.kapua.service.device.call.kura.model.configuration.xml.XmlConfigPropertyAdapted.ConfigPropertyType;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * Xml Kura configuration properties adapter. It marshal and unmarshal configuration properties in a proper way.
 *
 * @since 1.0
 */
public class KuraXmlConfigPropertiesAdapter extends XmlAdapter<KuraXmlConfigPropertiesAdapted, Map<String, Object>> {

    private XmlPropertiesAdapter<XmlConfigPropertyAdapted.ConfigPropertyType, XmlConfigPropertyAdapted> adapter = new XmlPropertiesAdapter<>(XmlConfigPropertyAdapted.class, () -> new XmlConfigPropertyAdapted(), new HashMap<ConfigPropertyType, XmlPropertyAdapter>() {
        {
            put(ConfigPropertyType.stringType, new StringPropertyAdapter());
            put(ConfigPropertyType.longType, new LongPropertyAdapter());
            put(ConfigPropertyType.doubleType, new DoublePropertyAdapter());
            put(ConfigPropertyType.floatType, new FloatPropertyAdapter());
            put(ConfigPropertyType.integerType, new IntegerPropertyAdapter());
            put(ConfigPropertyType.byteType, new BytePropertyAdapter());
            put(ConfigPropertyType.charType, new CharPropertyAdapter());
            put(ConfigPropertyType.booleanType, new BooleanPropertyAdapter());
            put(ConfigPropertyType.shortType, new ShortPropertyAdapter());
            put(ConfigPropertyType.passwordType, new KuraPasswordPropertyAdapter(KapuaLocator.getInstance().getComponent(CryptoUtil.class)));
        }
    });

    public KuraXmlConfigPropertiesAdapter() {
    }

    @Override
    public Map<String, Object> unmarshal(KuraXmlConfigPropertiesAdapted v) throws Exception {
        return adapter.unmarshal(v.getProperties());
    }

    @Override
    public KuraXmlConfigPropertiesAdapted marshal(Map<String, Object> v) throws Exception {
        final KuraXmlConfigPropertiesAdapted res = new KuraXmlConfigPropertiesAdapted();
        res.setProperties(adapter.marshal(v));
        return res;
    }
}
