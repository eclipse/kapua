/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;

public class GwtMqttTopic implements Serializable {

    private static final long serialVersionUID = -6373321036750058349L;

    private String fullTopic;

    public GwtMqttTopic() {
    }

    public GwtMqttTopic(String topic) {
        this.setFullTopic(topic);
    }

    public String getFullTopic() {
        return fullTopic;
    }

    public String getUnescapedFullTopic() {
        return KapuaSafeHtmlUtils.htmlUnescape(fullTopic);
    }

    public void setFullTopic(String topic) {
        this.fullTopic = topic;
    }

    @Override
    public String toString() {
        return fullTopic;
    }

    @Override
    public int hashCode() {
        // TODO - fixme
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        GwtMqttTopic other = (GwtMqttTopic) obj;
        if (fullTopic == null) {
            if (other.fullTopic != null) {
                return false;
            }
        }

        try {
            if (!SubToken.verifyTopic(SubToken.getTokens(other.fullTopic), fullTopic) && !SubToken.verifyTopic(SubToken.getTokens(fullTopic), other.fullTopic)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private static class SubToken {

        public static final int TOPIC = 1;
        public static final int PLUS = 2;
        public static final int HASH = 3;

        private int type;
        private String value;

        private SubToken(int type, String value) {
            this.type = type;
            this.value = value;
        }

        public static SubToken[] getTokens(String str) throws Exception {
            if (str == null || str.length() == 0) {
                return null;
            }
            ArrayList<SubToken> tokens = new ArrayList<SubToken>();
            String remainingStr = str;
            while (true) {
                if (remainingStr.contains("+/")) {
                    int plusIndex = remainingStr.indexOf("+/");
                    if (plusIndex == 0) {
                        tokens.add(new SubToken(PLUS, "+"));
                        remainingStr = remainingStr.substring(2);
                    } else {
                        tokens.add(new SubToken(TOPIC, remainingStr.substring(0, plusIndex - 1)));
                        remainingStr = remainingStr.substring(plusIndex);
                    }
                } else if (remainingStr.contains("#")) {
                    if (remainingStr.startsWith("#")) {
                        tokens.add(new SubToken(HASH, "#"));
                    } else if (remainingStr.endsWith("#")) {
                        tokens.add(new SubToken(TOPIC, remainingStr.substring(0, remainingStr.length() - 2)));
                        tokens.add(new SubToken(HASH, "#"));
                    } else {
                        throw new Exception("Invalid '#' placement within subscribed topic " + str);
                    }
                    break;
                } else {
                    tokens.add(new SubToken(TOPIC, remainingStr));
                    break;
                }
            }
            SubToken[] result = new SubToken[tokens.size()];
            for (int i = 0; i < tokens.size(); i++) {
                result[i] = (SubToken) tokens.get(i);
            }
            return result;
        }

        public static boolean verifyTopic(SubToken[] toks, String pubTopic) throws Exception {
            if (pubTopic == null) {
                throw new Exception("published topic is null");
            }
            SubToken tok = toks[0];
            if (toks.length > 1) {
                if (tok.isTopic()) {
                    if (pubTopic.length() == 0) {
                        return false;
                    }
                    if (pubTopic.startsWith(tok.getValue())) {
                        if (pubTopic.length() > tok.getValue().length()) {
                            return verifyTopic(trimToken(toks), pubTopic.substring(tok.getValue().length() + 1));
                        } else {
                            return verifyTopic(trimToken(toks), "");
                        }
                    } else {
                        return false;
                    }
                } else if (tok.isPlus()) {
                    int nextTopicLevelIndex = pubTopic.indexOf("/");
                    if (nextTopicLevelIndex == -1) {
                        if (pubTopic.length() != 0) {
                            return verifyTopic(trimToken(toks), "");
                        } else {
                            return false;
                        }
                    } else {
                        if (pubTopic.length() >= nextTopicLevelIndex + 2) {
                            return verifyTopic(trimToken(toks), pubTopic.substring(nextTopicLevelIndex + 1));
                        } else {
                            return false;
                        }
                    }
                } else {
                    throw new Exception("Invalid subscription topic: '#' can only be last topic level.");
                }
            } else {
                if (tok.isHash()) {
                    return true;
                } else if (tok.isPlus()) {
                    if (pubTopic.contains("/") || pubTopic.length() == 0) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return (pubTopic.compareTo(tok.getValue()) == 0);
                }
            }
        }

        public static SubToken[] trimToken(SubToken[] toks) {
            int newLength = toks.length - 1;
            SubToken[] result = new SubToken[newLength];
            for (int i = 0; i < newLength; i++) {
                result[i] = toks[i + 1];
            }
            return result;
        }

        public String getValue() {
            return value;
        }

        public boolean isTopic() {
            if (type == TOPIC) {
                return true;
            }
            return false;
        }

        public boolean isPlus() {
            if (type == PLUS) {
                return true;
            }
            return false;
        }

        public boolean isHash() {
            if (type == HASH) {
                return true;
            }
            return false;
        }
    }
}
