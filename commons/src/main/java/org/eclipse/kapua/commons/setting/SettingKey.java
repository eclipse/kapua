/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

/**
 * {@link SettingKey} definition.
 * <p>
 * This is to be implemented as a {@link Enum} to contain all Settings value available for a given {@link AbstractBaseKapuaSetting}
 *
 * @since 1.0.0
 */
public interface SettingKey {

    /**
     * Gets the {@link SettingKey} value.
     *
     * @return The {@link SettingKey} value.
     * @since 1.0.0
     */
    public String key();
}
