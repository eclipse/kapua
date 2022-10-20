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
package org.eclipse.kapua.commons.cache;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;


@Category(JUnitTests.class)
public class LocalCacheTest {

    @Test
    public void localCacheTest1() {
        int[] sizeMaxList = new int[]{0, 1, 10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{0, 1, 10, 1000, 10000, 2147483647};
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        int invalidNegativeSizeMax = -1;
        int invalidNegativeExpireAfter = -1;
        IllegalArgumentException illegalArgumentExceptionSizeMax = new IllegalArgumentException("maximum size must not be negative");
        IllegalArgumentException illegalArgumentExceptionExpireAfter = new IllegalArgumentException("duration cannot be negative: " + invalidNegativeExpireAfter + " SECONDS");

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache.get(defaultValueList));
                }
            }
        }
        for (int expireAfter : expireAfterList) {
            for (Object defaultValue : defaultValueList) {
                try {
                    LocalCache<Object, Object> localCache = new LocalCache<>(invalidNegativeSizeMax, expireAfter, defaultValue);
                } catch (Exception e) {
                    Assert.assertEquals("Expected IllegalArgumentException: maximum size must not be negative.", illegalArgumentExceptionSizeMax.toString(), e.toString());
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                try {
                    LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, invalidNegativeExpireAfter, defaultValue);
                } catch (Exception e) {
                    Assert.assertEquals("Expected java.lang.IllegalArgumentException: duration cannot be negative: " + invalidNegativeExpireAfter + " SECONDS", illegalArgumentExceptionExpireAfter.toString(), e.toString());
                }
            }
        }
    }

    @Test
    public void localCacheTest2() {
        int[] sizeMaxList = new int[]{0, 10, 1000, 10000, 2147483647};
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        int invalidNegativeSizeMax = -1;
        IllegalArgumentException illegalArgumentExceptionSizeMax = new IllegalArgumentException("maximum size must not be negative");

        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, defaultValue);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache.get(defaultValueList));
            }
        }
        for (Object defaultValue : defaultValueList) {
            try {
                LocalCache<Object, Object> localCache = new LocalCache<>(invalidNegativeSizeMax, defaultValue);
            } catch (Exception e) {
                Assert.assertEquals("Expected java.lang.IllegalArgumentException: maximum size must not be negative", illegalArgumentExceptionSizeMax.toString(), e.toString());
            }
        }
    }

    @Test
    public void setNamespaceTest() {
        int[] sizeMaxList = new int[]{0, 10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{0, 10, 1000, 10000, 2147483647};
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        String namespace = "namespace";

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    Assert.assertNull("Null expected", localCache1.getNamespace());
                    localCache1.setNamespace(namespace);
                    Assert.assertEquals("Expected and actual values should be the same.", namespace, localCache1.getNamespace());
                    localCache1.setNamespace(null);
                    Assert.assertNull("Null expected", localCache1.getNamespace());
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache2 = new LocalCache<>(sizeMax, defaultValue);
                Assert.assertNull("Null expected", localCache2.getNamespace());
                localCache2.setNamespace(namespace);
                Assert.assertEquals("Expected and actual values should be the same.", namespace, localCache2.getNamespace());
                localCache2.setNamespace(null);
                Assert.assertNull("Null expected", localCache2.getNamespace());
            }
        }
    }

    @Test
    public void getTest() {
        int[] sizeMaxList = new int[]{2, 10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{1, 10, 1000, 10000, 2147483647};
        int zeroSizeMax = 0;
        int oneSizeMax = 1;
        int zeroExpireAfter = 0;
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        String[] key = {"Key", "Second Key"};
        String[] keyValue = {"Key Value", "Second Key Value"};

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                    localCache1.put(key[0], keyValue[0]);
                    Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache1.get("Key"));
                    localCache1.put(key[1], keyValue[1]);
                    Assert.assertEquals("Expected and actual values should be the same.", keyValue[1], localCache1.get("Second Key"));
                    Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache1.get("Key"));
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, zeroExpireAfter, defaultValue);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                localCache1.put(key[0], keyValue[0]);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                localCache1.put(key[1], keyValue[1]);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Second Key"));
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
            }
        }
        for (int expireAfter : expireAfterList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(zeroSizeMax, expireAfter, defaultValue);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                localCache1.put(key[0], keyValue[0]);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                localCache1.put(key[1], keyValue[1]);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Second Key"));
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
            }
        }
        for (int expireAfter : expireAfterList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(oneSizeMax, expireAfter, defaultValue);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
                localCache1.put(key[0], keyValue[0]);
                Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache1.get("Key"));
                localCache1.put(key[1], keyValue[1]);
                Assert.assertEquals("Expected and actual values should be the same.", keyValue[1], localCache1.get("Second Key"));
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get("Key"));
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache2 = new LocalCache<>(sizeMax, defaultValue);
                Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
                localCache2.put(key[0], keyValue[0]);
                Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache2.get("Key"));
                localCache2.put(key[1], keyValue[1]);
                Assert.assertEquals("Expected and actual values should be the same.", keyValue[1], localCache2.get("Second Key"));
                Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache2.get("Key"));
            }
        }
        for (Object defaultValue : defaultValueList) {
            LocalCache<Object, Object> localCache2 = new LocalCache<>(zeroSizeMax, defaultValue);
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
            localCache2.put(key[0], keyValue[0]);
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
            localCache2.put(key[1], keyValue[1]);
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Second Key"));
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
        }
        for (Object defaultValue : defaultValueList) {
            LocalCache<Object, Object> localCache2 = new LocalCache<>(oneSizeMax, defaultValue);
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
            localCache2.put(key[0], keyValue[0]);
            Assert.assertEquals("Expected and actual values should be the same.", keyValue[0], localCache2.get("Key"));
            localCache2.put(key[1], keyValue[1]);
            Assert.assertEquals("Expected and actual values should be the same.", keyValue[1], localCache2.get("Second Key"));
            Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get("Key"));
        }
    }

    @Test
    public void getAllKeysTest() {
        int[] sizeMaxList = new int[]{10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{1, 10, 1000, 10000, 2147483647};
        int zeroExpireAfter = 0;
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        String[] key = {"Key", "Second Key", "Third Key"};
        String[] keyValue = {"Key Value", "Second Key Value", "Third Key Value"};
        ArrayList<String> expectedKeyList = new ArrayList<>();
        expectedKeyList.add("Second Key");
        expectedKeyList.add("Third Key");
        expectedKeyList.add("Key");

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    localCache1.put(key[0], keyValue[0]);
                    localCache1.put(key[1], keyValue[1]);
                    localCache1.put(key[2], keyValue[2]);
                    Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache1.getAllKeys());
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, zeroExpireAfter, defaultValue);
                localCache1.put(key[0], keyValue[0]);
                localCache1.put(key[1], keyValue[1]);
                localCache1.put(key[2], keyValue[2]);
                Assert.assertEquals("Empty list expected.", new ArrayList<>(), localCache1.getAllKeys());
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, defaultValue);
                localCache.put(key[0], keyValue[0]);
                localCache.put(key[1], keyValue[1]);
                localCache.put(key[2], keyValue[2]);
                Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache.getAllKeys());
            }
        }
    }

    @Test
    public void putTest() {
        int[] sizeMaxList = new int[]{1, 10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{1, 10, 1000, 10000, 2147483647};
        int zeroExpireAfter = 0;
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        String[] key = {"Key", "Second Key", "Third Key"};
        String[] keyValue = {"Key Value", "Second Key Value", "Third Key Value"};

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    for (int k = 0; k < key.length; k++) {
                        localCache1.put(key[k], keyValue[k]);
                        Assert.assertEquals("Expected and actual values should be the same.", keyValue[k], localCache1.get(key[k]));
                    }
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, zeroExpireAfter, defaultValue);
                for (int k = 0; k < key.length; k++) {
                    localCache1.put(key[k], keyValue[k]);
                    Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get(key[k]));
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, defaultValue);
                for (int k = 0; k < key.length; k++) {
                    localCache.put(key[k], keyValue[k]);
                    Assert.assertEquals("Expected and actual values should be the same.", keyValue[k], localCache.get(key[k]));
                }
            }
        }
    }

    @Test
    public void removeTest() {
        int[] sizeMaxList = new int[]{10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{1, 10, 1000, 10000, 2147483647};
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        int zeroExpireAfter = 0;
        String[] key = {"Key", "Second Key", "Third Key"};
        String[] keyValue = {"Key Value", "Second Key Value", "Third Key Value"};

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    localCache1.put(key[2], keyValue[2]);
                    localCache1.put(key[1], keyValue[1]);
                    localCache1.put(key[0], keyValue[0]);

                    ArrayList<String> expectedKeyList = new ArrayList<>();
                    expectedKeyList.add("Second Key");
                    expectedKeyList.add("Third Key");
                    expectedKeyList.add("Key");

                    localCache1.remove("Key");
                    expectedKeyList.remove("Key");
                    Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache1.getAllKeys());
                    localCache1.remove("Second Key");
                    expectedKeyList.remove("Second Key");
                    Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache1.getAllKeys());
                    localCache1.remove("Third Key");
                    expectedKeyList.remove("Third Key");
                    Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache1.getAllKeys());
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, zeroExpireAfter, defaultValue);
                localCache1.put(key[2], keyValue[2]);
                localCache1.put(key[1], keyValue[1]);
                localCache1.put(key[0], keyValue[0]);

                ArrayList<String> expectedKeyList = new ArrayList<>();
                expectedKeyList.add("Second Key");
                expectedKeyList.add("Third Key");
                expectedKeyList.add("Key");

                localCache1.remove("Key");
                expectedKeyList.remove("Key");
                Assert.assertEquals("Empty list expected.", new ArrayList<>(), localCache1.getAllKeys());
                localCache1.remove("Second Key");
                expectedKeyList.remove("Second Key");
                Assert.assertEquals("Empty list expected.", new ArrayList<>(), localCache1.getAllKeys());
                localCache1.remove("Third Key");
                expectedKeyList.remove("Third Key");
                Assert.assertEquals("Empty list expected.", new ArrayList<>(), localCache1.getAllKeys());
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache = new LocalCache<>(sizeMax, defaultValue);
                localCache.put(key[2], keyValue[2]);
                localCache.put(key[1], keyValue[1]);
                localCache.put(key[0], keyValue[0]);

                ArrayList<String> expectedKeyList = new ArrayList<>();
                expectedKeyList.add("Second Key");
                expectedKeyList.add("Third Key");
                expectedKeyList.add("Key");

                localCache.remove("Key");
                expectedKeyList.remove("Key");
                Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache.getAllKeys());
                localCache.remove("Second Key");
                expectedKeyList.remove("Second Key");
                Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache.getAllKeys());
                localCache.remove("Third Key");
                expectedKeyList.remove("Third Key");
                Assert.assertEquals("Expected and actual values should be the same.", expectedKeyList, localCache.getAllKeys());
            }
        }
    }

    @Test
    public void invalidateAllTest() {
        int[] sizeMaxList = new int[]{1, 10, 1000, 10000, 2147483647};
        int[] expireAfterList = new int[]{1, 10, 1000, 10000, 2147483647};
        Object[] defaultValueList = new Object[]{0, 10, 100000, "String", 'c', -10, -1000000000, 1000000000000L, 1000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false,};
        String[] key = {"Key", "Second Key", "Third Key"};
        String[] keyValue = {"Key Value", "Second Key Value", "Third Key Value"};

        for (int sizeMax : sizeMaxList) {
            for (int expireAfter : expireAfterList) {
                for (Object defaultValue : defaultValueList) {
                    LocalCache<Object, Object> localCache1 = new LocalCache<>(sizeMax, expireAfter, defaultValue);
                    for (int k = 0; k < key.length; k++) {
                        localCache1.put(key[k], keyValue[k]);
                        Assert.assertEquals("Expected and actual values should be the same.", keyValue[k], localCache1.get(key[k]));
                        localCache1.invalidateAll();
                        Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache1.get(key[k]));
                    }
                }
            }
        }
        for (int sizeMax : sizeMaxList) {
            for (Object defaultValue : defaultValueList) {
                LocalCache<Object, Object> localCache2 = new LocalCache<>(sizeMax, defaultValue);
                for (int k = 0; k < key.length; k++) {
                    localCache2.put(key[k], keyValue[k]);
                    Assert.assertEquals("Expected and actual values should be the same.", keyValue[k], localCache2.get(key[k]));
                    localCache2.invalidateAll();
                    Assert.assertEquals("Expected and actual values should be the same.", defaultValue, localCache2.get(key[k]));
                }
            }
        }
    }
}   
