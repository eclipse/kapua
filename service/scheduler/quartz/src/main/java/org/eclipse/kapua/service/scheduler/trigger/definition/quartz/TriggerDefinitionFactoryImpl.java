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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;

/**
 * {@link TriggerDefinitionFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class TriggerDefinitionFactoryImpl implements TriggerDefinitionFactory {

    @Override
    public TriggerDefinition newEntity(KapuaId scopeId) {
        return new TriggerDefinitionImpl(scopeId);
    }

    @Override
    public TriggerDefinitionCreator newCreator(KapuaId scopeId) {
        return new TriggerDefinitionCreatorImpl(scopeId);
    }

    @Override
    public TriggerDefinitionQuery newQuery(KapuaId scopeId) {
        return new TriggerDefinitionQueryImpl(scopeId);
    }

    @Override
    public TriggerDefinitionListResult newListResult() {
        return new TriggerDefinitionListResultImpl();
    }

    @Override
    public TriggerProperty newTriggerProperty(String name, String type, String value) {
        return new TriggerPropertyImpl(name, type, value);
    }

    @Override
    public TriggerDefinition clone(TriggerDefinition triggerDefinition) {
        try {
            return new TriggerDefinitionImpl(triggerDefinition);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, TriggerDefinition.TYPE, triggerDefinition);
        }
    }
}
