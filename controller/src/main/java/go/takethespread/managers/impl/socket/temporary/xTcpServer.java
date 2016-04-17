package go.takethespread.managers.impl.socket.temporary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class xTcpServer extends Thread {

    Socket socket;

    public xTcpServer() {
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8085, 0, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
                try {
                    socket = serverSocket.accept();
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    byte[] buff = new byte[64 * 1024];
                    int r = is.read(buff);
                    String data = new String(buff, 0, r);
                    if (data.equals("GO")) {
                        os.write("OKAY, I'M READY, MAN!".getBytes());
                    } else if (data.equals("GJ")) {
                        os.write("WAITING NOW...".getBytes());
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
