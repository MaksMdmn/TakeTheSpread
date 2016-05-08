package go.takethespread.managers.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NTTcpServer {

    private NTTcpDataBridge dataBridge;
    private Socket socket;
    private ServerSocket serverSocket;
    private volatile boolean isConn = false;
    private BufferedReader br;
    private PrintWriter pw;
    private Thread reading;
    private Thread writing;

    public NTTcpServer() {
        dataBridge = NTTcpDataBridge.getInstance();
    }

    public NTTcpDataBridge getDataBridge() {
        return dataBridge;
    }

    public void initServerWork() {
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
        reading.start();
        writing.start();
    }

    public void shutDown() {
        isConn = false;
        closeConnEntities();
    }

    public boolean isServerWorking(){
        return isConn;
    }

    private Thread initReading() {
        return new Thread(() -> {
            while (isConn) {
                try {
                    while (br.ready()) {
                        String data = br.readLine();
                        dataBridge.addAnswer(data);
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
                        pw.write(message + System.lineSeparator());
                        pw.flush();
                    }
                }
            }
        });
    }

    private void closeConnEntities() {
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
