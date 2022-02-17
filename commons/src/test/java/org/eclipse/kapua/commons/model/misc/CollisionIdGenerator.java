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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import java.math.BigInteger;

public class CollisionIdGenerator {

    private String fixedValue;
    private BigInteger startIncrementalValue;
    private int fixedValueGenerationCount;

    private int extractedValues;

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
