/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

/**
 * KapuaErrorCodes holds the enumeration of common error codes for KapuaServices.<br>
 * For each defined enum value, a corresponding message should be defined in the properties bundle named: kapua-service-error-messages.properties.
 *
 * @since 1.0
 */
public enum KapuaErrorCodes implements KapuaErrorCode {

    /**
     * Entity not found
     */
    ENTITY_NOT_FOUND,
    /**
     * Entity already exists
     */
    ENTITY_ALREADY_EXISTS,
    /**
     * Duplicate name
     */
    DUPLICATE_NAME,
    /**
     * Duplicate externalId
     */
    DUPLICATE_EXTERNAL_ID,
    /**
     * Entity uniqueness in scope
     */
    ENTITY_UNIQUENESS,
    /**
     * Illegal access
     */
    ILLEGAL_ACCESS,
    /**
     * Illegal argument
     */
    ILLEGAL_ARGUMENT,
    /**
     * Illegal null argument
     */
    ILLEGAL_NULL_ARGUMENT,
    /**
     * Illegal state
     */
    ILLEGAL_STATE,
    /**
     * Optimistic locking
     */
    OPTIMISTIC_LOCKING,
    /**
     * Unauthenticated
     */
    UNAUTHENTICATED,
    /**
     * Operation not supported
     */
    OPERATION_NOT_SUPPORTED,
    /**
     * Internal error
     */
    INTERNAL_ERROR,
    /**
     * Something very very bad! An error that the system does not expect at all and that is unmanaged.
     */
    SEVERE_INTERNAL_ERROR,
    /**
     * Child Accounts limit valuation
     */
    PARENT_LIMIT_EXCEEDED_IN_CONFIG,
    /**
     * User unauthorized
     */
    SUBJECT_UNAUTHORIZED,
    /**
     * Entity already exist in another account
     */
    ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
    /**
     * External Id already exist in another account
     */
    EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
    /**
     * The device has returned an error when starting bundle
     */
    BUNDLE_START_ERROR,

    /**
     * The device has returned an error when stopping bundle
     */
    BUNDLE_STOP_ERROR,

    /**
     * Syntax error in Package URI
     */
    PACKAGE_URI_SYNTAX_ERROR,

    /**
     * Max number of items is reached
     */
    MAX_NUMBER_OF_ITEMS_REACHED,

    /**
     * End before start time error
     */
    END_BEFORE_START_TIME_ERROR,

    /**
     * Same values start and end date and time
     */
    SAME_START_AND_DATE,

    /**
     * retry interval and cron expression both selected
     */
    RETRY_AND_CRON_BOTH_SELECTED,
    DEVICE_NOT_FOUND,
    SCHEDULE_DUPLICATE_NAME,
    DOWNLOAD_PACKAGE_EXCEPTION,

    /**
     * trigger will never be fired
     */
    TRIGGER_NEVER_FIRE,
    ADMIN_ROLE_DELETED_ERROR,

    /**
     * Deleting specific permission is not allowed
     */
    PERMISSION_DELETE_NOT_ALLOWED,

    /**
     * The service has been disabled
     */
    SERVICE_DISABLED

}
