package LanguageServers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageServerTmp {

    public static final int PORT = 1000;
    //private static final String SERVER_NAME="localhost";

    public static void main(String[] args) {



        while (true) {
            try {

                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket clientSocket = serverSocket.accept();
                InputStream is = clientSocket.getInputStream();//czytanie danych z socketa -od clienta
                OutputStream os = clientSocket.getOutputStream();
                InputStreamReader isreader = new InputStreamReader(is);//ułatwienie aby w byteach nie pracować
                OutputStreamWriter oswriter = new OutputStreamWriter(os);
                BufferedReader br = new BufferedReader(isreader);//za pomoc tych dwóch łaczymy się ze strumieniami
                BufferedWriter bw = new BufferedWriter(oswriter);

                //Na razie nie sprawdzam skrótu języka

                String toSend = null;



                List<String> words = new ArrayList<String>();
                String word;
                while ((word = br.readLine()) != null){
                    System.out.println(word);
                    words.add(word);
                }


//                for (String part : EN.keySet()) {
//                    if(part.toLowerCase().equals()){
//                        toSend = EN.get(part);
//                    }
//                }


                bw.write("Serwer language przekazuje że odczytał poprawnie");
                bw.newLine();
                bw.flush();


                // Zamknięcie strumieni i gniazda
                br.close();
                clientSocket.close();
                serverSocket.close();

                //Tworzenie nowego Socketa by odesłać informacje do klienta

                Socket clientResponseSocket = new Socket("localhost", 1500);
                PrintWriter clientResponseOut = new PrintWriter(clientResponseSocket.getOutputStream(), true);
                clientResponseOut.println(toSend);
                clientResponseSocket.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
