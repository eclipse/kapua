/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

public class Util {

    private Util() {
    }

    public static Actions parseAction(String value) {
        return value!=null ? Actions.valueOf(value) : null;
    }

    public static Set<Actions> parseActions(String action) {
        Set<Actions> actions = null;
        if (action!=null) {
            actions = new HashSet<>();
            for (String str : action.split(",")) {
                actions.add(parseAction(str));
            }
        }
        return actions;
    }

    public static BigInteger parseBigInteger(String value) {
        return value!=null ? BigInteger.valueOf(Long.parseLong(value)) : null;
    }

    public static Boolean parseBoolean(String value) {
        return value!=null ? Boolean.valueOf(value) : false;
    }

    public static UserStatus parseUserStatus(String value) {
        return value!=null ? UserStatus.valueOf(value) : null;
    }

    public static UserType parseUserType(String value) {
        return value!=null ? UserType.valueOf(value) : null;
    }

    public static Integer parseInteger(String value) {
        return value!=null ? Integer.valueOf(value) : null;
    }

    public static int parseInt(String value) {
        return value!=null ? Integer.valueOf(value) : 0;
    }

    public static KapuaId parseKapuaId(String value) {
        return value != null ? KapuaEid.parseCompactId(value) : null;
    }

    public static URI parseUri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
