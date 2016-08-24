package go.takethespread.managers.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class NTTcpServer {

    private NTTcpDataBridge dataBridge;
    private Socket socket;
    private ServerSocket serverSocket;
    private volatile boolean isConn = false;
    private BufferedReader br;
    private PrintWriter pw;
    private Thread reading;
    private Thread writing;
    private String host;
    private int port;

    public NTTcpServer() {
        dataBridge = new NTTcpDataBridge();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public NTTcpDataBridge getDataBridge() {
        return dataBridge;
    }

    public void initServerWork() {
        isConn = true;
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
            serverSocket.setSoTimeout(10 * 1000);
            if (socket == null) {
                socket = serverSocket.accept();
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
            }
        } catch (SocketTimeoutException e) {
            isConn = false;
            System.out.println("Socket time out");
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

    public boolean isServerWorking() {
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
            if (br != null) {
                br.close();
            }
            if (pw != null) {
                pw.close();
            }
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

    protected class NTTcpDataBridge {

        private BlockingDeque<String> messages;
        private BlockingDeque<String> answers;
        private BlockingDeque<String> nearMarketData;
        private BlockingDeque<String> farMarketData;

        private NTTcpDataBridge() {
            messages = new LinkedBlockingDeque<>();
            answers = new LinkedBlockingDeque<>();
            nearMarketData = new LinkedBlockingDeque<>();
            farMarketData = new LinkedBlockingDeque<>();
        }

        protected void addAnswer(String data) {
            if (data.isEmpty()) {
                return;
            }

            if (data.contains("TEST")) {
                System.out.println(data);
                return;
            }

            if (data.startsWith("n")) {
                nearMarketData.add(data.split(NTTcpMessage.ntToken)[1]);
                return;
            }

            if (data.startsWith("f")) {
                farMarketData.add(data.split(NTTcpMessage.ntToken)[1]);
                return;
            }

            answers.push(data);

        }

        protected void addMessage(String message) {
            messages.push(message);
        }

        protected String acceptMessage() {
            return messages.pollFirst();
        }

        protected String acceptAnswer() {
            return answers.pollFirst();
        }

        protected String acceptNearMarketData() {
            return nearMarketData.peekLast();
        }

        protected String acceptFarMarketData() {
            return farMarketData.peekLast();
        }

        protected boolean haveMessage() {
            return !messages.isEmpty();
        }

        protected boolean haveAnswers() {
            return !answers.isEmpty();
        }

        protected boolean haveMarketData() {
            return !nearMarketData.isEmpty() && !farMarketData.isEmpty();
        }

    }

}
