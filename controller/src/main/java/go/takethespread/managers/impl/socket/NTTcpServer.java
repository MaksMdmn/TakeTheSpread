package go.takethespread.managers.impl.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NTTcpServer extends Thread {

    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader br;
    private volatile boolean isConn;
    private NTTcpDataBridge dataBridge;
    private PrintWriter pw;

    public NTTcpServer() {
        dataBridge = NTTcpDataBridge.getInstance();
        isConn = true;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8085, 0, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isConn) {
            try {
                if (socket == null) {
                    System.out.println("im here");
                    socket = serverSocket.accept();
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pw = new PrintWriter(socket.getOutputStream());
                }

                if (!socket.isConnected()) {
                    System.out.println("socket is closed");
                    closeAllEntities();
                    break;
                }

                while (br.ready()) {
                    String data = br.readLine();
                    dataBridge.addData(data);
                }

                while (dataBridge.haveMessage()) {
                    String message = dataBridge.acceptMessage();
                    if (message != null) {
                        System.out.println("writing...");
                        pw.write(message + System.lineSeparator());
                        pw.flush();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public NTTcpDataBridge getDataBridge() {
        return dataBridge;
    }

    public void initServ() {
        start();
    }

    public void shutDown() {
        isConn = false;
        closeAllEntities();
    }

    private void closeAllEntities() {
        try {
            br.close();
            pw.close();
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
