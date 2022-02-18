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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.service.account.Account;

/**
 * Message information object.<br>
 * It is used as a container for the user dependent information during the store process (typically {@link Account} informations)
 *
 * @since 1.0
 */
public class MessageInfo {

    private Account account;

    /**
     * Construct a new message information using the given account
     *
     * @param account
     */
    public MessageInfo(Account account) {
        this.account = account;
    }

    /**
     * Get the account
     *
     * @return
     */
    public Account getAccount() {
        return this.account;
    }
}
