package com.hazelcast.config;

import com.hazelcast.core.RingbufferStore;
import com.hazelcast.core.RingbufferStoreFactory;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.ringbuffer.impl.RingbufferStoreWrapper;
import com.hazelcast.spi.serialization.SerializationService;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Properties;

import static com.hazelcast.config.InMemoryFormat.OBJECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelTest.class})
public class RingbufferStoreConfigTest {

    private RingbufferStoreConfig config = new RingbufferStoreConfig();

    @Test
    public void testDefaultSetting() {
        assertTrue(config.isEnabled());
        assertNull(config.getClassName());
        assertNull(config.getFactoryClassName());
        assertNull(config.getFactoryImplementation());
        assertNull(config.getStoreImplementation());
        assertNotNull(config.getProperties());
        assertTrue(config.getProperties().isEmpty());
    }

    @Test
    public void setStoreImplementation() {
        SerializationService serializationService = new DefaultSerializationServiceBuilder().build();
        RingbufferStore<Data> store = RingbufferStoreWrapper.create("name", config, OBJECT, serializationService, null);

        config.setStoreImplementation(store);

        assertEquals(store, config.getStoreImplementation());
    }

    @Test
    public void setProperties() {
        Properties properties = new Properties();
        properties.put("key", "value");

        config.setProperties(properties);

        assertEquals(properties, config.getProperties());
    }

    @Test
    public void setProperty() {
        config.setProperty("key", "value");

        assertEquals("value", config.getProperty("key"));
    }

    @Test
    public void setFactoryClassName() {
        config.setFactoryClassName("myFactoryClassName");

        assertEquals("myFactoryClassName", config.getFactoryClassName());
    }

    @Test
    public void setFactoryImplementation() {
        RingbufferStoreFactory factory = new RingbufferStoreFactory() {
            @Override
            public RingbufferStore newRingbufferStore(String name, Properties properties) {
                return null;
            }
        };

        config.setFactoryImplementation(factory);

        assertEquals(factory, config.getFactoryImplementation());
    }
}