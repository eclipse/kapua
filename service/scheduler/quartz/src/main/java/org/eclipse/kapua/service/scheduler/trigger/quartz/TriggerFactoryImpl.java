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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;

/**
 * Trigger service factory implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class TriggerFactoryImpl implements TriggerFactory {

    @Override
    public TriggerCreator newCreator(KapuaId scopeId) {
        return new TriggerCreatorImpl(scopeId, null);
    }

    @Override
    public Trigger newEntity(KapuaId scopeId) {
        return new TriggerImpl(scopeId);
    }

    @Override
    public TriggerQuery newQuery(KapuaId scopeId) {
        return new TriggerQueryImpl(scopeId);
    }

    @Override
    public TriggerListResult newListResult() {
        return new TriggerListResultImpl();
    }

    @Override
    public TriggerProperty newTriggerProperty(String name, String type, String value) {
        return new TriggerPropertyImpl(name, type, value);
    }
}
