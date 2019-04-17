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
