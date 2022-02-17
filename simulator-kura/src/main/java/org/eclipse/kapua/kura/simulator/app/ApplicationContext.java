/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app;

import org.eclipse.kapua.kura.simulator.topic.Topic;

public interface ApplicationContext {

    /**
     * Get a sender for any topic
     * <p>
     * The topic may contain the placeholder {@code application-id}, which will be replaced with
     * the application ID of the application this context belongs to.
     * </p>
     *
     * @param topic
     *            The fully qualified topic, the topic may be un-expanded
     * @return a new sender, never {@code null}
     */
    public Sender sender(Topic topic);
}
