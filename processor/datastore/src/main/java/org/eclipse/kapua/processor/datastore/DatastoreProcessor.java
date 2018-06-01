/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.processor.datastore;

import java.util.UUID;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.KapuaProcessorException;
import org.eclipse.kapua.processor.Processor;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatastoreProcessor implements Processor<TransportMessage> {

    private static final Logger logger = LoggerFactory.getLogger(DatastoreProcessor.class);

    private AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);

    @Override
    public void start() throws KapuaProcessorException {
        logger.info("Instantiate Jaxb Context... Done.");
    }

    @Override
    public void process(MessageContext<TransportMessage> message) throws KapuaProcessorException {
        logger.info("Datastore service... received message: {}", message);
        logger.info("Datastore service... converting message...");
        TransportMessage tm = message.getMessage();

        KapuaDataMessage kapuaDataMessage = new KapuaDataMessageImpl();

        //channel
        KapuaDataChannel kapuaChannel = new KapuaDataChannelImpl();
        kapuaChannel.setSemanticParts(tm.getChannel().getSemanticParts());

        //payload
        KapuaDataPayload kapuaPayload = new KapuaDataPayloadImpl();
        kapuaPayload.setMetrics(tm.getPayload().getMetrics());
        kapuaPayload.setBody(tm.getPayload().getBody());

        kapuaDataMessage.setChannel(kapuaChannel);
        kapuaDataMessage.setPayload(kapuaPayload);
        kapuaDataMessage.setCapturedOn(null);
        kapuaDataMessage.setClientId(tm.getClientId());
        kapuaDataMessage.setDeviceId(null);
        kapuaDataMessage.setId(UUID.randomUUID());
        kapuaDataMessage.setPosition(tm.getPosition());
        kapuaDataMessage.setReceivedOn(tm.getReceivedOn());
        kapuaDataMessage.setSentOn(tm.getSentOn());
        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                Account account = accountService.findByName(tm.getScopeName());
                if (account==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "message.scopeName", tm.getScopeName());
                }
                kapuaDataMessage.setScopeId(account.getId());
                logger.info("Datastore service... converting message... DONE");
                logger.info("Datastore service... storing message...");
                messageStoreService.store(kapuaDataMessage);
                logger.info("Datastore service... storing message... DONE");
            });
        } catch (KapuaException e) {
            logger.info("Datastore service... Error processing message {}", e.getMessage());
            throw new KapuaProcessorException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public void stop() throws KapuaProcessorException {
        // nothing to do
    }

}
