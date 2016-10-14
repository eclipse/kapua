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
package org.eclipse.kapua.service.generator.id.random.mysql;

import java.math.BigInteger;

import javax.persistence.Query;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.util.KapuaCommonsErrorCodes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.generator.id.IdGeneratorService;

/**
 * Id generator service implementation.<br>
 * This implementation returns a random sequence through a MySql database call.
 * 
 * @since 1.0
 *
 */
public class IdGeneratorServiceImpl implements IdGeneratorService
{
    private static final String QUERY_SELECT_UUID_SHORT = "SELECT UUID_SHORT() FROM DUAL";

    @Override
    public KapuaId generate()
        throws KapuaException
    {
        KapuaEid id = null;
        EntityManager em = null;
        try {
            em = CommonsEntityManagerFactory.getEntityManager();
            Query q = em.createNativeQuery(QUERY_SELECT_UUID_SHORT);
            BigInteger bi = (BigInteger) q.getSingleResult();

            id = new KapuaEid(bi);
        }
        catch (Exception pe) {
            throw new KapuaRuntimeException(KapuaCommonsErrorCodes.ID_GENERATION_ERROR, pe);
        }
        finally {
            if (em != null) {
                em.close();
            }
        }

        return id;
    }

}
