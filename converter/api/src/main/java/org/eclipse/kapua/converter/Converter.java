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
package org.eclipse.kapua.converter;

import org.eclipse.kapua.connector.MessageContext;

public interface Converter<F,T> {

    public static final String MESSAGE_DESTINATION = new String("message-destination");
    public static final String MESSAGE_QOS = new String("message-qos");
    public static final String MESSAGE_TYPE = new String("message-type");

    public MessageContext<T> convert(MessageContext<F> message) throws KapuaConverterException;
}
