/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

/**
 * Utility class to handle comparison between component versions.
 *
 * @see <a href="https://semver.org/">Semantic Versioning</a>.
 * @since 1.2.0
 */
public class SemanticVersion {

    private final String versionString;
    private final VersionToken majorVersion;
    private final VersionToken minorVersion;
    private final VersionToken patchVersion;

    /**
     * Constructor.
     * Parses the given {@link String} according to the rules defined by the <a href="https://semver.org/">Semantic Versioning</a>.
     *
     * @param version The {@link String} version to parse
     * @since 1.2.0
     */
    public SemanticVersion(String version) {
        this.versionString = version;

        String[] versionSplitted = version.split("\\.");

        majorVersion = new VersionToken(versionSplitted[0]);

        if (versionSplitted.length > 1) {
            minorVersion = new VersionToken(versionSplitted[1]);
        } else {
            minorVersion = null;
        }

        if (versionSplitted.length > 2) {
            patchVersion = new VersionToken(versionSplitted[2]);
        } else {
            patchVersion = null;
        }
    }

    /**
     * Gets the original {@link String} provided.
     *
     * @return The original {@link String} provided.
     * @since 1.2.0
     */
    public String getVersionString() {
        return versionString;
    }

    /**
     * Gets the major version.
     *
     * @return The major version.
     * @since 1.2.0
     */
    public VersionToken getMajorVersion() {
        return majorVersion;
    }

    /**
     * Gets the minor version.
     *
     * @return The minor version.
     * @since 1.2.0
     */
    public VersionToken getMinorVersion() {
        return minorVersion;
    }

    /**
     * Gets the patch version.
     *
     * @return The patch version.
     * @since 1.2.0
     */
    public VersionToken getPatchVersion() {
        return patchVersion;
    }

    /**
     * Compares {@code this} {@link SemanticVersion} with another {@link SemanticVersion}.
     *
     * @param other The {@link SemanticVersion} to use to compare.
     * @return {@code true} if {@code this} {@link SemanticVersion} is after or matches the given {@link SemanticVersion}
     * @since 1.2.0
     */
    public boolean afterOrMatches(SemanticVersion other) {
        return after(other) || matches(other);
    }

    /**
     * Compares {@code this} {@link SemanticVersion} with another {@link SemanticVersion}.
     *
     * @param other The {@link SemanticVersion} to use to compare.
     * @return {@code true} if {@code this} {@link SemanticVersion} is after the given {@link SemanticVersion}
     * @since 1.2.0
     */
    public boolean after(SemanticVersion other) {
        if (getMajorVersion().after(other.getMajorVersion())) {
            return true;
        } else if (getMajorVersion().matches(other.getMajorVersion())) {

            if (getMinorVersion() != null && getMinorVersion().after(other.getMinorVersion())) {
                return true;
            } else if (getMinorVersion().matches(other.getMinorVersion())) {
                if (getPatchVersion() != null && getPatchVersion().after(other.getPatchVersion())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Compares {@code this} {@link SemanticVersion} with another {@link SemanticVersion}.
     *
     * @param other The {@link SemanticVersion} to use to compare.
     * @return {@code true} if {@code this} {@link SemanticVersion} is before or matches the given {@link SemanticVersion}
     * @since 1.2.0
     */
    public boolean beforeOrMatches(SemanticVersion other) {
        return before(other) || matches(other);
    }

    /**
     * Compares {@code this} {@link SemanticVersion} with another {@link SemanticVersion}.
     *
     * @param other The {@link SemanticVersion} to use to compare.
     * @return {@code true} if {@code this} {@link SemanticVersion} is before the given {@link SemanticVersion}
     * @since 1.2.0
     */
    public boolean before(SemanticVersion other) {
        if (getMajorVersion().before(other.getMajorVersion())) {
            return true;
        } else if (getMajorVersion().matches(other.getMajorVersion())) {

            if (getMinorVersion() != null && getMinorVersion().before(other.getMinorVersion())) {
                return true;
            } else if (getMinorVersion().matches(other.getMinorVersion())) {
                if (getPatchVersion() != null && getPatchVersion().before(other.getPatchVersion())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Compares {@code this} {@link SemanticVersion} with another {@link SemanticVersion}.
     *
     * @param other The {@link SemanticVersion} to use to compare.
     * @return {@code true} if {@code this} {@link SemanticVersion} is matches the given {@link SemanticVersion}
     * @since 1.2.0
     */
    public boolean matches(SemanticVersion other) {
        return getVersionString().equals(other.getVersionString());
    }

    @Override
    public String toString() {
        return getVersionString();
    }

    /**
     * Representation of the single token of a component version.
     *
     * @since 1.2.0
     */
    static class VersionToken {
        String versionString;
        Integer versionInteger;
        boolean integerComparison;

        /**
         * Constructor.
         *
         * @param versionToken The version token.
         * @since 1.2.0
         */
        VersionToken(String versionToken) {
            try {
                versionInteger = Integer.parseInt(versionToken);
                integerComparison = true;
            } catch (NumberFormatException e) {
                versionInteger = null;
                integerComparison = false;
            }

            versionString = versionToken;
        }

        /**
         * Gets the {@link String} representation of the {@link VersionToken}.
         *
         * @return The {@link String} representation of the {@link VersionToken}.
         * @since 1.2.0
         */
        public String getVersionString() {
            return versionString;
        }

        /**
         * Gets the {@link Integer} representation of the {@link VersionToken}, if available.
         *
         * @return The {@link String} representation of the {@link VersionToken} or {@code null} if token is not a {@link Integer}.
         * @since 1.2.0
         */
        public Integer getVersionInteger() {
            return versionInteger;
        }

        /**
         * Returns whether or not {@code this} {@link VersionToken} can be compared as an {@link Integer}.
         *
         * @return {@true} if {@code this} {@link VersionToken} can be compared as a {@link Integer}, {@code false} otherwise.
         */
        public boolean isIntegerComparison() {
            return integerComparison;
        }


        /**
         * Compares {@code this} {@link VersionToken} with another {@link VersionToken}.
         *
         * @param other The {@link VersionToken} to use to compare.
         * @return {@code true} if {@code this} {@link VersionToken} is after the given {@link VersionToken}
         * @since 1.2.0
         */
        public boolean after(VersionToken other) {
            if (isIntegerComparison() && other.isIntegerComparison()) {
                return getVersionInteger() > other.getVersionInteger();
            } else {
                return getVersionString().compareTo(other.getVersionString()) > 0;
            }
        }

        /**
         * Compares {@code this} {@link VersionToken} with another {@link VersionToken}.
         *
         * @param other The {@link VersionToken} to use to compare.
         * @return {@code true} if {@code this} {@link VersionToken} matches the given {@link VersionToken}
         * @since 1.2.0
         */
        public boolean matches(VersionToken other) {
            if (isIntegerComparison() && other.isIntegerComparison()) {
                return getVersionInteger().equals(other.getVersionInteger());
            } else {
                return getVersionString().compareTo(other.getVersionString()) == 0;
            }
        }


        /**
         * Compares {@code this} {@link VersionToken} with another {@link VersionToken}.
         *
         * @param other The {@link VersionToken} to use to compare.
         * @return {@code true} if {@code this} {@link VersionToken} is before the given {@link VersionToken}
         * @since 1.2.0
         */
        public boolean before(VersionToken other) {
            if (isIntegerComparison() && other.isIntegerComparison()) {
                return getVersionInteger() < other.getVersionInteger();
            } else {
                return getVersionString().compareTo(other.getVersionString()) < 0;
            }
        }

        @Override
        public String toString() {
            return getVersionString();
        }
    }
}
