/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.amqp;

public interface DestinationTranslator {

    /**
     * Translate destination if needed by the architecture/use case from client domain to broker domain
     * 
     * @param destination
     * @return
     */
    String translateFromClientDomain(String destination);

    /**
     * Translate destination if needed by the architecture/use case from broker domain to client domain
     * 
     * @param destination
     * @return
     */
    String translateToClientDomain(String destination);

}
