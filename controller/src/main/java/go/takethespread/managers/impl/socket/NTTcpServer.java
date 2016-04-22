package go.takethespread.managers.impl.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NTTcpServer {

    private NTTcpDataBridge dataBridge;
    private Socket socket;
    private ServerSocket serverSocket;
    private volatile boolean isConn;
    private BufferedReader br;
    private PrintWriter pw;
    private Thread reading;
    private Thread writing;

    public NTTcpServer() {
        dataBridge = NTTcpDataBridge.getInstance();
        isConn = true;
        try {
            serverSocket = new ServerSocket(8085, 0, InetAddress.getByName("localhost"));
            if (socket == null) {
                socket = serverSocket.accept();
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reading = initReading();
        this.writing = initWriting();
    }

    public NTTcpDataBridge getDataBridge() {
        return dataBridge;
    }

    public void initServerWork() {
        reading.start();
        writing.start();
    }

    public void shutDown() {
        isConn = false;
        closeAllEntities();
    }

    private Thread initReading() {
        return new Thread(() -> {
            while (isConn) {
                try {
                    while (br.ready()) {
                        System.out.println("reading...");
                        String data = br.readLine();
                        System.out.println("data: " + data);
                        dataBridge.addData(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Thread initWriting() {
        return new Thread(() -> {
            while (isConn) {
                while (dataBridge.haveMessage()) {
                    String message = dataBridge.acceptMessage();
                    if (message != null) {
                        System.out.println("writing...");
                        pw.write(message + System.lineSeparator());
                        pw.flush();
                    }
                }
            }
        });
    }

    private void closeAllEntities() {
        try {
            br.close();
            pw.close();
            if (reading != null) {
                reading.interrupt();
            }
            if (writing != null) {
                writing.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
