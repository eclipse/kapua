/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerStatus;

import java.util.Date;

/**
 * {@link FiredTriggerCreator} implementation.
 *
 * @since 1.5.0
 */
public class FiredTriggerCreatorImpl extends AbstractKapuaEntityCreator<FiredTrigger> implements FiredTriggerCreator {

    private KapuaId triggerId;
    private Date firedOn;
    private FiredTriggerStatus status;
    private String message;

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.5.0
     */
    public FiredTriggerCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getTriggerId() {
        return triggerId;
    }

    @Override
    public void setTriggerId(KapuaId triggerId) {
        this.triggerId = triggerId;
    }

    @Override
    public Date getFiredOn() {
        return firedOn;
    }

    @Override
    public void setFiredOn(Date firedOn) {
        this.firedOn = firedOn;
    }

    @Override
    public FiredTriggerStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(FiredTriggerStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
