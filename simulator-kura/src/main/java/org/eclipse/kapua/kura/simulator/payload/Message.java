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
package org.eclipse.kapua.kura.simulator.payload;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.kapua.kura.simulator.topic.Topic;
import org.eclipse.kapua.kura.simulator.topic.Topic.Segment;
import org.eclipse.kapua.kura.simulator.util.Hex;

public class Message {

    private final Topic topic;
    private final byte[] payload;
    private final Map<String, String> topicContext;

    public Message(final Topic topic, final byte[] payload) {
        this(topic, payload, Collections.emptyMap());
    }

    public Message(final Topic topic, final byte[] payload, final Map<String, String> topicContext) {
        this.topic = topic;
        this.payload = payload;
        this.topicContext = topicContext;
    }

    public Topic getTopic() {
        return topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public Message localize(final Topic topic) {
        return localize(topic, topicContext);
    }

    public Message localize(final Topic topic, final Map<String, String> topicContext) {
        final LinkedList<Segment> newTopic = new LinkedList<>(this.topic.getSegments());

        for (final Segment seg : topic.getSegments()) {
            final String segValue1 = this.topic.renderSegment(newTopic.removeFirst(), topicContext);
            final String segValue2 = topic.renderSegment(seg, topicContext);

            if (!segValue1.equals(segValue2)) {
                return null;
            }
        }

        return new Message(Topic.from(newTopic), payload);
    }

    @Override
    public String toString() {
        return String.format("[%s -> %s]", topic, Hex.toHex(payload, 256));
    }
}
