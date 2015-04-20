package ss.ftp;

import org.junit.Assert;
import org.junit.Test;

public class FTPFileTest {
    @Test
    public void testSimpleParsing() {
        FTPFile file = new FTPFile("-rw-r--r--    1 502      503          1024 Jan 01 00:00 file.txt");
        Assert.assertEquals("File name: ", "file.txt", file.getName());
        Assert.assertEquals("File size: ", 1024, file.getSize());
    }

    @Test
    public void testLongLineParsing() {
        FTPFile file = new FTPFile("-rw-r--r--    1 502      503      153024206 Apr 07 08:55 video.mp4");
        Assert.assertEquals("File name: ", "video.mp4", file.getName());
        Assert.assertEquals("File size: ", 153024206, file.getSize());
    }
}