/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.engine.app;

import java.io.IOException;

import org.eclipse.kapua.model.id.KapuaId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class KapuaIdSerializer extends StdSerializer<KapuaId> {

    public KapuaIdSerializer() {
        super(KapuaId.class);
    }

    @Override
    public void serialize(KapuaId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toCompactId());
    }
}
