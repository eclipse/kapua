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
package org.eclipse.kapua.service.authentication.credential;

/**
 * {@link Credential} predicates used to build query predicates.
 * 
 * @since 1.0.0
 */
public class CredentialPredicates {

    public static final String SUBJECT = "subject";
    public static final String SUBJECT_TYPE = SUBJECT + ".subjectType";
    public static final String SUBJECT_ID = SUBJECT + ".subjectId";
    public static final String TYPE = "type";
    public static final String KEY = "key";

}
