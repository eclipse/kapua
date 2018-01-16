/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.configuration;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.service.configuration.internal.StringUtil;
import org.eclipse.kapua.service.configuration.internal.metatype.Password;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void shouldConvertPasswordsArrayToString() {
        // Given
        Password[] passwords = new Password[] {new Password("foo") };

        // When
        String passwordsString = StringUtil.valueToString(passwords);

        // Then
        Assertions.assertThat(passwordsString).isEqualTo("foo");
    }

}
