package code.kevinAuberson_AdrianRogner.Server.src.main.java.ch.heig.dai.lab.protocoldesign;

import java.net.*;
import java.io.*;

import static java.lang.Character.isDigit;
import static java.nio.charset.StandardCharsets.*;

public class TextualTCPServer {
    private BufferedReader in;
    private BufferedWriter out;
    private int port;

    TextualTCPServer(int port){
        this.port = port;
    }

    /**
     * Connection entre le serveur et client et lancement du calcul
     */
    public void waitConnection() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                try (Socket clientSocket = serverSocket.accept()){
                    communicateClient(clientSocket);
                } catch (IOException e) {
                    System.out.println("Server: client socket ex.: " + e);
                    serverSocket.close();
                }
            }
        } catch(IOException e){
            System.out.println("Server: server socket ex.: " + e);
        }

    }

    /**
     * Fonction qui permet de savoir si l'opération entrée par l'utilisateur est valable
     * @param operation
     * @return
     */
    boolean isOperation(String operation){
        switch (operation.toUpperCase()){
            case "ADD":
                return true;
            case "SUB":
                return true;
            case "MULT":
                return true;
            case "DIV":
                return true;
            default:
                return false;
        }
    }

    /**
     * Communication entre le serveur et le client
     * @param clientSocket
     */
    public void communicateClient(Socket clientSocket) {
        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), UTF_8));
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), UTF_8));
            String line;
            this.out.write("Bienvenue sur notre calculatrice, vous pouvez effectuer les opérations suivantes : ADD, SUB, MULT et DIV\nEn utilisant ce format : OPERATION OPERAND1 OPERAND2\nPour quitter entrez QUIT\nEND_WELCOME\n");
            this.out.flush();

            while ((line = in.readLine()) != null) {
                if(line.equalsIgnoreCase("QUIT")){
                    this.out.write("Merci et au revoir" + "\n");
                    this.out.flush();
                    clientSocket.close();

                    in.close();
                    out.close();
                    break;
                }

                String[] command = line.split(" ");

                if(command.length != 3){
                    this.out.write("ERROR : BAD FORMAT \n");
                    this.out.flush();
                    continue;
                }

                int operand1;
                int operand2;
                String operation;

                try {
                    operation = command[0].toUpperCase();
                } catch (Exception e){
                    this.out.write("ERROR : BAD NOT SPECIFIED" + "\n");
                    this.out.flush();
                    continue;
                }

                if(!isOperation(operation)){
                    this.out.write("ERROR : BAD OPERATION " + "\n");
                    this.out.flush();
                    continue;
                }

                if(!command[1].matches("\\d*") || !command[2].matches("\\d*")){
                    out.write("ERROR : OPERAND 1 OR 2 NOT A INT" + "\n");
                    out.flush();
                    continue;
                }

                try{
                    operand1 = Integer.parseInt((command[1]));
                } catch (Exception e) {
                    this.out.write("ERROR : BAD OPERAND 1" + "\n");
                    this.out.flush();
                    continue;
                }

                try {
                    operand2 = Integer.parseInt(command[2]);
                } catch (Exception e){
                    this.out.write("ERROR : BAD OPERAND 2" + "\n");
                    out.flush();
                    continue;
                }

                out.write("Résultat de votre calcul : "+ calculate(operand1, operand2, operation) + "\n");
                out.flush();
            }
        } catch (IOException e) {
            System.out.println("Communication ex.: " + e);
        }
    }

    /**
     * Fonction pour effectuer le calcul
     * @param op1 operand 1
     * @param op2 operand 2
     * @param op operation à effectuer
     * @return un int
     */
    public int calculate(int op1, int op2, String op){

        switch (op.toUpperCase()){
            case "ADD":
                return op1 + op2;
            case "SUB":
                return op1 - op2;
            case "MULT":
                return op1 * op2;
            case "DIV":
                return op1 / op2;
            default:
                return 0;
        }
    }
}
