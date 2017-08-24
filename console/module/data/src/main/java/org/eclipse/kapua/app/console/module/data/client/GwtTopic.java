/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseTreeModel;

public class GwtTopic extends KapuaBaseTreeModel implements Serializable {

    private static final long serialVersionUID = 7519496938895060911L;

    public GwtTopic() {
        super();
    }

    public GwtTopic(String topicName, String baseTopic, String semanticTopic, Date timestamp) {
        this();
        set("topicName", topicName);
        set("baseTopic", baseTopic);
        set("semanticTopic", semanticTopic);
        set("timestamp", timestamp);

        if (semanticTopic.split("/").length > 6) {
            int charIndex = 0;
            short slashCount = 0;
            String exceededSemanticTopic = "";
            for (char c : semanticTopic.toCharArray()) {
                if (c == '/') {
                    slashCount++;
                }

                if (slashCount == 5) {
                    exceededSemanticTopic = semanticTopic.substring(charIndex + 1);
                    break;
                }

                charIndex++;
            }

            set("topicNameLimited", exceededSemanticTopic);
        } else {
            set("topicNameLimited", topicName);
        }
    }

    public GwtTopic(String topicName, String baseTopic, String semanticTopic, Date timestamp, KapuaBaseTreeModel[] children) {
        this(topicName, baseTopic, semanticTopic, timestamp);
        for (int i = 0; i < children.length; i++) {
            add(children[i]);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) DateUtils.formatDateTime(getTimestamp());
        } else {
            return super.get(property);
        }
    }

    public String getTopicName() {
        return (String) get("topicName");
    }

    public String getBaseTopic() {
        return (String) get("baseTopic");
    }

    public String getSemanticTopic() {
        return (String) get("semanticTopic");
    }

    public String getUnescapedSemanticTopic() {
        return (String) getUnescaped("semanticTopic");
    }

    public void setTimestamp(Date timestamp) {
        set("timestamp", timestamp);
    }

    public Date getTimestamp() {
        return (Date) get("timestamp");
    }

    public String getTimestampFormatted() {
        return (String) get("timestampFormatted");
    }
}
