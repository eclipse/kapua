/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client;

public enum GwtKapuaErrorCode {
    BUNDLE_START_ERROR,
    BUNDLE_STOP_ERROR,
    PACKAGE_URI_SYNTAX_ERROR,
    CANNOT_REMOVE_LAST_ADMIN,
    CURRENT_ADMIN_PASSWORD_DOES_NOT_MATCH,
    DUPLICATE_NAME,
    DEVICE_NEVER_CONNECTED,
    DEVICE_NOT_CONNECTED,
    ENTITY_UNIQUENESS,
    ENTITY_NOT_FOUND,
    ILLEGAL_ACCESS,
    ILLEGAL_ARGUMENT,
    ILLEGAL_NULL_ARGUMENT,
    INTERNAL_ERROR,
    INVALID_RULE_QUERY,
    INVALID_USERNAME_PASSWORD,
    INVALID_XSRF_TOKEN,
    LOCKED_USER,
    OVER_RULE_LIMIT,
    REQUEST_BAD_METHOD,
    UNAUTHENTICATED,
    WARNING,
    XSRF_INVALID_TOKEN,
    PARENT_LIMIT_EXCEEDED_IN_CONFIG,
    SUBJECT_UNAUTHORIZED,
    ENTITY_ALREADY_EXISTS,
    UNABLE_TO_PARSE_CRON_EXPRESSION,
    ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
    SELF_LIMIT_EXCEEDED_IN_CONFIG,
    MAX_NUMBER_OF_ITEMS_REACHED,
    OPERATION_NOT_ALLOWED_ON_ADMIN_USER,
    RETRY_AND_CRON_BOTH_SELECTED,
    END_BEFORE_START_TIME_ERROR,
    SAME_START_AND_DATE,
    SCHEDULE_DUPLICATE_NAME,
    DOWNLOAD_PACKAGE_EXCEPTION,
    TRIGGER_NEVER_FIRE,
    JOB_STARTING_ERROR,
    ADMIN_ROLE_DELETED_ERROR,
    PERMISSION_DELETE_NOT_ALLOWED,
    JOB_STOPPING_ERROR,
}
