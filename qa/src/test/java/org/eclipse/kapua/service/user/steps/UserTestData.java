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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import javax.inject.Singleton;

import org.eclipse.kapua.service.account.Account;

@Singleton
public class UserTestData {

    public String userName;

    /**
     * One of two users used in tests - A
     */
    public ComparableUser userA = null;

    /**
     * One of two users used in tests - B
     */
    public ComparableUser userB = null;

    /**
     * Account that was created by last Account creation step.
     */
    public Account lastAccount;

    /**
     * User that was created by last User creation step.
     */
    public ComparableUser lastUser;

    public void clearData() {
        userA = null;
        userB = null;
        lastAccount = null;
        lastUser = null;
    }
}
