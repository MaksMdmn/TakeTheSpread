package go.takethespread.managers.impl.temporary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

public class ClientListener extends Thread {

    // ================
    // private fields
    // ================

    // ================
    // public methods
    // ================

    private ServerDispatcher serverDispatcher;
    private ClientInfo clientInfo;
    private BufferedReader bufferReader;
    private String decoded = null;

    private int messagesCount = 0;

    public ClientListener(ClientInfo clientInfo,
                          ServerDispatcher serverDispatcher) throws IOException {
        this.clientInfo = clientInfo;
        this.serverDispatcher = serverDispatcher;
        Socket socket = clientInfo.socket;

        bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher and notifies the server dispatcher.
     */
    public void run() {
        String message;

        while (!isInterrupted()) {
            try {
                message = bufferReader.readLine();

                if (message == null) {
                    break;
                }

                messagesCount++;

                try {
                    decoded = URLDecoder.decode(message, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                System.out.println(messagesCount + ", " + decoded);

                if (messagesCount % 2 == 0) {
                    serverDispatcher.sendMessage(clientInfo, decoded);
                }

            } catch (IOException e) {
                break;
            }
        }

        // Communication is broken. Interrupt both listener and sender threads
        clientInfo.clientSender.interrupt();
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
