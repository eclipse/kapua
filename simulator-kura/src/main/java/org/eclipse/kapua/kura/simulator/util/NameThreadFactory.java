/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class NameThreadFactory implements ThreadFactory {

	private final String baseName;

	private final AtomicLong counter = new AtomicLong();

	private final boolean daemon;

	public NameThreadFactory(final String baseName) {
		this(baseName, false);
	}

	public NameThreadFactory(final String baseName, final boolean daemon) {
		this.baseName = baseName;
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(final Runnable r) {
		final String name = String.format("%s/%s", this.baseName, this.counter.incrementAndGet());
		final Thread t = new Thread(r, name);
		t.setDaemon(this.daemon);
		return t;
	}

}
