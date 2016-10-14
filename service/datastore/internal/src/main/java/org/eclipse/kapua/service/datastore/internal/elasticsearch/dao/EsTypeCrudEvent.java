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
package org.eclipse.kapua.service.datastore.internal.elasticsearch.dao;

public class EsTypeCrudEvent extends EsDaoEvent
{

    public enum Operation
    {
        INSERT, 
        UPSERT, 
        DELETE
    }

    public enum OperationResult
    {
        SUCCESS, 
        FAILURE
    }

    private Operation       operation;
    private OperationResult operResult;

    private String          account;
    private String          type;

    public Operation getOperation()
    {
        return operation;
    }

    public void setOperation(Operation operation)
    {
        this.operation = operation;
    }

    public OperationResult getOperResult()
    {
        return operResult;
    }

    public void setOperResult(OperationResult operResult)
    {
        this.operResult = operResult;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
