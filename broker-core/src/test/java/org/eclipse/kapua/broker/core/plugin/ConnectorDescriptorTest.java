/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.junit.Assert;
import org.junit.Test;

public class ConnectorDescriptorTest {
    
    /**
     * A simple test to get a default descriptor
     */
    @Test
    public void testNonNull () {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        Assert.assertNotNull(provider);
    }
    
    /**
     * A simple test to get a descriptor
     */
    @Test
    public void testDefault1 () {
        ConnectorDescriptorProvider provider = ConnectorDescriptorProviders.getInstance();
        ConnectorDescriptor descriptor = provider.getDescriptor("foo");
        Assert.assertNotNull(descriptor);
    }
}
