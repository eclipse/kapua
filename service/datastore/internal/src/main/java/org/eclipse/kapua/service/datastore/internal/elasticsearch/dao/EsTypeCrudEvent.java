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
package org.eclipse.kapua.service.datastore.internal.elasticsearch.dao;

/**
 * Elasticsearch crud event type
 * 
 * @since 1.0
 *
 */
public class EsTypeCrudEvent extends EsDaoEvent
{

    /**
     * CRUD operation
     *
     */
    public enum Operation
    {
        /**
         * Insert
         */
        INSERT,
        /**
         * Upsert (insert if not exist, update if exist)
         */
        UPSERT,
        /**
         * Delete
         */
        DELETE
    }

    /**
     * Allowed operation results
     *
     */
    public enum OperationResult
    {
        /**
         * Success
         */
        SUCCESS,
        /**
         * Failure
         */
        FAILURE
    }

    private Operation       operation;
    private OperationResult operResult;

    private String account;
    private String type;

    /**
     * Get the operation
     * 
     * @return
     */
    public Operation getOperation()
    {
        return operation;
    }

    /**
     * Set the operation
     * 
     * @param operation
     */
    public void setOperation(Operation operation)
    {
        this.operation = operation;
    }

    /**
     * Get operation result
     * 
     * @return
     */
    public OperationResult getOperResult()
    {
        return operResult;
    }

    /**
     * Set operation result
     * 
     * @param operResult
     */
    public void setOperResult(OperationResult operResult)
    {
        this.operResult = operResult;
    }

    /**
     * Get account
     * 
     * @return
     */
    public String getAccount()
    {
        return account;
    }

    /**
     * Set account
     * 
     * @param account
     */
    public void setAccount(String account)
    {
        this.account = account;
    }

    /**
     * Get type
     * 
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     * Set type
     * 
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }

}
