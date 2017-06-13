/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.locator.guice.service.internal;

import java.math.BigInteger;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

@KapuaProvider
public class FactoryAImpl implements FactoryA {

    @Override
    public KapuaId newKapuaId(String shortId) {
        return null;
    }

    @Override
    public KapuaId newKapuaId(BigInteger bigInteger) {
        return null;
    }

}
