/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.storage.TxContext;

public interface DeviceValidation {
    void validateCreatePreconditions(DeviceCreator deviceCreator) throws KapuaException;

    void validateUpdatePreconditions(TxContext txContext, Device device) throws KapuaException;

    void validateFindPreconditions(TxContext txContext, KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    void validateQueryPreconditions(KapuaQuery query) throws KapuaException;

    void validateCountPreconditions(KapuaQuery query) throws KapuaException;

    void validateDeletePreconditions(TxContext txContext, KapuaId scopeId, KapuaId deviceId) throws KapuaException;

    void validateFindByClientIdPreconditions(KapuaId scopeId, String clientId) throws KapuaException;
}
