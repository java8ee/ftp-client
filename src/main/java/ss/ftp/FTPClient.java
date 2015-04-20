package ss.ftp;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class FTPClient {
    private final String host;
    private final int port;
    private final String user;
    private final String password;

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public FTPClient(String host, int port, String user, String password) {
        assert host != null;

        this.host = host;
        this.port = port == 0 ? 21 : port;
        this.user = user;
        this.password = password;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        response();
        request(FTPCommand.USER, user);
        response();
        request(FTPCommand.PASS, password);
        response();
    }

    public void close() {
        try {
            if (socket != null) {
                request(FTPCommand.QUIT);
                response();
                socket.close();
            }
        } catch(IOException cause) {
            // Skip exceptions
        } finally {
            socket = null;
        }
    }

    public long size(String filename) throws IOException {
        request(FTPCommand.SIZE, filename);
        FTPResponse response = response();
        return response.getCode() == 213 ? Long.parseLong(response.getText()) : -1;
    }

    public FTPFile[] list(String dir) throws IOException {
        Socket socket = openChannel();
        request(FTPCommand.LIST, dir == null ? "/" : dir);
        response();

        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

        byte[] buffer = new byte[1024];
        StringBuilder buf = new StringBuilder();
        int count;
        while ((count = in.read(buffer)) > 0) {
            buf.append(new String(buffer));
            Arrays.fill(buffer, (byte) 0);
        }
        in.close();
        socket.close();

        response();

        String[] lines = buf.toString().trim().split("\r\n");
        FTPFile[] files = new FTPFile[lines.length];
        for (int i = 0; i < lines.length; i++) {
            files[i] = new FTPFile(lines[i]);
        }

        return files;
    }

    private Socket openChannel() throws IOException {
        return passive();
    }

    private Socket passive() throws IOException {
        request(FTPCommand.SYST);
        response();
        request(FTPCommand.PWD);
        response();
        request(FTPCommand.TYPE, "I");
        response();

        request(FTPCommand.PASV);
        FTPResponse response = response();
        String text = response.getText();
        text = text.substring(text.indexOf('(') + 1, text.indexOf(')'));
        String[] buf = text.split(",");
        String ip = buf[0] + "." + buf[1] + "." + buf[2] + "." + buf[3];
        int port = (Integer.parseInt(buf[4]) << 8) | Integer.parseInt(buf[5]);
        log(ip + ":" + port, true);

        return new Socket(host, port);
    }

    private void request(FTPCommand command, String ... args) throws IOException {
        StringBuilder request = new StringBuilder(command.toString());
        for (String arg : args) {
            request.append(' ').append(arg);
        }
        log(request.toString(), false);

        out.write(request.toString() + "\r\n");
        out.flush();
    }

    private FTPResponse response() throws IOException {
        String str = in.readLine();
        log(str, true);
        return new FTPResponse(str);
    }

    private void log(String message, boolean in) {
        System.out.println((in ? "<<< " : ">>> ") + message);
    }
}