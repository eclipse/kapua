/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message;

import org.eclipse.kapua.service.device.management.message.xml.KapuaAppPropertiesXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Kapua application property definition.
 *
 * @since 1.0
 */
@XmlJavaTypeAdapter(KapuaAppPropertiesXmlAdapter.class)
public interface KapuaAppProperties {

    /**
     * Get the property value
     *
     * @return
     */
    String getValue();
}
