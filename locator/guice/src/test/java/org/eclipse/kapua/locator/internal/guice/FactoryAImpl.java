/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.locator.internal.guice;

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
