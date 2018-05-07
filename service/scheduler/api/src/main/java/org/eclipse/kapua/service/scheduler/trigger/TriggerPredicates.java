/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaNamedEntityPredicates;

public interface TriggerPredicates extends KapuaNamedEntityPredicates {

    String TRIGGER_PROPERTIES = "triggerProperties";

    String TRIGGER_PROPERTIES_NAME = TRIGGER_PROPERTIES + ".name";

    String TRIGGER_PROPERTIES_VALUE = TRIGGER_PROPERTIES + ".propertyValue";

    String TRIGGER_PROPERTIES_TYPE = TRIGGER_PROPERTIES + ".propertyType";

    String TRIGGER_NAME = "name";

    String STARTS_ON = "startsOn";

    String ENDS_ON = "endsOn";
}
