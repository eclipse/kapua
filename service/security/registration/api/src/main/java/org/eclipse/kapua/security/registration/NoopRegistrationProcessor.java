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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.security.registration;

import org.eclipse.kapua.service.user.User;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class NoopRegistrationProcessor implements RegistrationProcessorProvider {


    @Override
    public Collection<? extends RegistrationProcessor> createAll() {
        return Collections.singleton(new RegistrationProcessor() {
            @Override
            public Optional<User> createUser(JwtContext context) throws Exception {
                return Optional.empty();
            }

            @Override
            public void close() throws Exception {

            }
        });
    }
}
