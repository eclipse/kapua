/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Models a topic for messages posted to the Kapua platform.<br>
 * Topic are expected to be in the form of "account/clientId/&lt;application_specific&gt;";
 * system topic starts with the $EDC account.
 *
 * @since 1.0.0
 */
public class DatastoreChannel {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(DatastoreChannel.class);

    /**
     * Default topic parts separator
     */
    public static final String TOPIC_SEPARATOR = "/";
    /**
     * Default multiple level wild card
     */
    public static final String MULTI_LEVEL_WCARD = "#";

    private static final String MULTI_LEVEL_SEPARATOR_WCARD = TOPIC_SEPARATOR + MULTI_LEVEL_WCARD;

    /**
     * Default single level wild card
     */
    public static final String SINGLE_LEVEL_WCARD = "+";

    public static final String ALERT_TOPIC = "ALERT";

    private String channel;
    private String[] channelParts;

    /**
     * Construct a channel with the provided channel name
     *
     * @param channel
     * @throws InvalidChannelException
     */
    public DatastoreChannel(String channel) throws InvalidChannelException {
        // Must be not null and not single level wild card
        if (channel == null) {
            throw new InvalidChannelException("Invalid channel: " + channel);
        }

        // Check if there is one single level wild card or if the multi level wild card is present more than once or not at the end of the topic
        if (channel.indexOf(SINGLE_LEVEL_WCARD) != -1) {
            throw new InvalidChannelException(String.format("Invalid channel [%s]. The channel cannot contain [%s] wildcard!", channel, SINGLE_LEVEL_WCARD));
        }
        int indexOfMultiLevelWildCard = channel.indexOf(MULTI_LEVEL_WCARD);
        if (indexOfMultiLevelWildCard != -1) {
            if (indexOfMultiLevelWildCard < channel.length() - 1) {
                throw new InvalidChannelException(String.format("Invalid channel [%s]. The channel [%s] wildcard is allowed only at the end of the channel!", channel, MULTI_LEVEL_WCARD));
            }
        }

        this.channel = channel;
        channelParts = this.channel.split(TOPIC_SEPARATOR);
    }

    /**
     * Return the channel without the multi level wildcard, if any.
     *
     * @return
     */
    public String getChannelCleaned() {
        if (isAnyChannel()) {
            return "";
        } else if (isWildcardChannel()) {
            return channel.substring(0, channel.length() - 1);
        } else {
            return channel;
        }
    }

    /**
     * Check if the channel is an alert channel, so if it has 'ALERT' as third level topic (and no more topics level).<br>
     * In the MQTT word this method return true if the topic is like 'account/client/ALERT'.
     *
     * @return
     * @since 1.0.0
     */
    public boolean isAlertTopic() {
        return ALERT_TOPIC.equals(channel);
    }

    /**
     * Return true if the channel is single path channel and the last channel part is the {@link DatastoreChannel#MULTI_LEVEL_WCARD} char.
     *
     * @return
     * @since 1.0.0
     */
    public boolean isAnyChannel() {
        return channel != null && MULTI_LEVEL_WCARD.equals(channel);
    }

    /**
     * Return true if the channel is multi path channel and the last channel part is the {@link DatastoreChannel#MULTI_LEVEL_WCARD} char.<br>
     * <b>This method returns false if the channel is {@link DatastoreChannel#MULTI_LEVEL_WCARD}.</b>
     *
     * @return
     * @since 1.0.0
     */
    public boolean isWildcardChannel() {
        return channel != null && channel.endsWith(MULTI_LEVEL_SEPARATOR_WCARD);
    }

    /**
     * Get the channel formatted string given the topic parts
     *
     * @param parts
     * @return
     * @since 1.0.0
     */
    public static String getChannel(final List<String> parts) {
        return String.join(DatastoreChannel.TOPIC_SEPARATOR, parts);
    }

    /**
     * Get the channel
     *
     * @return
     * @since 1.0.0
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Get the last topic part (leaf)
     *
     * @return
     * @since 1.0.0
     */
    public String getLeafName() {
        return channelParts[channelParts.length - 1];
    }

    /**
     * Get the parent topic
     *
     * @return
     * @since 1.0.0
     */
    public String getParentTopic() {
        int iLastSlash = channel.lastIndexOf(TOPIC_SEPARATOR);
        return iLastSlash != -1 ? channel.substring(0, iLastSlash) : null;
    }

    /**
     * Get the grand parent topic
     *
     * @return
     * @since 1.0.0
     */
    public String getGrandParentTopic() {
        String parentTopic = getParentTopic();
        if (parentTopic != null) {
            int iLastSlash = parentTopic.lastIndexOf(TOPIC_SEPARATOR);
            return iLastSlash != -1 ? parentTopic.substring(0, iLastSlash) : null;
        } else {
            return null;
        }
    }

    /**
     * Get the channel parts
     *
     * @return
     * @since 1.0.0
     */
    public String[] getParts() {
        return channelParts;
    }

    @Override
    public String toString() {
        return channel;
    }

}
