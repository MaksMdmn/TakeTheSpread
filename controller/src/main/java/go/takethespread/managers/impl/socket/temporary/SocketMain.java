package go.takethespread.managers.impl.socket.temporary;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMain {

    private void startUp() {
        // Open server socket for listening
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2002);
            //System.out.println("Server started on port " + LISTENING_PORT);
        } catch (IOException e) {
        }

        if (serverSocket == null) {
            return;
        }

        // Start ServerDispatcher thread
        ServerDispatcher serverDispatcher = new ServerDispatcher();

        // Accept and handle client connections
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                ClientInfo clientInfo = new ClientInfo();
                clientInfo.socket = socket;

                clientInfo.clientListener = new ClientListener(clientInfo, serverDispatcher);
                clientInfo.clientListener.start();

                clientInfo.clientSender = new ClientSender(clientInfo, serverDispatcher);
                clientInfo.clientSender.start();

                serverDispatcher.addClient(clientInfo);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
