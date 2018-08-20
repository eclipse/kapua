/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.KapuaNamedEntityAttributes;

public class AccountAttributes extends KapuaNamedEntityAttributes {

    public static final String PARENT_ACCOUNT_PATH = "parentAccountPath";

    public static final String CHILD_ACCOUNTS = "childAccounts";

    public static final String ORGANIZATION = "organization";

    public static final String ORGANIZATION_NAME = ORGANIZATION + ".name";

    public static final String ORGANIZATION_EMAIL = ORGANIZATION + ".email";
}
