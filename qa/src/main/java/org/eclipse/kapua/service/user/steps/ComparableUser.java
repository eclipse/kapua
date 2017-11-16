/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import org.eclipse.kapua.service.user.User;

/**
 * Wrapper around user to make User class comparable by its attributes.
 * It provides equals() method.
 */
public class ComparableUser {
    private User user;

    ComparableUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComparableUser) {
            ComparableUser other = (ComparableUser) obj;
            return compareAllUserAtt(user, other.getUser());
        } else {
            return false;
        }
    }

    private boolean compareAllUserAtt(User thisUser, User otherUser) {
        return thisUser.getName().equals(otherUser.getName()) &&
                thisUser.getDisplayName().equals(otherUser.getDisplayName()) &&
                thisUser.getEmail().equals(otherUser.getEmail()) &&
                thisUser.getPhoneNumber().equals(otherUser.getPhoneNumber()) &&
                thisUser.getStatus().equals(otherUser.getStatus());
    }
}