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
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.message.internal.device.data.KapuaDeviceDataTest;
import org.eclipse.kapua.message.internal.device.lifecycle.LifecycleTestSuite;
import org.eclipse.kapua.message.internal.xml.MetricTestSuite;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BasicMessageTestSuite.class,
        MetricTestSuite.class,
        KapuaDeviceDataTest.class,
        LifecycleTestSuite.class

})
@Category(JUnitTests.class)
public class FullMessageTestSuite {
}
