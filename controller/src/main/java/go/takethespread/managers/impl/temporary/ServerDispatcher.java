package go.takethespread.managers.impl.temporary;

import java.util.Vector;

public class ServerDispatcher {

    private Vector messageQueue = new Vector();
    private Vector<ClientInfo> clients = new Vector<>();

    public synchronized void addClient(ClientInfo clientInfo) {
        clients.add(clientInfo);
    }

    public synchronized void deleteClient(ClientInfo clientInfo) {
        int clientIndex = clients.indexOf(clientInfo);

        if (clientIndex != -1) {
            clients.removeElementAt(clientIndex);
        }
    }

    private synchronized void sendMessageToAllClients(String message) {
        for (int i = 0; i < clients.size(); i++) {
            ClientInfo info = clients.get(i);

            info.clientSender.sendMessage(message);
        }
    }

    public void sendMessage(ClientInfo clientInfo, String message) {
        clientInfo.clientSender.sendMessage(message);
    }
}
