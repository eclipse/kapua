/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
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

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;

@Entity(name = "CollisionEntity")
@Table(name = "collision_entity_test")
public class CollisionEntity extends AbstractKapuaNamedEntity {

    private static final long serialVersionUID = -4404075555024329043L;

    private static CollisionIdGenerator collisionIdGenerator;

    public static final String TYPE = "collisionEntity";

    @Basic
    @Column(name = "test_field")
    private String testField;

    public String getType() {
        return TYPE;
    }

    public static void initializeCollisionIdGenerator(CollisionIdGenerator collisionIdGenerator) {
        CollisionEntity.collisionIdGenerator = collisionIdGenerator;
    }

    public CollisionEntity() {
    }

    public CollisionEntity(String testField) {
        this.testField = testField;
    }

    /**
     * Before update action to correctly set the modified on and modified by fields
     * 
     * @throws KapuaException
     */
    @PrePersist
    @Override
    protected void prePersistsAction() {
        setId(new KapuaEid(collisionIdGenerator.generate()));

        setCreatedBy(new KapuaEid(BigInteger.ONE));
        setCreatedOn(new Date());
    }

    public String getTestField() {
        return testField;
    }

}