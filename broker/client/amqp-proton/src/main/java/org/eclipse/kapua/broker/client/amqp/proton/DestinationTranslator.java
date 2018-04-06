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
package org.eclipse.kapua.broker.client.amqp.proton;

/**
 * Destination translator
 *
 */
public interface DestinationTranslator {

    /**
     * Translate destination if needed by the architecture/use case
     * 
     * @param destination
     * @return
     */
    String translate(String destination);

}
