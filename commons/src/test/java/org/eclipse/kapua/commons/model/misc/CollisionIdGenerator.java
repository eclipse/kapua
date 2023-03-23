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

    private BigInteger currentValue;
    private final BigInteger incrementBy;
    private final int everyNthGeneration;

    private int generatedValues;

    public CollisionIdGenerator(long initialValue, long incrementBy, int everyNthGeneration) {
        this.currentValue = BigInteger.valueOf(initialValue);
        this.incrementBy = BigInteger.valueOf(incrementBy);
        this.everyNthGeneration = everyNthGeneration;
        this.generatedValues = 0;
    }

    /**
     * Generate a {@link BigInteger} fixed value until fixedValueGenerationCount is reached. After that the value will be incremental from the startIncrementalValue<br>
     *
     * @return
     */
    public BigInteger generate() {
        if (++generatedValues % everyNthGeneration == 0) {
            currentValue = currentValue.add(incrementBy);
        }
        return currentValue;
    }

    public int getGeneretedValuesCount() {
        return generatedValues;
    }

}
