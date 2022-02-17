/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.router;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

@Category(JUnitTests.class)
public class EndPointContainerTest extends Assert {

    @Test
    public void endPointContainerTest() {
        EndPointContainer container = new EndPointContainer();
        assertEquals("Expected and actual values should be the same.", 0, container.getEndPoints().size());
    }

    @Test
    public void setAndGetEndPointsTest() {
        EndPointContainer container = new EndPointContainer();
        List<EndPoint> endPointList = new ArrayList<>();
        EndPoint endPoint1 = new EndChainEndPoint();
        EndPoint endPoint2 = new EndChainEndPoint();

        assertEquals("Expected and actual values should be the same.", 0, container.getEndPoints().size());

        endPointList.add(endPoint1);
        endPointList.add(endPoint2);
        endPointList.add(null);
        container.setEndPoints(endPointList);

        assertEquals("Expected and actual values should be the same.", 3, container.getEndPoints().size());
        assertEquals("Expected and actual values should be the same.", endPoint1, container.getEndPoints().get(0));
        assertEquals("Expected and actual values should be the same.", endPoint2, container.getEndPoints().get(1));
        assertNull("Null expected ", container.getEndPoints().get(2));
    }
}