/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.data.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseTreeModel;

import java.io.Serializable;
import java.util.Date;

public class GwtTopic extends KapuaBaseTreeModel implements Serializable, IsSerializable {

    private static final long serialVersionUID = 7519496938895060911L;

    static final Date NO_TIMESTAMP = new Date(0);

    private static final String SEMANTIC_TOPIC = "semanticTopic";
    private static final String TIMESTAMP = "timestamp";

    public GwtTopic() {
        super();
    }

    public GwtTopic(String topicName, String baseTopic, String semanticTopic, Date timestamp) {
        this();
        set("topicName", topicName);
        set("baseTopic", baseTopic);
        set(SEMANTIC_TOPIC, semanticTopic);
        set(TIMESTAMP, timestamp);

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
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getTimestamp()));
        } else {
            return super.get(property);
        }
    }

    public String getTopicName() {
        return get("topicName");
    }

    public String getBaseTopic() {
        return get("baseTopic");
    }

    public String getSemanticTopic() {
        return get(SEMANTIC_TOPIC);
    }

    public String getUnescapedSemanticTopic() {
        return getUnescaped(SEMANTIC_TOPIC);
    }

    public void setTimestamp(Date timestamp) {
        set(TIMESTAMP, timestamp);
    }

    public Date getTimestamp() {
        return get(TIMESTAMP);
    }

    public String getTimestampFormatted() {
        return get("timestampFormatted");
    }

    public String[] getTopicFragments() {
        return ((String) get(SEMANTIC_TOPIC)).split("/");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GwtTopic) {
            return getSemanticTopic().equals(((GwtTopic) obj).getSemanticTopic());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
