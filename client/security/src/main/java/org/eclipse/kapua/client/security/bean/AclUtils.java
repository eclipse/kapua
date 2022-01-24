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
package org.eclipse.kapua.client.security.bean;

import org.eclipse.kapua.client.security.bean.AuthAcl.Action;

public class AclUtils {

    private AclUtils() {
    }

    public static boolean isRead(Action action) {
        return Action.all.equals(action) || Action.read.equals(action) || Action.readAdmin.equals(action);
    }

    public static boolean isWrite(Action action) {
        return Action.all.equals(action) || Action.write.equals(action) || Action.writeAdmin.equals(action);
    }

    public static boolean isAdmin(Action action) {
        return Action.all.equals(action) || Action.admin.equals(action) || Action.readAdmin.equals(action) || Action.writeAdmin.equals(action);
    }

}
