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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractMessageStoreServiceTest extends KapuaTest {


    public static final String DEFAULT_COMMONS_PATH = "../../../commons";

    @BeforeClass
    public static void tearUpGlobal()
            throws KapuaException {
//        KapuaConfigurableServiceSchemaUtils.createSchemaObjects(DEFAULT_COMMONS_PATH);
        XmlUtil.setContextProvider(new DatastoreJAXBContextProvider());
    }

    @AfterClass
    public static void tearDownGlobal() {
//        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
    }
}
