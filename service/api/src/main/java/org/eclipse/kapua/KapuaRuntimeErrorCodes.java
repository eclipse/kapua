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
package org.eclipse.kapua;

/**
 * Kapua runtime error codes
 * 
 * @since 1.0
 *
 */
public enum KapuaRuntimeErrorCodes implements KapuaErrorCode {
    /**
     * Unavailable service locator
     */
    SERVICE_LOCATOR_UNAVAILABLE,
    /**
     * Service operation non supported
     */
    SERVICE_OPERATION_NOT_SUPPORTED,

    /**
     * Translator not found
     */
    TRANSLATOR_NOT_FOUND,

    /**
     * Unavailable entity creator factory
     */
    ENTITY_CREATOR_FACTORY_UNAVAILABLE,
    /**
     * Unavailable entity creator
     */
    ENTITY_CREATOR_UNAVAILABLE,
    /**
     * Entity creation error
     */
    ENTITY_CREATION_ERROR,

}
