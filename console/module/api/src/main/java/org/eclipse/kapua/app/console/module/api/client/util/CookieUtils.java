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
package org.eclipse.kapua.app.console.module.api.client.util;

import java.util.Date;

import com.google.gwt.user.client.Cookies;

/**
 *
 * CookieUtils
 *
 * Cookie management utils class for the Gwt Client implementation.
 * It used for trust machine cookie support on the Gwt Client side.
 *
 */
public class CookieUtils {

    public static final String KAPUA_COOKIE_TRUST = "kapua-trust-";

    public static final int SECURITY_COOKIE_EXPIRES_DAY = 30;

    private String username;
    private int cookieExpiresDays;

    /**
     * Create the Cookie for the given username
     * <p>
     * It's possible to configure the security.cookie.expires_days related of the number of expires days
     * in the kapua-config.* configuration files.
     *
     * @param username
     */
    public CookieUtils(String username) {
        this.username = username;
        cookieExpiresDays = SECURITY_COOKIE_EXPIRES_DAY;
    }

    /**
     * Check if the given cookie exists and if its value is set
     *
     * @param cookieName
     * @return boolean
     */
    static public boolean isCookieEnabled(String cookieName) {
        boolean ret = false;
        String value = Cookies.getCookie(cookieName);
        if (value != null) {
            if (!"".equals(value)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * Returns the trust machine key saved on the client browser
     *
     * @return trust key
     */
    public String getTrustKeyCookie() {
        return Cookies.getCookie(KAPUA_COOKIE_TRUST + this.username);
    }

    private Date addDays(Date dateIn, int numDays) {
        long milisPerDay = 86400000;

        // convert the dateIn to milliseconds
        long dateInMilis = dateIn.getTime();

        // add numDays to the date
        dateInMilis = dateInMilis + (numDays * milisPerDay);

        return new Date(dateInMilis);
    }

    /**
     * Create the MFA / Trust machine cookie for the current user with the given trust_key value
     *
     * @param trustKey
     */
    public void createTrustCookie(String trustKey) {
        String cookieName = KAPUA_COOKIE_TRUST + this.username;

        // remove the old cookie if exists
        removeCookie(cookieName);

        Date expireDtm = addDays(new Date(), cookieExpiresDays);

        Cookies.setCookie(cookieName, trustKey, expireDtm);
    }

    /**
     * Remove the cookie from the user
     */
    static public void removeCookie(String cookieName) {
        Cookies.removeCookie(cookieName, "/");
    }

}
