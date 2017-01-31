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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.model.subject;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.model.subject.SubjectFactory;
import org.eclipse.kapua.model.subject.SubjectType;

/**
 * {@link SubjectFactory} implementation.
 * 
 * @since 1.0.0
 *
 */
@KapuaProvider
public class SubjectFactoryImpl implements SubjectFactory {

    @Override
    public Subject newSubject(SubjectType type, KapuaId id) {
        return new SubjectImpl(type, id);
    }

}
