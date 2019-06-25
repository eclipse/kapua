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
package org.eclipse.kapua.service.scheduler.trigger.definition;

import org.eclipse.kapua.model.KapuaNamedEntityAttributes;

/**
 * {@link TriggerDefinitionAttributes} attributes.
 *
 * @see org.eclipse.kapua.model.KapuaEntityAttributes
 * @since 1.1.0
 */
public class TriggerDefinitionAttributes extends KapuaNamedEntityAttributes {

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_TYPE = "triggerType";

    /**
     * @since 1.1.0
     */
    public static final String PROCESSOR_NAME = "processorName";

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_PROPERTIES = "triggerProperties";

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_PROPERTIES_NAME = TRIGGER_PROPERTIES + ".name";

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_PROPERTIES_TYPE = TRIGGER_PROPERTIES + ".type";

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_PROPERTIES_VALUE = TRIGGER_PROPERTIES + ".value";

}
