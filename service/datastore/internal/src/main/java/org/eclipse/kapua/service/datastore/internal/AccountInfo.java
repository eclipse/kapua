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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.LocalServicePlan;

class AccountInfo 
{
    private Account account;
    private LocalServicePlan servicePlan;
    
    public AccountInfo(Account account, LocalServicePlan servicePlan) {
        this.account = account;
        this.servicePlan = servicePlan;
    }
    
    public Account getAccount() {
        return this.account;
    }
    
    public LocalServicePlan getServicePlan() {
        return this.servicePlan;
    }
}
