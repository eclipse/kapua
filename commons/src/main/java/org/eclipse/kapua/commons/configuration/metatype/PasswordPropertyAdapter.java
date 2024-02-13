/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.model.xml.XmlPropertyAdapted;
import org.eclipse.kapua.model.xml.adapters.ClassBasedXmlPropertyAdapterBase;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PasswordPropertyAdapter extends ClassBasedXmlPropertyAdapterBase<Password> {
    private final CryptoUtil cryptoUtil;

    public PasswordPropertyAdapter(CryptoUtil cryptoUtil) {
        super(Password.class);
        this.cryptoUtil = cryptoUtil;
    }

    @Override
    public boolean canMarshall(Class objectClass) {
        return Password.class.equals(objectClass);
    }

    @Override
    public boolean doesEncrypt() {
        return true;
    }

    @Override
    public String marshallValue(Object value) {
        return cryptoUtil.encodeBase64(value.toString());
    }

    @Override
    public Password unmarshallValue(String value) {
        return new Password(cryptoUtil.decodeBase64(value));
    }

    @Override
    public Object unmarshallValues(XmlPropertyAdapted<?> property) {
        if (!property.getArray()) {
            return property.isEncrypted() ? unmarshallValue(property.getValues()[0]) : new Password(property.getValues()[0]);
        } else {
            return Arrays
                    .stream(property.getValues())
                    .map(value -> property.isEncrypted() ? unmarshallValue(value) : new Password(value))
                    .collect(Collectors.toList()).toArray();
        }
    }
}
