package ss.ftp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FTPFile {
    private long size;
    private String name = "";

    public FTPFile(String line) {
        Pattern regex = Pattern.compile("(\\d+)\\s+\\w{3}\\s+\\d{1,2}\\s+[\\d:]{4,5}\\s+(.*)");
        Matcher matcher = regex.matcher(line);
        if (matcher.find()) {
            this.size = Long.parseLong(matcher.group(1));
            this.name = matcher.group(2);
        }
    }

    public long getSize() {
        return size;
    }
    public String getName() {
        return name;
    }
}