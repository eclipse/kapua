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
package org.eclipse.kapua.model.subject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.KapuaService;

/**
 * {@link Subject} definition.<br>
 * The {@link Subject} represent an "actor" on the system.<br>
 * The {@link Subject} can be different 'things' in the system according to the {@link SubjectType}.
 * Other {@link KapuaService}s can use this entity to add domain properties to the acting entities in the system.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "subject")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "subjectType", //
        "id"//
},//
        factoryClass = SubjectXmlRegistry.class, factoryMethod = "newSubject")
public interface Subject extends KapuaSerializable {

    /**
     * Returns the {@link SubjectType}.
     * 
     * @return The {@link SubjectType}.
     * @since 1.0.0
     */
    @XmlElement(name = "subjectType")
    public SubjectType getSubjectType();

    /**
     * Sets the {@link SubjectType}.
     * 
     * @param type
     *            The {@link SubjectType}.
     * @since 1.0.0
     */
    public void setType(SubjectType type);

    /**
     * Returns the subject id.
     * 
     * @return The subject id.
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getId();

    /**
     * Sets the subject id.
     * 
     * @param id
     *            The subject id.
     * @since 1.0.0
     */
    public void setId(KapuaId id);
}
