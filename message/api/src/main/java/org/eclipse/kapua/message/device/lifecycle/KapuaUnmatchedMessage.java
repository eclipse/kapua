package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaMessage;

/**
 * Kapua unmatched message object definition.<br>
 * This message is used, as last resource, if no control message filter matches the incoming topic.
 * 
 * @since 1.0
 *
 */
public interface KapuaUnmatchedMessage extends KapuaMessage<KapuaUnmatchedChannel, KapuaUnmatchedPayload>
{

}
