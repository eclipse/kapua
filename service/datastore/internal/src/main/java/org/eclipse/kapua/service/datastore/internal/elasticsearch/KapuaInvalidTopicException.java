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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

public class KapuaInvalidTopicException extends Exception {

    private static final long serialVersionUID = 2841854292521738239L;

    private String m_account;

    public KapuaInvalidTopicException(String account, String topic) {
        super(topic);
        m_account = account;
    }

    public String getAccount() {
        return m_account;
    }
}
