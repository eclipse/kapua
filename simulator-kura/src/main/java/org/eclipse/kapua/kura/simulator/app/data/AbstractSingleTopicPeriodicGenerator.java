/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
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
package org.eclipse.kapua.kura.simulator.app.data;

import java.time.Instant;

import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.generator.GeneratorScheduler;
import org.eclipse.kapua.kura.simulator.topic.Topic;

public abstract class AbstractSingleTopicPeriodicGenerator implements Handler {

    private final GeneratorScheduler.Handle handle;
    private final ApplicationContext context;
    private final Topic topic;

    public AbstractSingleTopicPeriodicGenerator(final ApplicationContext context, final GeneratorScheduler scheduler, final String dataTopic) {
        this.context = context;
        this.handle = scheduler.add(this::tick);
        this.topic = Topic.data(dataTopic);
    }

    @Override
    public void close() throws Exception {
        this.handle.remove();
    }

    protected void tick(final Instant timestamp) {
        update(timestamp, this.context.sender(this.topic));
    }

    protected abstract void update(final Instant timestamp, final Sender sender);
}
