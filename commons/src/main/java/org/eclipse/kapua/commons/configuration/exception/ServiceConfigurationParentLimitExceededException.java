/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration.exception;

import org.eclipse.kapua.commons.configuration.ResourceLimitedServiceConfigurationManagerImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link KapuaConfigurationException} to {@code throw} when an update of a {@link ResourceLimitedServiceConfigurationManagerImpl}
 * {@code maxNumberChildEntities} property exceeds available resources of the parent scope.
 *
 * @since 2.0.0
 */
public class ServiceConfigurationParentLimitExceededException extends KapuaConfigurationException {

    private final String servicePid;
    private final KapuaId parentScopeId;
    private final long limitExceededBy;

    /**
     * Constructor.
     *
     * @param servicePid      The {@link KapuaConfigurableService} pid.
     * @param parentScopeId   The parent scope {@link KapuaId} for which limit has been exceeded.
     * @param limitExceededBy The amount of exceed.
     * @since 2.0.0
     */
    public ServiceConfigurationParentLimitExceededException(String servicePid, KapuaId parentScopeId, long limitExceededBy) {
        super(limitExceededBy < 1000000 ? KapuaConfigurationErrorCodes.PARENT_LIMIT_EXCEEDED_BY : KapuaConfigurationErrorCodes.PARENT_LIMIT_EXCEEDED, servicePid, parentScopeId, limitExceededBy);

        this.servicePid = servicePid;
        this.parentScopeId = parentScopeId;
        this.limitExceededBy = limitExceededBy;
    }

    /**
     * Gets the {@link KapuaConfigurableService} pid.
     *
     * @return he {@link KapuaConfigurableService} pid.
     * @since 2.0.0
     */
    public String getServicePid() {
        return servicePid;
    }

    /**
     * Gets the parent scope {@link KapuaId} for which limit has been exceeded.
     *
     * @return The parent scope {@link KapuaId} for which limit has been exceeded.
     * @since 2.0.0
     */
    public KapuaId getScopeId() {
        return parentScopeId;
    }

    /**
     * Gets the amount of exceed.
     *
     * @return the amount of exceed.
     * @since 2.0.0
     */
    public long getLimitExceededBy() {
        return limitExceededBy;
    }
}
