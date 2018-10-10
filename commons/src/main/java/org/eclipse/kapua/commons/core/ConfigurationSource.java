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
package org.eclipse.kapua.commons.core;

import java.util.List;

/**
 * Represents a single configuration source (e.g. a JSON or a property file).
 *
 */
public interface ConfigurationSource {

    public List<String> getKeys();

    public String getString(String key);

    public String getString(String key, String defaultValue);

    public Integer getInteger(String key);

    public Integer getInteger(String key, Integer defaultValue);

    public Long getLong(String key);

    public Long getLong(String key, Long defaultValue);
}
