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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaIllegalArgumentExceptionTest {

    String[] argumentName;
    String[] argumentValue;
    KapuaErrorCodes[] kapuaErrorCodes;

    @Before
    public void initialize() {
        argumentName = new String[]{"Argument Name", null};
        argumentValue = new String[]{"Argument Value", null};
        kapuaErrorCodes = new KapuaErrorCodes[]{
                KapuaErrorCodes.ENTITY_NOT_FOUND,
                KapuaErrorCodes.ENTITY_ALREADY_EXISTS,
                KapuaErrorCodes.DUPLICATE_NAME,
                KapuaErrorCodes.DUPLICATE_EXTERNAL_ID,
                KapuaErrorCodes.ENTITY_UNIQUENESS,
                KapuaErrorCodes.ILLEGAL_ACCESS,
                KapuaErrorCodes.ILLEGAL_ARGUMENT,
                KapuaErrorCodes.ILLEGAL_NULL_ARGUMENT,
                KapuaErrorCodes.ILLEGAL_STATE,
                KapuaErrorCodes.OPTIMISTIC_LOCKING,
                KapuaErrorCodes.UNAUTHENTICATED,
                KapuaErrorCodes.OPERATION_NOT_SUPPORTED,
                KapuaErrorCodes.INTERNAL_ERROR,
                KapuaErrorCodes.SEVERE_INTERNAL_ERROR,
                KapuaErrorCodes.PARENT_LIMIT_EXCEEDED_IN_CONFIG,
                KapuaErrorCodes.SUBJECT_UNAUTHORIZED,
                KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
                KapuaErrorCodes.EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT,
                KapuaErrorCodes.MAX_NUMBER_OF_ITEMS_REACHED,
                KapuaErrorCodes.DEVICE_NOT_FOUND,
                KapuaErrorCodes.ADMIN_ROLE_DELETED_ERROR,
                KapuaErrorCodes.PERMISSION_DELETE_NOT_ALLOWED,
                KapuaErrorCodes.SERVICE_DISABLED
        };
    }

    @Test
    public void kapuaIllegalArgumentExceptionStringParametersTest() {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                KapuaIllegalArgumentException kapuaIllegalArgumentException = new KapuaIllegalArgumentException(name, value);
                Assert.assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ILLEGAL_ARGUMENT, kapuaIllegalArgumentException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", name, kapuaIllegalArgumentException.getArgumentName());
                Assert.assertEquals("Expected and actual values should be the same.", value, kapuaIllegalArgumentException.getArgumentValue());
                Assert.assertEquals("Expected and actual values should be the same.", "An illegal value was provided for the argument " + name + ": " + value + ".", kapuaIllegalArgumentException.getMessage());
                Assert.assertNull("Null expected.", kapuaIllegalArgumentException.getCause());
            }
        }
    }

    @Test
    public void kapuaIllegalArgumentExceptionKapuaErrorCodesStringParametersTest() {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                String[] message = {
                        "The entity of type " + name + " with id/name " + value + " was not found.",
                        "Error: " + name + ", " + value,
                        "An entity with the same name " + name + " already exists.",
                        "An entity with the same external Id " + name + " already exists.",
                        "Error: " + name + ", " + value,
                        "The current subject is not authorized for " + name + ".",
                        "An illegal value was provided for the argument " + name + ": " + value + ".",
                        "An illegal null value was provided for the argument " + name + ".",
                        "The application is in an illegal state: " + name + ".",
                        "The entity is out of state as it has been modified or deleted by another transaction.",
                        "No authenticated Subject found in context.",
                        "Error: " + name + ", " + value,
                        "An internal error occurred: " + name + ".",
                        "Error: " + name + ", " + value,
                        "Maximum number of items has been exceeded,",
                        "User does not have permission to perform this action. Required permission: " + name + ".",
                        "An entity with the same name " + name + " already exists.",
                        "An entity with the same external id " + name + " already exists in another account.",
                        "Max number of " + name + " reached. Please increase the number or set InfiniteChild" + name + " parameter to True.",
                        "The selected devices were not found. Please refresh device list.",
                        "Operation not allowed on admin role.",
                        "Operation not allowed on this specific permission.",
                        "The Service is disabled: " + name
                };

                for (int i = 0; i < kapuaErrorCodes.length; i++) {
                    KapuaIllegalArgumentException kapuaIllegalArgumentException = new KapuaIllegalArgumentException(kapuaErrorCodes[i], name, value);
                    Assert.assertEquals("Expected and actual values should be the same for Kapua ErrorCode: " + kapuaErrorCodes[i], kapuaErrorCodes[i], kapuaIllegalArgumentException.getCode());
                    Assert.assertEquals("Expected and actual values should be the same for Kapua ErrorCode: " + kapuaErrorCodes[i], name, kapuaIllegalArgumentException.getArgumentName());
                    Assert.assertEquals("Expected and actual values should be the same for Kapua ErrorCode: " + kapuaErrorCodes[i], value, kapuaIllegalArgumentException.getArgumentValue());
                    Assert.assertEquals("Expected and actual values should be the same for Kapua ErrorCode: " + kapuaErrorCodes[i], message[i], kapuaIllegalArgumentException.getMessage());
                    Assert.assertNull("Null expected for KapuaErrorCode: " + kapuaErrorCodes[i], kapuaIllegalArgumentException.getCause());
                }
            }
        }
    }

    @Test
    public void kapuaIllegalArgumentExceptionNullKapuaErrorCodesStringParametersTest() {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                KapuaIllegalArgumentException kapuaIllegalArgumentException = new KapuaIllegalArgumentException(null, name, value);
                Assert.assertNull("Null expected.", kapuaIllegalArgumentException.getCode());
                Assert.assertEquals("Expected and actual values should be the same.", name, kapuaIllegalArgumentException.getArgumentName());
                Assert.assertEquals("Expected and actual values should be the same.", value, kapuaIllegalArgumentException.getArgumentValue());
                Assert.assertNull("Null expected.", kapuaIllegalArgumentException.getCause());
                Assert.assertEquals(String.format("Error: %s, %s", name, value), kapuaIllegalArgumentException.getMessage());
            }
        }
    }

    @Test
    public void throwingExceptionStringParametersTest() throws KapuaIllegalArgumentException {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                try {
                    throw new KapuaIllegalArgumentException(name, value);
                } catch (KapuaIllegalArgumentException ex) {
                    Assert.assertEquals(
                            String.format("An illegal value was provided for the argument %s: %s.", name, value),
                            ex.getMessage());
                } catch (Throwable t) {
                    Assert.fail();
                }
            }
        }
    }

    @Test
    public void throwingExceptionKapuaErrorCodesStringParametersTest() throws KapuaIllegalArgumentException {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                for (KapuaErrorCodes code : kapuaErrorCodes) {
                    try {
                        throw new KapuaIllegalArgumentException(code, name, value);
                    } catch (KapuaIllegalArgumentException ex) {
                        //We just want to check that this type of exception is thrown two lines above... very low value test
                        //correct error messages are tested separately above
                        Assert.assertTrue(true);
                    } catch (Throwable t) {
                        Assert.fail();
                    }
                }
            }
        }
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void throwingExceptionNullKapuaErrorCodesStringParametersTest() throws KapuaIllegalArgumentException {
        for (String name : argumentName) {
            for (String value : argumentValue) {
                throw new KapuaIllegalArgumentException(null, name, value);
            }
        }
    }
}
