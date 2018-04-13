/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *      Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.model;

import java.math.BigInteger;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.misc.CollisionEntity;
import org.eclipse.kapua.commons.model.misc.CollisionIdGenerator;
import org.eclipse.kapua.commons.model.misc.CollisionServiceImpl;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the random identifier generator retry mechanism.
 *
 * @since 1.0
 *
 */
public class KapuaIdGeneratorTest extends AbstractCommonServiceTest {

    public static final String DEFAULT_TEST_FILTER = "test_*.sql";

    @BeforeClass
    public static void tearUp()
            throws KapuaException {
        scriptSession(DEFAULT_TEST_PATH, DEFAULT_TEST_FILTER);
    }

    @Test
    /**
     * Test the key collision behavior through collision emulation.<br>
     * Test 2 login, the first succeed at the first attempt, the second will fail after third attempt (due to key violation exception)
     */
    public void testKeyCollision() {
        CollisionIdGenerator collisionIdGenerator = new CollisionIdGenerator("1000", new BigInteger("499"), 5);
        CollisionEntity.initializeCollisionIdGenerator(collisionIdGenerator);
        CollisionServiceImpl collisionServiceImpl = new CollisionServiceImpl();
        try {
            // this insert creates the record with the correct id
            collisionServiceImpl.insert("Collision - first record");
            Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
            // this insert will fail for a SystemSettingKey.KAPUA_INSERT_MAX_RETRY count
            collisionServiceImpl.insert("Collision - second record");
            Assert.fail("The insert should throws exception!");
        } catch (KapuaException e) {
            Assert.assertEquals("The generated random identifiers count is wrong!", SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY) + 1,
                    collisionIdGenerator.getGeneretedValuesCount());
        }
    }

    @Test
    /**
     * Test the key collision behavior through collision emulation.<br>
     * Test 2 login, the first succeed at the first attempt, the second will succeed at the third attempt (so only 2 key violation will occur)
     */
    public void testKeyPartialCollision() {
        CollisionIdGenerator collisionIdGenerator = new CollisionIdGenerator("2000", new BigInteger("1499"), 4);
        CollisionEntity.initializeCollisionIdGenerator(collisionIdGenerator);
        CollisionServiceImpl collisionServiceImpl = new CollisionServiceImpl();
        try {
            // this insert creates the record with the correct id
            collisionServiceImpl.insert("PartialCollision - first record");
            Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
            // this insert will fail for a SystemSettingKey.KAPUA_INSERT_MAX_RETRY count
            collisionServiceImpl.insert("PartialCollision - second record");
            Assert.assertEquals("The generated random identifiers count is wrong!", SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY) + 1,
                    collisionIdGenerator.getGeneretedValuesCount());
        } catch (KapuaException e) {
            Assert.fail("The insert shouldn't throws exception!");
        }
    }

    @Test
    /**
     * Test the key collision behavior through collision emulation.<br>
     * Test 4 login, the first 3 succeed at the first attempt, the 4th will fail 3 times due to a duplicated unique field exception (not a table key)
     */
    public void testKeyNoKeyCollision() {
        CollisionIdGenerator collisionIdGenerator = new CollisionIdGenerator("3000", new BigInteger("2499"), 0);
        CollisionEntity.initializeCollisionIdGenerator(collisionIdGenerator);
        CollisionServiceImpl collisionServiceImpl = new CollisionServiceImpl();
        try {
            collisionServiceImpl.insert("NoKeyCollision - first record");
            Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
            collisionServiceImpl.insert("NoKeyCollision - second record");
            Assert.assertEquals("The generated random identifiers count is wrong!", 2, collisionIdGenerator.getGeneretedValuesCount());
            collisionServiceImpl.insert("NoKeyCollision - third record");
            Assert.assertEquals("The generated random identifiers count is wrong!", 3, collisionIdGenerator.getGeneretedValuesCount());
            collisionServiceImpl.insert("NoKeyCollision - third record");
            Assert.fail("The insert should throws exception!");
        } catch (KapuaException e) {
            Assert.assertEquals("The generated random identifiers count is wrong!", SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY) + 3,
                    collisionIdGenerator.getGeneretedValuesCount());
        }
    }

    @Test
    /**
     * Just generate few ids and check if the numbers are fitted into the expected limits
     * 
     * @throws Exception
     */
    public void testIdGeneratorBound() throws Exception {
        int idSize = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_KEY_SIZE);
        BigInteger upperLimit = BigInteger.valueOf(2).pow(idSize);
        for (int i = 0; i < 1000; i++) {
            BigInteger generated = IdGenerator.generate();
            Assert.assertFalse("The generated id is out of the expected bounds!", generated.compareTo(BigInteger.ZERO) < 0 || generated.compareTo(upperLimit) != -1);
        }
    }
}
