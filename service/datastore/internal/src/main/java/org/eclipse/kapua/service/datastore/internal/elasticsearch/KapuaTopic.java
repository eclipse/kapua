/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Models a topic for messages posted to the Kapua platform.<br>
 * Topic are expected to be in the form of "account/asset/&lt;application_specific&gt;";<br>
 * system topic starts with the $EDC account.
 */
public class KapuaTopic {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(KapuaTopic.class);

    public static final String MULTI_LEVEL_WCARD = "#";
    public static final String SINGLE_LEVEL_WCARD = "+";
    public static final String TOPIC_SEPARATOR = "/";

    public static final String SYS_ACCOUNT = "$SYS";
    public static final String EDC_ACCOUNT = "$EDC";
    public static final String ALERT_TOPIC = "ALERT";

    private String m_account;
    private String m_asset;
    private String m_semanticTopic;
    private String m_fullTopic;
    private String[] m_topicParts;

    public KapuaTopic(String accountName, String asset, String semTopic) throws KapuaInvalidTopicException {
        this(new StringBuilder().append(accountName)
                .append(TOPIC_SEPARATOR)
                .append(asset)
                .append(TOPIC_SEPARATOR)
                .append(semTopic)
                .toString());
    }

    public KapuaTopic(String fullTopic) throws KapuaInvalidTopicException {

        m_fullTopic = fullTopic;

        if (fullTopic == null ||
                fullTopic.compareTo(MULTI_LEVEL_WCARD) == 0) {
            return;
        }

        m_topicParts = fullTopic.split(TOPIC_SEPARATOR);

        if (m_topicParts.length == 0) {
            return;
        }

        int accountIndex = 0;
        if (m_topicParts[0].startsWith("$")) {
            if (EDC_ACCOUNT.equals(m_topicParts[0])) {
                accountIndex = 1;
            } else {
                return;
            }
        }

        // Either a data topic or a control ($EDC) topic
        if ((m_topicParts.length - accountIndex) < 2) {
            // Special case: The topic is too small
            String account = null;
            if (m_topicParts.length > accountIndex) {
                account = m_topicParts[accountIndex];
            }
            throw new KapuaInvalidTopicException(account, fullTopic);
        }

        // Account and asset parts exist
        m_account = m_topicParts[accountIndex];
        m_asset = m_topicParts[accountIndex + 1];

        if ((m_topicParts.length - accountIndex) == 2) {
            // Special case: The topic does not have a semantic part
            throw new KapuaInvalidTopicException(m_account, fullTopic);
        }

        // Semantic topic exists at offset
        int offset = accountIndex * (EDC_ACCOUNT.length() + 1);

        m_semanticTopic = fullTopic.substring(offset + m_account.length() + m_asset.length() + 2);
    }

    public boolean isSystemTopic() {
        return SYS_ACCOUNT.equals(m_topicParts[0]);
    }

    public boolean isKapuaTopic() {
        return EDC_ACCOUNT.equals(m_topicParts[0]);
    }

    public boolean isAlertTopic() {
        return ALERT_TOPIC.equals(m_topicParts[2]);
    }

    public boolean isAnyAccount() {
        return SINGLE_LEVEL_WCARD.equals(this.getAccount());
    }

    public boolean isAnyAsset() {
        return SINGLE_LEVEL_WCARD.equals(this.getAsset());
    }

    public boolean isAnySubtopic() {
        final String multilevelAnySubtopic = String.format("%s%s", TOPIC_SEPARATOR, MULTI_LEVEL_WCARD);
        boolean isAnySubtopic = this.getSemanticTopic().endsWith(multilevelAnySubtopic) ||
                MULTI_LEVEL_WCARD.equals(this.getSemanticTopic());

        return isAnySubtopic;
    }

    public String getAccount() {
        return m_account;
    }

    public String getAsset() {
        return m_asset;
    }

    public String getSemanticTopic() {
        return m_semanticTopic;
    }

    public String getLeafName() {
        int iLastSlash = m_semanticTopic.lastIndexOf(TOPIC_SEPARATOR);
        return iLastSlash != -1 ? m_semanticTopic.substring(iLastSlash + 1) : m_semanticTopic;
    }

    public String getParentTopic() {
        int iLastSlash = m_semanticTopic.lastIndexOf(TOPIC_SEPARATOR);
        return iLastSlash != -1 ? m_semanticTopic.substring(0, iLastSlash) : null;
    }

    public String getGrandParentTopic() {
        String parentTopic = getParentTopic();
        if (parentTopic != null) {
            int iLastSlash = parentTopic.lastIndexOf(TOPIC_SEPARATOR);
            return iLastSlash != -1 ? parentTopic.substring(0, iLastSlash) : null;
        } else {
            return null;
        }
    }

    public String getFullTopic() {
        return m_fullTopic;
    }

    public String[] getTopicParts() {
        return m_topicParts;
    }

    @Override
    public String toString() {
        return m_fullTopic;
    }

/*	
	@Override
	public int hashCode() {
		//TODO - fixme
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
		
		KapuaTopic other = (KapuaTopic) obj;
		if (m_fullTopic == null) {
			if (other.m_fullTopic != null) {
				return false;
			}
		}
		
		try {
			if (!SubToken.verifyTopic(SubToken.getTokens(other.m_fullTopic), m_fullTopic) && !SubToken.verifyTopic(SubToken.getTokens(m_fullTopic), other.m_fullTopic)) {
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
		
		public static SubToken [] getTokens(String str) throws Exception {
			if(str == null || str.length() == 0) {
				return null;
			}
			ArrayList<SubToken> tokens = new ArrayList<SubToken>();
			String remainingStr = str;
			while(true) {
				if(remainingStr.contains("+/")) {
					int plusIndex = remainingStr.indexOf("+/");
					if(plusIndex == 0 ) {
						tokens.add(new SubToken(PLUS, "+"));
						remainingStr = remainingStr.substring(2);
					} else {
						tokens.add(new SubToken(TOPIC, remainingStr.substring(0, plusIndex-1)));
						remainingStr = remainingStr.substring(plusIndex);
					}
				} else if(remainingStr.contains("#")) {
					if(remainingStr.startsWith("#")) {
						tokens.add(new SubToken(HASH, "#"));
					} else if(remainingStr.endsWith("#")) {
						tokens.add(new SubToken(TOPIC, remainingStr.substring(0, remainingStr.length()-2)));
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
			SubToken [] result = new SubToken[tokens.size()];
			for(int i=0; i<tokens.size(); i++) {
				result[i] = (SubToken)tokens.get(i);
			}
			return result;
		}
		
		public static boolean verifyTopic(SubToken [] toks, String pubTopic) throws Exception {
			if(pubTopic==null) {
				throw new Exception("published topic is null");
			}
			SubToken tok = toks[0];
			if(toks.length > 1) {
				if(tok.isTopic()) {
					if(pubTopic.length() == 0) {
						return false;
					}
					if(pubTopic.startsWith(tok.getValue())) {
						if(pubTopic.length()>tok.getValue().length()) {
							return verifyTopic(trimToken(toks), pubTopic.substring(tok.getValue().length()+1));
						} else {
							return verifyTopic(trimToken(toks), "");
						}
					} else {
						return false;
					}
				} else if(tok.isPlus()) {
					int nextTopicLevelIndex = pubTopic.indexOf("/");
					if(nextTopicLevelIndex == -1) {
						if(pubTopic.length() != 0) {
							return verifyTopic(trimToken(toks), "");
						} else {
							return false;
						}
					} else {
						if(pubTopic.length() >= nextTopicLevelIndex+2) {
							return verifyTopic(trimToken(toks), pubTopic.substring(nextTopicLevelIndex + 1));
						} else {
							return false;
						}
					}
				} else {
					throw new Exception("Invalid subscription topic: '#' can only be last topic level.");
				}
			} else {
				if(tok.isHash()) {
					return true;
				} else if(tok.isPlus()) {
					if(pubTopic.contains("/") || pubTopic.length()==0) {
						return false;
					} else {
						return true;
					}
				} else {
					return (pubTopic.compareTo(tok.getValue())==0);
				}
			}
		}
		
		public static SubToken [] trimToken(SubToken [] toks) {
			int newLength = toks.length-1;
			SubToken [] result = new SubToken[newLength];
			for(int i=0; i<newLength; i++) {
				result[i] = toks[i+1];
			}
			return result;
		}
		
		
		public String getValue() {
			return value;
		}

		
		public boolean isTopic() {
			if(type == TOPIC) {
				return true;
			}
			return false;
		}
		
		public boolean isPlus() {
			if(type == PLUS) {
				return true;
			}
			return false;
		}
		
		public boolean isHash() {
			if(type == HASH) {
				return true;
			}
			return false;
		}
	}
*/
}
