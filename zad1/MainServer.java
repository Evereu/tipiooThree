package zad1;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


//Musi być klientem i serwerem bo ma odebrać żądanie i przekazać do serwera języków


public class MainServer {

    public static final int PORT = 5000;
    private static final String SERVER_NAME="localhost";

    static ServerSocket serverSocket;
    static Socket clientSocket;

    public static void main(String[] args) {

        HashMap<String, Integer> languageServersDic = new HashMap<String, Integer>();
        languageServersDic.put("EN", 1001);
        languageServersDic.put("DE", 1002);
        languageServersDic.put("ES", 1003);
        languageServersDic.put("FR", 1004);

        Thread serverThread = new Thread(() -> {
            String clientServerPort;
            String word;
            String lang;
            while (true) {
                try {
                    serverSocket = new ServerSocket(PORT);// Tworzenie serwera na porcie 5000
                    clientSocket = serverSocket.accept();  // Akceptowanie połączeń klientów

                    InputStream is = clientSocket.getInputStream(); // Czytanie danych ze socketa - od klienta
                    OutputStream os = clientSocket.getOutputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Za pomocą tych dwóch łączymy się ze strumieniami
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                    String[] msgFromClient = br.readLine().split(":");
                    word = msgFromClient[0];
                    lang = msgFromClient[1];
                    clientServerPort = msgFromClient[2];

                    for (String part : msgFromClient) {
                        log("kabanos: "+part);
                    }

                    log("Odczytany kod języka przez MainServer: "+lang);

                    String langserv = languageServersDic.get(lang).toString();

                    String clientAdress = clientSocket.getInetAddress().getHostAddress().replace("/", "-");

                        if(languageServersDic.containsKey(lang)){
                            bw.write(languageServersDic.get(lang).toString());
                            bw.newLine();
                            bw.flush();

                            // TODO {"polskie słowo do przetłumaczenia", adres klienta, port na którym klient czeka na wynik}
                            clientThread(word,langserv, clientAdress, clientServerPort);

                            br.close();
                            clientSocket.close();
                            serverSocket.close();
                        }

                    br.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    //{"polskie słowo do przetłumaczenia", adres klienta, port na którym klient czeka na wynik}

    public static void clientThread(String word, String langserv, String clientAdress, String clientPort){
        Thread clientThread = new Thread(() -> {
            try {
                InetAddress serverIp = InetAddress.getByName(SERVER_NAME); // Tłumaczenie adresu serwera na adres IP
                Socket clientSocket = new Socket(serverIp, Integer.parseInt(langserv));
                InputStream is = clientSocket.getInputStream(); // Czytanie danych ze socketa - od klienta
                OutputStream os = clientSocket.getOutputStream();
                InputStreamReader isreader = new InputStreamReader(is); // Ułatwienie aby w byteach nie pracować
                OutputStreamWriter oswriter = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(oswriter); // Wysyłka do serwera
                BufferedReader br = new BufferedReader(isreader); // Odczyt z serwera

                // Wysłanie "word" do serwera
                bw.write(word+":"+clientAdress+":"+clientPort);
                bw.newLine();
                bw.flush();

                clientSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    public static void log(String message){
        System.out.println("[MainServer]:  "+ message);
        System.out.flush();
    }
}

















