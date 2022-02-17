/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.xml;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.account.Account;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Account#getParentAccountPath()} {@link XmlAdapter}.
 *
 * @since 1.6.0
 */
public class AccountParentPathXmlAdapter extends XmlAdapter<String, String> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaIdFactory KAPUA_ID_FACTORY = LOCATOR.getFactory(KapuaIdFactory.class);

    @Override
    public String marshal(String parentAccountPathLong) {
        if (Strings.isNullOrEmpty(parentAccountPathLong)) {
            return null;
        } else {
            String[] parentAccountPathLongTokens = parentAccountPathLong.substring(1).split("/");

            List<String> parentAccountPathBase64List =
                    Arrays.stream(parentAccountPathLongTokens)
                            .map(p -> KAPUA_ID_FACTORY.newKapuaId(new BigInteger(p)).toCompactId())
                            .collect(Collectors.toList());

            return "/" + String.join("/", parentAccountPathBase64List);
        }
    }

    @Override
    public String unmarshal(String parentAccountPathBase64) throws KapuaIllegalArgumentException {
        if (Strings.isNullOrEmpty(parentAccountPathBase64)) {
            return null;
        } else {
            String[] parentAccountPathBase64Tokens = parentAccountPathBase64.substring(1).split("/");

            try {
                List<String> parentAccountPathLongList =
                        Arrays.stream(parentAccountPathBase64Tokens)
                                .map(p -> KAPUA_ID_FACTORY.newKapuaId(p).toStringId())
                                .collect(Collectors.toList());

                return "/" + String.join("/", parentAccountPathLongList);
            } catch (IllegalArgumentException e) {
                throw new KapuaIllegalArgumentException("account.parentAccountPath", parentAccountPathBase64);
            }
        }
    }
}
