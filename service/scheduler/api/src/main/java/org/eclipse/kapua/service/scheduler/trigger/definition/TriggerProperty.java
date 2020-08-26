/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.definition;

/**
 * {@link TriggerProperty} definition.
 *
 * @since 1.0.0
 */
public interface TriggerProperty {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the property type.
     *
     * @return The property type.
     * @since 1.0.0
     */
    String getPropertyType();

    /**
     * Sets the property type.
     *
     * @param propertyType The property type.
     * @since 1.0.0
     */
    void setPropertyType(String propertyType);

    /**
     * Sets the property value.
     *
     * @return The property value.
     * @since 1.0.0
     */
    String getPropertyValue();

    /**
     * Sets the property value.
     *
     * @param propertyValue The property value.
     * @since 1.0.0
     */
    void setPropertyValue(String propertyValue);

}
