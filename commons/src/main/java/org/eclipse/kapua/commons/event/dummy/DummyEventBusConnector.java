/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.dummy;

import java.util.Date;

import org.eclipse.kapua.commons.event.EventListener;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The event bus connector receives messsages from the event bus and forwards them
 * to the event listener that it is bound to. The connector connect to the event 
 * bus through a client that is picked from a client pool. 
 * 
 * @since 1.0
 */
public class DummyEventBusConnector {

    private final static Logger logger = LoggerFactory.getLogger(DummyEventBusConnector.class);

    private Thread thread;

    private final EventListener listener;

    public DummyEventBusConnector(EventListener listener) {
        this.listener = listener;
        // Initialize event bus client
        // EventBusClient client = new EventBusClient();
        // client.setCallback(this);
    }

    public void start() {
        logger.info("Starting event connector ...");

        final DummyEventBusConnector thisConnector = this;
        this.thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    thisConnector.onKapuaEvent(new KapuaEvent() {

                        @Override
                        public String getContextId() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public Date getTimestamp() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public KapuaId getUserId() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getService() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getEntityType() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public KapuaId getEntityId() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getOperation() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public OperationStatus getOperationStatus() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getFailureMessage() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getInputs() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getOutputs() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getNote() {
                            // TODO Auto-generated method stub
                            return null;
                        }

                        @Override
                        public String getProperties() {
                            // TODO Auto-generated method stub
                            return null;
                        }
                    });
                    ;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        logger.info("Exiting event connector thread loop ...");
                        break;
                    }
                }
            }

        });
        thread.start();
    }

    public void stop() {
        logger.info("Stopping event connector ...");
        thread.interrupt();
    }

    // EventBus client callback handler
    private void onKapuaEvent(KapuaEvent event) {
        listener.onKapuaEvent(event);
    }
}
