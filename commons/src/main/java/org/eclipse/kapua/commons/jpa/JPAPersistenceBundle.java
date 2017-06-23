/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.ServiceBundle;
import org.eclipse.kapua.locator.inject.MultiService;
import org.eclipse.kapua.commons.core.ComponentProvider;

@ComponentProvider
@MultiService(provides=ServiceBundle.class)
public class JPAPersistenceBundle implements ServiceBundle {

    @Inject CommonsEntityManagerFactoryImpl commonsEntityManagerFactory;
    
    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
