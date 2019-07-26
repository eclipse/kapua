/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate;

import org.eclipse.kapua.model.domain.AbstractDomain;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Certificate} domain.<br>
 * Used to describe the {@link Certificate} and {@link CertificateInfo} {@link Domain} in the {@link CertificateService} and {@link CertificateService}.
 *
 * @since 1.0.0
 */
public class CertificateDomain extends AbstractDomain {

    private String name = "certificate";
    private Set<Actions> actions = new HashSet<>(Arrays.asList(Actions.read, Actions.delete, Actions.write));

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public boolean getGroupable() {
        return false;
    }
}
