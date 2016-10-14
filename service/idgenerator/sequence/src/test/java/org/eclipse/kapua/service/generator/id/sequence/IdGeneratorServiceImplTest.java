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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.generator.id.IdGeneratorService;
import org.junit.Assert;
import org.junit.Test;

public class IdGeneratorServiceImplTest extends Assert
{
    @Test
    public void testIdGeneration()
        throws Exception
    {
        KapuaLocator locator = KapuaLocator.getInstance();
        IdGeneratorService idGeneratorService = locator.getService(IdGeneratorService.class);

        KapuaId id = idGeneratorService.generate();

        assertNotNull(id);
        assertNotNull(id.getShortId());
        assertTrue(!id.getShortId().isEmpty());
        assertNotNull(id.getId());
    }

    @Test
    public void testBulkIdGeneration()
        throws Exception
    {
        KapuaLocator locator = KapuaLocator.getInstance();
        IdGeneratorService idGeneratorService = locator.getService(IdGeneratorService.class);

        Set<KapuaId> generatedIds = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            KapuaId id = idGeneratorService.generate();
            assertFalse(generatedIds.contains(id));
            generatedIds.add(id);
        }
    }

}
