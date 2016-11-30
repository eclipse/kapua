package org.eclipse.kapua.service.user;

/** 
 * The device user type
 */
public enum UserType {
    /** 
     * Device user type
     */
    DEVICE,
    
    /** 
     * Internal user type (user credentials from Kapua)
     */
    INTERNAL,
    
    /** 
     * External user type (user credentials from SSO)
     */
    EXTERNAL
}
