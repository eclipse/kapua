/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @since 1.2.0
 */
public abstract class AbstractJpaJbatchEntity implements Serializable {

    protected <T> T readObject(byte[] byteObject) {
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(byteObject);
                ObjectInputStream ios = new ObjectInputStream(bais)
        ) {
            return (T) ios.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid byte[]. Size: " + byteObject.length, e);
        }
    }

    protected byte[] writeObject(Serializable serializableObject) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)
        ) {
            oos.writeObject(serializableObject);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid object. Object: " + serializableObject, e);
        }
    }
}
