package code.kevinAuberson_AdrianRogner.Server.src.main.java.ch.heig.dai.lab.protocoldesign;

public class Server {
    final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        // Create a new server and run it
        Server server = new Server();
        server.run();
    }

    private void run() {
        TextualTCPServer tcp = new TextualTCPServer(SERVER_PORT);
        tcp.waitConnection();
    } 
}