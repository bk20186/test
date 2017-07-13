import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created by adzyuba on 13.07.2017.
 */

public class BinaryObjectTest {
    private Ignite ignite;

    private IgniteCache<BinaryObject, BinaryObject> cache;

    private BinaryObject key1;

    @Before
    public void setup() {
        ignite = Ignition.start("ignite.xml");
        cache = ignite.getOrCreateCache("test_cache");

        BinaryObjectBuilder keyBuilder = ignite.binary().builder("key");
        keyBuilder.setField("key1", "abcd", String.class);
        keyBuilder.setField("key2", 123L, Long.class);
        key1 = keyBuilder.build();

        BinaryObjectBuilder valueBuilder = ignite.binary().builder("value");
        valueBuilder.setField("value1", "v1");
        valueBuilder.setField("value2", 1);

        cache.put(key1, valueBuilder.build());
    }

    @After
    public void tearDown() {
        ignite.close();
    }

    @Test
    public void test() {
        BinaryObjectBuilder keyBuilder = ignite.binary().builder("key");
        keyBuilder.setField("key2", 123L);
        keyBuilder.setField("key1", "abcd");
        BinaryObject key2 = keyBuilder.build();
        Assert.assertThat(key1, is(key2));
        Assert.assertNotNull(cache.get(key2));
    }
}
