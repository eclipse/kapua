/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * Base class for JPA counterpart of jBatch entities.
 * <p>
 * It offers {@link #readObject(byte[])} and {@link #writeObject(Serializable)} as utility method since most of the jBatch entities on the default {@link com.ibm.jbatch.container.services.impl.JDBCPersistenceManagerImpl}
 * contains binary objects. This is odd but current deployment have that format and, for backward compatibility we need to keep this format.
 * <p>
 * Further improvement could be use the Apache Lang 3 {@link org.apache.commons.lang3.SerializationUtils#serialize(Serializable)} and {@link org.apache.commons.lang3.SerializationUtils#deserialize(byte[])},
 * but compatibility needs to be checked.
 *
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
