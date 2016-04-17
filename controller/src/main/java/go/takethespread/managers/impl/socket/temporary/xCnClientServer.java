package go.takethespread.managers.impl.socket.temporary;

public class xCnClientServer {
    public static void main(String[] args) {
        int port = 8085;
        String host = "localhost";
        String com1 = "GO";
        String com2 = "GJ";

        new xTcpServer();
        xTcpClient client = new xTcpClient(host, port);

        client.write(com1.getBytes());
        System.out.println(new String(client.read()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        xTcpClient client1 = new xTcpClient(host, port);
        client1.write(com2.getBytes());
        System.out.println(new String(client1.read()));
    }
}
