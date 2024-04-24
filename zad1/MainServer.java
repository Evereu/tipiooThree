package zad1;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


//Musi być klientem i serwerem bo ma odebrać żądanie i przekazać do serwera języków


public class MainServer {

    public static final int PORT = 5000;
    private static final String SERVER_NAME = "localhost";


    public static HashMap<String, Integer> languageServersDic = new HashMap<String, Integer>() {{
        put("EN", 1001);
        put("DE", 1002);
        put("ES", 1003);
        put("FR", 1004);
    }};

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                requestMainServerHandlerThread(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //{"polskie słowo do przetłumaczenia", adres klienta, port na którym klient czeka na wynik}


    public static void requestMainServerHandlerThread(Socket socket) {
        Thread clientThread = new Thread(() -> {
            String clientServerPort;
            String word;
            String lang;

            try {
                // Akceptowanie połączeń klientów

                InputStream is = socket.getInputStream(); // Czytanie danych ze socketa - od klienta
                OutputStream os = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is)); // Za pomocą tych dwóch łączymy się ze strumieniami
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                String[] msgFromClient = br.readLine().split(":");
                word = msgFromClient[0];
                lang = msgFromClient[1];
                clientServerPort = msgFromClient[2];

                for (String part : msgFromClient) {
                    log(part);
                }

                log("Odczytany kod języka przez MainServer: " + lang);

                String langserv = languageServersDic.get(lang).toString();

                String clientAdress = socket.getInetAddress().getHostAddress().replace("/", "-");

                if (languageServersDic.containsKey(lang)) {
                    bw.write(languageServersDic.get(lang).toString());
                    bw.newLine();
                    bw.flush();


                    InetAddress serverIp = InetAddress.getByName(SERVER_NAME); // Tłumaczenie adresu serwera na adres IP
                    Socket clientResponseSocket = new Socket(serverIp, Integer.parseInt(langserv));
                    PrintWriter clientResponseOut = new PrintWriter(clientResponseSocket.getOutputStream(), true);
                    clientResponseOut.println(word + ":" + clientAdress + ":" + clientServerPort);
                    clientResponseSocket.close();

                    br.close();
                    socket.close();
                    socket.close();
                }
                br.close();
                socket.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }


    public static void log(String message) {
        System.out.println("[MainServer]:  " + message);
        System.out.flush();
    }
}




















