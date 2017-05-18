/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.event;

import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;

public interface KapuaEvent {

    public enum OperationStatus {
        OK,
        FAIL
    }

    public String getContextId();
    public Date getTimestamp();
    public KapuaId getUserId();
    public String getService();
    public String getEntityType();
    public KapuaId getEntityId();
    public String getOperation();
    public OperationStatus getOperationStatus();
    public String getFailureMessage();
    public String getInputs();
    public String getOutputs();
    public String getNote();
}
