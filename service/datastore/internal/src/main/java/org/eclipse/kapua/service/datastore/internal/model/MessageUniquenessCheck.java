/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal.model;

/**
 * Once a message is going to be stored to datastore the store call can terminate with error on client side (due to timeout for example) but performed on server side.
 * The store call is then retried and the message could be inserted twice.
 * To avoid that, the current implementation does a query looking for a message with a specific id (the one from the message) in all the indexes belonging to the account.
 * This is safer since changes in the message indexing by or the settings of the datastore (index by week/day/hour) can affect the index where the message should be stored to and then the effectivness of the check.
 * But this has a performance drawback. The number of queries to be performed are linear with the indexes available so, if there are a lot of indexes, the query will need more time and resources to be executed.
 * This enum define a new parameter to change the search behavior by account.
 */
public enum MessageUniquenessCheck {

    /**
     * No check
     */
    NONE,
    /**
     * The search is done only to the index where the message is expected to be, based on current configuration.
     */
    BOUND,
    /**
     * Will check in all the indexes defined for the account
     */
    FULL
}