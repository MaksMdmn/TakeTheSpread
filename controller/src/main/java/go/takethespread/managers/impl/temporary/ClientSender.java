package go.takethespread.managers.impl.temporary;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Vector;

public class ClientSender extends Thread {

    // ================
    // private fields
    // ================

    // ================
    // public methods
    // ================

    private Vector messageQueue = new Vector();

    private ServerDispatcher serverDispatcher;
    private ClientInfo clientInfo;
    private PrintWriter out;

    public ClientSender(ClientInfo clientInfo, ServerDispatcher serverDispatcher)
            throws IOException {
        this.clientInfo = clientInfo;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientInfo.socket;
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServeDispatcher).
     */
    public synchronized void sendMessage(String message) {
        messageQueue.add(message);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized String getNextMessageFromQueue() throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = (String) messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */
    private void sendMessageToClient(String message) {
        String encoded;
        try {
            encoded = URLEncoder.encode(message, "UTF-8");
            out.println(encoded);
            out.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    public void run() {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();


                sendMessageToClient(message);
            }
        } catch (Exception e) {
            // Commuication problem
//            break;
        }

        // Communication is broken. Interrupt both listener and sender threads
        clientInfo.clientListener.interrupt();
        serverDispatcher.deleteClient(clientInfo);
    }

    // ================
    // private methods
    // ================

    // ================
    // util methods
    // ================

    // ================
    // getter/setter
    // ================

}
