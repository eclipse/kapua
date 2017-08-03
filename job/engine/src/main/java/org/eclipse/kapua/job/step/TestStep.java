/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.step;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.Batchlet;

public class TestStep extends AbstractBatchlet implements Batchlet {

    @Override
    public String process() throws Exception {
        System.err.println("Process call");
        return null;
    }

}
