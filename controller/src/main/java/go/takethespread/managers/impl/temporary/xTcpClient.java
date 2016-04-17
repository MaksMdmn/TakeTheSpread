package go.takethespread.managers.impl.temporary;

import java.io.IOException;
import java.net.Socket;

public class xTcpClient extends Thread {

    private Socket socket;
    private String host;
    private int port;

    public xTcpClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] b) {
        try {
            socket.getOutputStream().write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] read() {
        byte buff[] = new byte[64 * 1024];
        try {
            socket.getInputStream().read(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff;
    }


}
