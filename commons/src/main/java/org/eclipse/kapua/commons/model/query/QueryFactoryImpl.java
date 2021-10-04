/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.QueryFactory;

/**
 * {@link QueryFactory} implementation.
 *
 * @since 1.6.0
 */
@KapuaProvider
public class QueryFactoryImpl implements QueryFactory {

    @Override
    public KapuaQuery newQuery() {
        return new AbstractKapuaQuery() {
        };
    }
}
