/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

import java.util.Map;

import org.eclipse.kapua.service.commons.PropertyMapper;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

class SpringPropertyMapper implements PropertyMapper {

    public <T> T convert(Map<String, String> aSource, String aPrefix, Class<T> claz) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(aSource);
        Binder binder = new Binder(source);

        BindResult<T> res = binder.bind("", claz);
        return res.get();
    }
}
