/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *      Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.model.id;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.model.AbstractCommonServiceTest;
import org.eclipse.kapua.commons.model.misc.CollisionEntity;
import org.eclipse.kapua.commons.model.misc.CollisionEntityJpaRepository;
import org.eclipse.kapua.commons.model.misc.CollisionIdGenerator;
import org.eclipse.kapua.commons.model.misc.CollisionServiceImpl;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Test the random identifier generator retry mechanism.
 *
 * @since 1.0
 */
@Category(JUnitTests.class)
public class IdGeneratorTest extends AbstractCommonServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGeneratorTest.class);

    public static final String DEFAULT_TEST_PATH = "./src/test/sql/H2/";
    public static final String DEFAULT_TEST_FILTER = "test_*.sql";
    public static final String DEFAULT_COMMONS_PATH = "../commons";
    public static final String DROP_TEST_FILTER = "test_*_drop.sql";
    private static JpaTxManager txManager;
    private static CollisionEntityJpaRepository repo;
    private static final int MAX_RETRIES = 5;

    @BeforeClass
    public static void setUp() {
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL;DB_CLOSE_DELAY=-1", "kapua", "kapua").update();
        scriptSession(DEFAULT_TEST_PATH, DEFAULT_TEST_FILTER);
        txManager = new JpaTxManager(new KapuaEntityManagerFactory("kapua-commons-unit-test"));
        repo = new CollisionEntityJpaRepository(MAX_RETRIES);
    }

    @AfterClass
    public static void tearDown() {
        scriptSession(DEFAULT_TEST_PATH, DROP_TEST_FILTER);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
    }

    @Test
    /**
     * Test the key collision behavior through collision emulation.<br>
     * Test 2 login, the first succeed at the first attempt, the second will fail after third attempt (due to key violation exception)
     */
    public void testKeyCollision() {

        CollisionIdGenerator collisionIdGenerator = new CollisionIdGenerator("1000", new BigInteger("499"),
                MAX_RETRIES + 2);
        CollisionEntity.initializeCollisionIdGenerator(collisionIdGenerator);
        CollisionServiceImpl collisionServiceImpl = new CollisionServiceImpl();
        try {
            txManager.execute(tx -> {
                repo.create(tx, new CollisionEntity("Collision - first record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
                // this insert will fail for a SystemSettingKey.KAPUA_INSERT_MAX_RETRY count
                repo.create(tx, new CollisionEntity("Collision - second record"));
                Assert.fail("The insert should throws exception!");
                return Optional.empty();
            });
            // this insert creates the record with the correct id
        } catch (KapuaException e) {
            Assert.assertEquals("The generated random identifiers count is wrong!", MAX_RETRIES + 1,
                    collisionIdGenerator.getGeneretedValuesCount());
        }
    }

    @Test
    /**
     * Test the key collision behavior through collision emulation.<br>
     * Test 2 login, the first succeed at the first attempt, the second will succeed at the third attempt (so only 2 key violation will occur)
     */
    public void testKeyPartialCollision() {
        CollisionIdGenerator collisionIdGenerator = new CollisionIdGenerator("2000", new BigInteger("1499"),
                2);
        CollisionEntity.initializeCollisionIdGenerator(collisionIdGenerator);
        CollisionServiceImpl collisionServiceImpl = new CollisionServiceImpl();
        try {
            txManager.execute(tx -> {
                // this insert creates the record with the correct id
                repo.create(tx, new CollisionEntity("PartialCollision - first record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
                // this insert will fail for a SystemSettingKey.KAPUA_INSERT_MAX_RETRY count
                repo.create(tx, new CollisionEntity("PartialCollision - second record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 2,
                        collisionIdGenerator.getGeneretedValuesCount());
                return Optional.empty();
            });
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
            txManager.execute(tx -> {
                repo.create(tx, new CollisionEntity("NoKeyCollision - first record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 1, collisionIdGenerator.getGeneretedValuesCount());
                repo.create(tx, new CollisionEntity("NoKeyCollision - second record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 2, collisionIdGenerator.getGeneretedValuesCount());
                repo.create(tx, new CollisionEntity("NoKeyCollision - third record"));
                Assert.assertEquals("The generated random identifiers count is wrong!", 3, collisionIdGenerator.getGeneretedValuesCount());
                repo.create(tx, new CollisionEntity("NoKeyCollision - third record"));
                Assert.fail("The insert should throws exception!");
                return Optional.empty();
            });
        } catch (KapuaException e) {
            Assert.assertEquals("The generated random identifiers count is wrong!", MAX_RETRIES + 3,
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
