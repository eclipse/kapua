/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
    CANNOT_REMOVE_LAST_ADMIN,
    CURRENT_ADMIN_PASSWORD_DOES_NOT_MATCH,
    DUPLICATE_NAME,
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
    UNAUTHENTICATED,
    WARNING,
    XSRF_INVALID_TOKEN,
    PARENT_LIMIT_EXCEEDED_IN_CONFIG,
    SUBJECT_UNAUTHORIZED,
    ENTITY_ALREADY_EXISTS,
    UNABLE_TO_PARSE_CRON_EXPRESSION,
    ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
    SELF_LIMIT_EXCEEDED_IN_CONFIG
}
