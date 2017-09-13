/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

public interface TriggerPredicates extends KapuaUpdatableEntityPredicates {

    /**
     * The {@link Trigger} name.
     */
    public static final String NAME = "name";

    public static final String TRIGGER_PROPERTIES = "triggerProperties";

    public static final String TRIGGER_PROPERTIES_NAME = TRIGGER_PROPERTIES + ".name";

    public static final String TRIGGER_PROPERTIES_VALUE = TRIGGER_PROPERTIES + ".propertyValue";

    public static final String TRIGGER_PROPERTIES_TYPE = TRIGGER_PROPERTIES + ".propertyType";
}
