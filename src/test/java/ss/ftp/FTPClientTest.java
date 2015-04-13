package ss.ftp;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class FTPClientTest {
    private static String host;
    private static int port;
    private static String user;
    private static String password;

    @BeforeClass
    public static void setUp() throws IOException {
        // Load FTP properties
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("ftp.properties"));
        host = (String) properties.get("ftp.host");
        port = Integer.parseInt((String) properties.get("ftp.port"));
        user = (String) properties.get("ftp.user");
        password = (String) properties.get("ftp.password");
    }

    @Test
    public void testConnection() throws IOException {
        FTPClient ftp = new FTPClient(host, port, user, password);
        ftp.connect();
        ftp.close();
    }
}