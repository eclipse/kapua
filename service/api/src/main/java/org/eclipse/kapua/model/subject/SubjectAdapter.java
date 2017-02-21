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
package org.eclipse.kapua.model.subject;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaIdFactory;

/**
 * Kapua {@link Subject} adapter. It marshal and unmarshal the Kapua {@link Subject} in a proper way.
 * 
 * @since 1.0.0
 *
 */
public class SubjectAdapter extends XmlAdapter<String, Subject> {

    private static final String SEMICOLUMN = ":";

    /**
     * {@link KapuaLocator} instance.
     */
    private final KapuaLocator locator = KapuaLocator.getInstance();

    /**
     * {@link SubjectFactory} instance.
     */
    private final SubjectFactory subjectFactory = locator.getFactory(SubjectFactory.class);

    /**
     * {@link KapuaIdFactory} instance.
     */
    private final KapuaIdFactory kapuaIdFactory = locator.getFactory(KapuaIdFactory.class);

    @Override
    public String marshal(Subject subject) throws Exception {
        return new StringBuilder().append(subject.getSubjectType()).append(SEMICOLUMN).append(subject.getId().toCompactId()).toString();
    }

    @Override
    public Subject unmarshal(String v) throws Exception {
        return null;
    }

}
