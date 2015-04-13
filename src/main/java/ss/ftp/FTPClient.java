package ss.ftp;

import java.io.*;
import java.net.Socket;

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

    public void close() throws IOException {
        try {
            if (socket != null) {
                request(FTPCommand.QUIT);
                response();
                socket.close();
            }
        } finally {
            socket = null;
        }
    }

    public long size(String filename) throws IOException {
        request(FTPCommand.SIZE, filename);
        FTPResponse response = response();
        return response.getCode() == 213 ? Long.parseLong(response.getText()) : -1;
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