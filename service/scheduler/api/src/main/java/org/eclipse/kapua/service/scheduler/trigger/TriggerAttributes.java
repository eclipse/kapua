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
package org.eclipse.kapua.service.scheduler.trigger;

import org.eclipse.kapua.model.KapuaNamedEntityAttributes;

/**
 * {@link TriggerAttributes} attributes.
 *
 * @see org.eclipse.kapua.model.KapuaEntityAttributes
 * @since 1.0.0
 */
public class TriggerAttributes extends KapuaNamedEntityAttributes {

    /**
     * @since 1.0.0
     */
    public static final String STARTS_ON = "startsOn";

    /**
     * @since 1.0.0
     */
    public static final String ENDS_ON = "endsOn";

    /**
     * @since 1.1.0
     */
    public static final String TRIGGER_DEFINITION_ID = "triggerDefinitionId";

    /**
     * @since 1.0.0
     */
    public static final String TRIGGER_PROPERTIES = "triggerProperties";

    /**
     * @since 1.0.0
     */
    public static final String TRIGGER_PROPERTIES_NAME = TRIGGER_PROPERTIES + ".name";

    /**
     * @since 1.0.0
     */
    public static final String TRIGGER_PROPERTIES_VALUE = TRIGGER_PROPERTIES + ".propertyValue";

    /**
     * @since 1.0.0
     */
    public static final String TRIGGER_PROPERTIES_TYPE = TRIGGER_PROPERTIES + ".propertyType";

}
