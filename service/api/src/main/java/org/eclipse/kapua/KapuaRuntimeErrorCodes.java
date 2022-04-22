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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;

import java.util.Properties;

/**
 * {@link KapuaErrorCode}s for {@link KapuaRuntimeException}.
 *
 * @since 1.0.0
 */
public enum KapuaRuntimeErrorCodes implements KapuaErrorCode {
    /**
     * Unavailable service locator
     *
     * @since 1.0.0
     */
    SERVICE_LOCATOR_UNAVAILABLE,
    /**
     * Service operation non supported
     *
     * @since 1.0.0
     */
    SERVICE_OPERATION_NOT_SUPPORTED,

    /**
     * Translator not found
     *
     * @since 1.0.0
     * @deprecated Since 1.2.0. Moved error to more appropriate {@code kapua-translator-api} module.
     */
    @Deprecated
    TRANSLATOR_NOT_FOUND,

    /**
     * Unavailable entity creator factory
     *
     * @since 1.0.0
     */
    ENTITY_CREATOR_FACTORY_UNAVAILABLE,
    /**
     * Unavailable entity creator
     *
     * @since 1.0.0
     */
    ENTITY_CREATOR_UNAVAILABLE,
    /**
     * Entity creation error
     *
     * @since 1.0.0
     */
    ENTITY_CREATION_ERROR,

    /**
     * Error while invoking {@link org.eclipse.kapua.model.KapuaEntityFactory#clone(KapuaEntity)}
     *
     * @since 1.1.0
     */
    ENTITY_CLONE_ERROR,

    /**
     * Error while reading {@link java.util.Properties} from {@link KapuaUpdatableEntity#getEntityAttributes()} or {@link KapuaUpdatableEntity#getEntityProperties()}
     *
     * @since 1.1.0
     */
    ENTITY_PROPERTIES_READ_ERROR,

    /**
     * Error while writing {@link java.util.Properties} to {@link KapuaUpdatableEntity#setEntityAttributes(Properties)} or {@link KapuaUpdatableEntity#setEntityProperties(Properties)}
     *
     * @since 1.1.0
     */
    ENTITY_PROPERTIES_WRITE_ERROR
}
