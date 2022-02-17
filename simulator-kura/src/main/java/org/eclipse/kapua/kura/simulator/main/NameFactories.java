/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringJoiner;

public final class NameFactories {

    private NameFactories() {
    }

    public static NameFactory prefixed(final String prefix) {
        if (prefix == null) {
            return Integer::toString;
        }

        return i -> prefix + i;
    }

    public static NameFactory hostname() {
        return prefixed(dashify(getHostname()) + "-");
    }

    public static NameFactory hostnameAddress() throws Exception {
        return prefixed(dashify(getHostnameAddress().getHostAddress()) + "-");
    }

    public static NameFactory mainInterfaceName() throws Exception {
        return prefixed(dashify(getHostnameInterface().getName()) + "-");
    }

    public static NameFactory mainInterfaceIndex() throws Exception {
        return prefixed(getHostnameInterface().getIndex() + "-");
    }

    public static NameFactory mainInterfaceAddress() throws Exception {
        final StringJoiner sj = new StringJoiner("-");
        for (final byte b : getHostnameInterface().getHardwareAddress()) {
            sj.add(String.format("%02X", b));
        }
        return prefixed(sj.toString() + "-");
    }

    /**
     * Convert all dots to dashes, remove all other special characters
     *
     * @param string
     *            a string to process
     * @return the result
     */
    private static String dashify(final String string) {
        if (string == null) {
            return null;
        }

        return string.replaceAll("\\.", "-").replaceAll("[^a-zA-Z0-9\\-]", "");
    }

    private static NetworkInterface getHostnameInterface() throws UnknownHostException, SocketException {
        final String hostname = getHostname();
        final InetAddress addr = getHostnameAddress(hostname);

        final NetworkInterface iface = NetworkInterface.getByInetAddress(addr);
        if (iface == null) {
            throw new IllegalStateException(String.format("Unable to find interface for %s / %s", hostname, addr));
        }

        return iface;
    }

    private static String getHostname() {
        final String hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.isEmpty()) {
            throw new IllegalStateException("Environment variable 'HOSTNAME' is not set");
        }
        return hostname;
    }

    private static InetAddress getHostnameAddress() throws UnknownHostException {
        return getHostnameAddress(getHostname());
    }

    private static InetAddress getHostnameAddress(final String hostname) throws UnknownHostException {
        final InetAddress addr = InetAddress.getByName(hostname);
        if (addr == null) {
            throw new IllegalStateException(String.format("Unable to get address of hostname '%s'", hostname));
        }
        return addr;
    }
}
