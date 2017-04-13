/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.simulation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonReader {

    private JsonReader() {
    }

    public static Configuration parse(final Reader reader) {
        return createGson().fromJson(reader, Configuration.class);
    }

    public static Configuration parse(final InputStream stream, final Charset charset) {
        return createGson().fromJson(new InputStreamReader(stream, charset), Configuration.class);
    }

    public static Configuration parse(final String json) {
        return createGson().fromJson(json, Configuration.class);
    }

    private static Gson createGson() {
        final GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }
}
