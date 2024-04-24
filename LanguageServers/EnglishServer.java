package LanguageServers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnglishServer {

    EnglishServer(){

    }


    public static final int EnglishServerPORT = 1001;

    static HashMap<String, String> EN = new HashMap<String, String>() {{
        put("pies", "dog");
        put("kot", "cat");
        put("woda", "water");
    }};

    public static void main(String[] args) {
        String toSend = null;
        String wordToTranslate;
        String clientAdress;
        int clientPort;


        while (true){

            try {
                ServerSocket serverSocket = new ServerSocket(EnglishServerPORT);
                Socket clientSocket = serverSocket.accept();
                InputStream is = clientSocket.getInputStream();//czytanie danych z socketa -od clienta
                OutputStream os = clientSocket.getOutputStream();
                InputStreamReader isreader = new InputStreamReader(is);//ułatwienie aby w byteach nie pracować
                OutputStreamWriter oswriter = new OutputStreamWriter(os);
                BufferedReader br = new BufferedReader(isreader);//za pomoc tych dwóch łaczymy się ze strumieniami
                BufferedWriter bw = new BufferedWriter(oswriter);

                System.out.println("EnglishServer tutututu");


                String[] msgFromMainServer = br.readLine().split(":");

                wordToTranslate = msgFromMainServer[0].toLowerCase();
                clientAdress = msgFromMainServer[1];
                clientPort = Integer.parseInt(msgFromMainServer[2]);


                for (String part : EN.keySet()) {
                    if(part.toLowerCase().equals(wordToTranslate)){
                        toSend = EN.get(part);
                    }
                }

                if(toSend == null){
                    toSend = "Takie słowo nie istnieje";
                }

                // Zamknięcie strumieni i gniazda
                br.close();
                clientSocket.close();
                serverSocket.close();

                Socket clientResponseSocket = new Socket(clientAdress, clientPort);
                PrintWriter clientResponseOut = new PrintWriter(clientResponseSocket.getOutputStream(), true);
                clientResponseOut.println(toSend);
                clientResponseSocket.close();


            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
