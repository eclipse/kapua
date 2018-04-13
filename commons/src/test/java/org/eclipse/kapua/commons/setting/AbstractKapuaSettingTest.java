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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AbstractKapuaSettingTest {

    private static class TestSettingKey implements SettingKey {

        private String key;

        @SuppressWarnings("unused")
        public TestSettingKey(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return this.key;
        }
    }

    @Test
    public void shouldReadPathFromEnv() {
        // When
        String path = new TestSetting().property("PATH");

        // Then
        Assertions.assertThat(path).isNotEmpty();
    }

    @Test
    public void shouldReadEnvUsingDotNotation() {
        // Given
        System.setProperty("FOO_BAR_BAZ", "qux");

        // When
        String path = new TestSetting().property("foo.bar.baz");

        // Then
        Assertions.assertThat(path).isEqualTo("qux");
    }

    static class TestSetting extends AbstractKapuaSetting<TestSettingKey> {

        String property(String key) {
            return config.getString(key);
        }

        protected TestSetting() {
            super("test.properties");
        }

    }

}
