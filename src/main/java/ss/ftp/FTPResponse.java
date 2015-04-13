package ss.ftp;

public class FTPResponse {
    private final int code;
    private final String text;
    private final boolean multiline;

    public FTPResponse(String answer) {
        String num = extractNumber(answer);
        this.code = Integer.parseInt(num);

        int pos = num.length();
        this.multiline = answer.charAt(pos) == '-';
        this.text = answer.substring(pos + 1).trim();
    }

    public int getCode() {
        return code;
    }
    public String getText() {
        return text;
    }
    public boolean isMultiline() {
        return multiline;
    }

    private String extractNumber(String str) {
        StringBuilder res = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if ('0' <= ch && ch <= '9') {
                res.append(ch);
            } else {
                break;
            }
        }
        return res.length() == 0 ? "0" : res.toString();
    }
}