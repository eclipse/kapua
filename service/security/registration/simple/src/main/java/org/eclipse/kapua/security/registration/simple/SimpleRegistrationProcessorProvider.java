/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.eclipse.kapua.security.registration.RegistrationProcessor;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.security.registration.simple.SimpleRegistrationProcessor.Settings;
import org.eclipse.kapua.security.registration.simple.setting.SimpleSetting;

public class SimpleRegistrationProcessorProvider implements RegistrationProcessorProvider {

    @Override
    public Set<? extends RegistrationProcessor> createAll() {
        final Optional<Settings> result = SimpleRegistrationProcessor.Settings.loadSimpleSettings(SimpleSetting.getInstance());

        return result
                .map(settings -> new SimpleRegistrationProcessor("preferred_username", settings))
                .map(Collections::singleton)
                .orElseGet(Collections::emptySet);
    }

}
