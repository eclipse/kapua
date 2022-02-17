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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import java.math.BigInteger;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;

/**
 * Kapua identifier factory reference implementation.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class KapuaIdFactoryImpl implements KapuaIdFactory {

    @Override
    public KapuaId newKapuaId(String shortId) {
        return KapuaEid.parseCompactId(shortId);
    }

    @Override
    public KapuaId newKapuaId(BigInteger bigInteger) {
        return new KapuaEid(bigInteger);
    }
}
