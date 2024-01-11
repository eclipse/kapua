/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.xml;

public interface XmlPropertyAdapted<T extends Enum<T>> {

    String getName();

    void setName(String name);

    boolean getArray();

    void setArray(boolean array);

    T getType();

    void setType(T type);

    boolean isEncrypted();

    void setEncrypted(boolean encrypted);

    String[] getValues();

    void setValues(String[] values);
}
