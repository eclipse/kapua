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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.generator.id.sequence;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.generator.id.IdGeneratorService;

/**
 * Id generator service implementation.<br>
 * This implementation return a sequence.
 * 
 * @since 1.0
 *
 */
public class IdGeneratorServiceImpl implements IdGeneratorService
{
    private static AtomicLong seed = new AtomicLong(System.currentTimeMillis());

    @Override
    public KapuaId generate()
        throws KapuaException
    {
        return new KapuaEid(BigInteger.valueOf(seed.incrementAndGet()));
    }
}
