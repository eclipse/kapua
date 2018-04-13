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

import org.eclipse.kapua.kura.simulator.Module;
import org.eclipse.kapua.kura.simulator.Transport;
import org.eclipse.kapua.kura.simulator.payload.Message;
import org.eclipse.kapua.kura.simulator.topic.Topic;
import org.eclipse.kapua.kura.simulator.topic.Topic.Segment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationController implements Module {

    private final Map<String, Entry> applications = new HashMap<>();
    private final Transport transport;

    private static class Entry {

        private final String id;
        private final Handler handler;

        public Entry(final String id, final ApplicationContext context, final Handler handler) {
            this.id = id;
            this.handler = handler;
        }

        public String getId() {
            return this.id;
        }

        public Handler getHandler() {
            return this.handler;
        }
    }

    public ApplicationController(final Transport transport) {
        this.transport = transport;
    }

    public ApplicationController(final Transport transport, final Collection<Application> applications) {
        this.transport = transport;
        applications.forEach(this::add);
    }

    public void add(final Application application) {
        final Descriptor desc = application.getDescriptor();
        final String id = desc.getId();

        remove(application);

        final ApplicationContext context = new ApplicationContext() {

            @Override
            public Sender sender(final Topic topic) {
                topic.attach("application-id", id);
                return Sender.transportSender(topic, ApplicationController.this.transport);
            }
        };

        final Handler handler = application.createHandler(context);

        final Entry entry = new Entry(id, context, handler);
        this.applications.put(id, entry);

        subscribeEntry(entry);
    }

    public void remove(final Application application) {
        final String id = application.getDescriptor().getId();
        final Entry entry = this.applications.remove(id);
        if (entry == null) {
            return;
        }

        this.transport.unsubscribe(Topic.application(id).append(Segment.wildcard()));
    }

    private void subscribeEntry(final Entry entry) {
        this.transport.subscribe(Topic.application(entry.getId()).append(Segment.wildcard()), msg -> {
            // try to cut off application id prefix
            final Message message = msg.localize(Topic.application(entry.getId()));
            if (message != null) {
                // matches our pattern
                entry.getHandler().processMessage(message);
            }
        });
    }

    @Override
    public void connected(final Transport transport) {
        for (final Entry entry : this.applications.values()) {
            entry.getHandler().connected();
            subscribeEntry(entry);
        }
    }

    @Override
    public void disconnected(final Transport transport) {
        for (final Entry entry : this.applications.values()) {
            entry.getHandler().disconnected();
        }
    }

    public Set<String> getApplicationIds() {
        return Collections.unmodifiableSet(this.applications.keySet());
    }

}
