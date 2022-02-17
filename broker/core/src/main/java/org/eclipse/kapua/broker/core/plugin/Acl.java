/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

/**
 * Acl
 *
 * @since 1.0
 */
public class Acl {

    public static final Acl ALL = new Acl(true, true, true);
    public static final Acl READ = new Acl(true, false, false);
    public static final Acl WRITE = new Acl(false, true, false);
    public static final Acl ADMIN = new Acl(false, false, true);
    public static final Acl WRITE_ADMIN = new Acl(false, true, true);
    public static final Acl READ_ADMIN = new Acl(true, false, true);

    private boolean readPermission;
    private boolean writePermission;
    private boolean adminPermission;

    protected Acl(boolean readPermission, boolean writePermission, boolean adminPermission) {
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.adminPermission = adminPermission;
    }

    /**
     * Has read rights
     *
     * @return
     */
    public boolean isRead() {
        return readPermission;
    }

    /**
     * Has write rights
     *
     * @return
     */
    public boolean isWrite() {
        return writePermission;
    }

    /**
     * Has admin rights
     *
     * @return
     */
    public boolean isAdmin() {
        return adminPermission;
    }

}
