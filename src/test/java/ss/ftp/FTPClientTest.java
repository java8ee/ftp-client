package ss.ftp;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class FTPClientTest {
    private static Properties properties;
    private FTPClient ftp;

    @BeforeClass
    public static void loadSettings() throws IOException {
        properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("ftp.properties"));
    }

    @Before
    public void setUp() throws IOException {
        ftp = new FTPClient(getString("host"), getInt("port"), getString("user"), getString("password"));
        ftp.connect();
    }

    @After
    public void tearDown() throws IOException {
        ftp.close();
    }

    @Test
    public void testSize() throws IOException {
        String filename = getString("filename");
        long size = ftp.size(filename);
        assertEquals("Incorrect file size for filename = " + filename, getLong("filesize"), size);
    }

    @Test
    public void testSizeNonExistentFile() throws IOException {
        String filename = "nonexist" + getString("filename");
        long size = ftp.size(filename);
        assertEquals("Incorrect file size for filename = " + filename, -1, size);
    }


    private static String getString(String suffix) {
        return (String) properties.get("ftp." + suffix);
    }
    private static int getInt(String suffix) {
        return Integer.parseInt(getString(suffix));
    }
    private static long getLong(String suffix) {
        return Long.parseLong(getString(suffix));
    }
}