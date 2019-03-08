/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.commons.model.id.KapuaEid;

import java.math.BigInteger;

public class CollisionEntityCreator extends AbstractKapuaNamedEntityCreator<CollisionEntity> {

    private static final long serialVersionUID = -6079623075798561334L;

    private String testField;

    public CollisionEntityCreator(String testField) {
        super(new KapuaEid(new BigInteger("1")), "collisionEntityCreator");
        this.testField = testField;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }

}
