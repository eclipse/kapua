/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    private boolean read;
    private boolean write;
    private boolean admin;

    protected Acl(boolean read, boolean write, boolean admin) {
        this.read = read;
        this.write = write;
        this.admin = admin;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isWrite() {
        return write;
    }

    public boolean isAdmin() {
        return admin;
    }

}
