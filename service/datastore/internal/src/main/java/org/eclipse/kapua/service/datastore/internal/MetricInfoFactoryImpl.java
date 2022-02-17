/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/**
 * {@link MetricInfoFactory} implementation.
 *
 * @since 1.3.0
 */
@KapuaProvider
public class MetricInfoFactoryImpl implements MetricInfoFactory {

    @Override
    public MetricInfo newStorable() {
        return new MetricInfoImpl();
    }

    @Override
    public MetricInfoListResult newListResult() {
        return new MetricInfoListResultImpl();
    }

    @Override
    public MetricInfoQuery newQuery(KapuaId scopeId) {
        return new MetricInfoQueryImpl(scopeId);
    }
}
