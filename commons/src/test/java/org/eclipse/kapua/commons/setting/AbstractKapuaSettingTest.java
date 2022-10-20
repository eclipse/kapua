/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AbstractKapuaSettingTest {

    private class IncorrectConfigResourceName extends AbstractKapuaSetting<TestSettingKey> {
        protected IncorrectConfigResourceName() {
            super("incorrect.properties");
        }
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void abstractKapuaSettingIncorrectConfigResourceNameTest() {
        AbstractKapuaSetting abstractKapuaSetting = new IncorrectConfigResourceName();
    }

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
    public void shouldReadPathFromEnvTest() {
        String path = new TestSetting().property("PATH");

        Assertions.assertThat(path).isNotEmpty();
    }

    @Test
    public void shouldReadEnvUsingDotNotationTest() {
        System.setProperty("FOO_BAR_BAZ", "qux");
        String path = new TestSetting().property("foo.bar.baz");

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
