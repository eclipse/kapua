package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;

/**
 * Abstract Kapua service.<br>
 * It handles the {@link EntityManagerFactory} and {@link EntityManagerSession} to avoid to redefine each time in the subclasses.
 * 
 * @since 1.0
 *
 */
public class AbstractKapuaService
{
    
    protected EntityManagerSession entityManagerSession;
    protected EntityManagerFactory entityManagerFactory;

    /**
     * Constructor
     * 
     * @param entityManagerFactory
     */
    protected AbstractKapuaService(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        entityManagerSession = new EntityManagerSession(entityManagerFactory);
    }
}
