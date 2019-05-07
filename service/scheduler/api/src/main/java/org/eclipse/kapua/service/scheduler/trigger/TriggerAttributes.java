/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

    public static final String STARTS_ON = "startsOn";

    public static final String ENDS_ON = "endsOn";

    public static final String TRIGGER_DEFINITION_ID = "triggerDefinitionId";

    public static final String TRIGGER_PROPERTIES = "triggerProperties";

    public static final String TRIGGER_PROPERTIES_NAME = TRIGGER_PROPERTIES + ".name";

    public static final String TRIGGER_PROPERTIES_VALUE = TRIGGER_PROPERTIES + ".propertyValue";

    public static final String TRIGGER_PROPERTIES_TYPE = TRIGGER_PROPERTIES + ".propertyType";

}
