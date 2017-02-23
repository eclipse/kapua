/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
