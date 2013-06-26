package oscilloscopeui;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ajarax
 */
public class DataStorageTest {

    public DataStorageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of storeValues method, of class DataStorage.
     */
    @Test
    public void testStoreValues() throws Exception {
        System.out.println("storeValues");
        String[][] values = {{"a", "b"}, {"b", "c"}};
        DataStorage instance = new DataStorage();
        for (String[] s : values) {
            instance.storeValues(s);
        }
        String test = "a,b".concat(System.lineSeparator())
                .concat("b,c").concat(System.lineSeparator());
        instance.close();
        File f = instance.getStorageFile();
        byte[] encoded = Files.readAllBytes(f.toPath());
        String real = Charset.defaultCharset()
                .decode(ByteBuffer.wrap(encoded)).toString();
        f.delete();
        assertEquals(test, real);
    }
}