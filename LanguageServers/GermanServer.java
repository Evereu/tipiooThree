package LanguageServers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class GermanServer {

    public static final int GermanServerPORT = 1002;

    static HashMap<String, String> DEdictionary = new HashMap<String, String>() {{
        put("pies", "Hund");
        put("kot", "Katze");
        put("Samolot", "Flugzeug");
        put("Książka", "Buch");
        put("Szachy", "Schach");
        put("Drzewo", "Baum");
        put("Motyl", "Schmetterling");
        put("Komputer", "Computer");
        put("Plaża", "Strand");
        put("Serce", "Herz");
        put("Miasto", "Stadt");
        put("Deszcz", "Regen");
    }};

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(GermanServerPORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                languageServerRequestHandler(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void languageServerRequestHandler(Socket socket) {
        Thread clientThread = new Thread(() -> {
            String toSend = null;
            String wordToTranslate;
            String clientAdress;
            int clientPort;

            try {

                InputStream is = socket.getInputStream();//czytanie danych z socketa -od clienta
                OutputStream os = socket.getOutputStream();
                InputStreamReader isreader = new InputStreamReader(is);//ułatwienie aby w byteach nie pracować
                OutputStreamWriter oswriter = new OutputStreamWriter(os);
                BufferedReader br = new BufferedReader(isreader);//za pomoc tych dwóch łaczymy się ze strumieniami

                System.out.println("GermanServer tutututu");

                String[] msgFromMainServer = br.readLine().split(":");

                wordToTranslate = msgFromMainServer[0].toLowerCase();
                clientAdress = msgFromMainServer[1];
                clientPort = Integer.parseInt(msgFromMainServer[2]);

                for (String part : DEdictionary.keySet()) {
                    if (part.toLowerCase().equals(wordToTranslate)) {
                        toSend = DEdictionary.get(part);
                    }
                }

                if (toSend == null) {
                    toSend = "Brak słowa w słowniku";
                }

                // Zamknięcie strumieni i gniazda
                br.close();
                socket.close();
                socket.close();

                Socket clientResponseSocket = new Socket(clientAdress, clientPort);
                PrintWriter clientResponseOut = new PrintWriter(clientResponseSocket.getOutputStream(), true);
                clientResponseOut.println(toSend);
                clientResponseSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

}
