/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
