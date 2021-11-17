import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{
    Socket skt;
    public static ArrayList<Socket> clients = new ArrayList<>();

    public Server(Socket cliente){
        this.skt = cliente;
    }
    public static void respondeClients(String msg) throws IOException {
        PrintStream resposta;

        for (int i = 0; i < Server.clients.size(); i++) {
            Socket client = Server.clients.get(i);
            if(client.isConnected()){
                System.out.println("Broadcast to client:" + client);
                resposta = new PrintStream(client.getOutputStream());
                resposta.println(msg);
            }else{
                //DEBUG: clients não desconectam do servidor
                System.out.println("cliente desconectado do servidor:");
                Server.clients.remove(Server.clients.get(i));
            }

        }
    }

    /* A classe Thread, que foi instancia no servidor, implementa Runnable.
       Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'.
    */
    public void run(){
        System.out.println("Nova conexao com o cliente " + this.skt.getInetAddress().getHostAddress());
        System.out.println("Total de clients conectados: " + Server.clients.size());

        try {
            Scanner request = null;
            String ultimamsg = null;
            //Cria objeto de resquests do cliente
            request = new Scanner(this.skt.getInputStream());
            //inicia conversa com o cliente
            while (this.skt.isConnected()){
                //recebe mensagem de cliente
                if(request.hasNextLine()){
                    ultimamsg = request.nextLine();
                    Server.respondeClients(ultimamsg);
                    System.out.println(ultimamsg);
                }
            }
            //Finaliza objetos
            request.close();
            this.skt.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}