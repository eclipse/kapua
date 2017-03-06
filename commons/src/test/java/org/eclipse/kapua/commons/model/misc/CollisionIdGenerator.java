/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import java.math.BigInteger;

public class CollisionIdGenerator {

    private String fixedValue;
    private BigInteger startIncrementalValue;
    private int fixedValueGenerationCount;

    private int extractedValues = 0;

    public CollisionIdGenerator(String fixedValue, BigInteger startIncrementalValue, int fixedValueGenerationCount) {
        this.fixedValue = fixedValue;
        this.startIncrementalValue = startIncrementalValue;
        this.fixedValueGenerationCount = fixedValueGenerationCount;
    }

    /**
     * Generate a {@link BigInteger} fixed value until fixedValueGenerationCount is reached. After that the value will be incremental from the startIncrementalValue<br>
     *
     * @return
     */
    public BigInteger generate() {
        if (++extractedValues < fixedValueGenerationCount) {
            return new BigInteger(fixedValue);
        } else {
            startIncrementalValue = startIncrementalValue.add(new BigInteger("1"));
            return startIncrementalValue;
        }
    }

    public int getGeneretedValuesCount() {
        return extractedValues;
    }

}
