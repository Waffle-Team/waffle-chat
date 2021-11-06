import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Server implements Runnable{
    public Socket skt;
    public static ArrayList<Socket> clients = new ArrayList<Socket>();

    public Server(Socket cliente){
        this.skt = cliente;
    }
    public static void respondeClients(String msg) throws IOException {
        PrintStream resposta;

        for (int i = 0; i < Server.clients.size(); i++) {
            Socket client = Server.clients.get(i);
            System.out.println("Broadcast to client:" + client);
            resposta = new PrintStream(client.getOutputStream());
            resposta.println(msg);
        }
    }


    public static void main(String[] args)  throws IOException {


        //Cria um socket na porta 12345
        ServerSocket servidor = new ServerSocket (12345);

        /*
        * Aguarda alguém se conectar. A execução do servidor
        * fica bloqueada na chamada do método accept da classe
        * ServerSocket. Quando alguém se conectar ao servidor, o
        * método desbloqueia e retorna com um objeto da classe
        * Socket, que é uma porta da comunicação.
        * */
        System.out.println("Aguardando conexão do cliente...");

        Socket cliente;
        do{
            cliente = servidor.accept();
            // Cria uma thread do servidor para tratar a conexão
            Server tratamento = new Server(cliente);
            Thread t = new Thread(tratamento);

            //adiciona client a lista de clients do servidor
            clients.add(cliente);

            // Inicia a thread para o cliente conectado
            t.start();
        }while (cliente != null);
    }

    /* A classe Thread, que foi instancia no servidor, implementa Runnable.
       Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'.
    */
    public void run(){
        System.out.println("Nova conexao com o cliente " + this.skt.getInetAddress().getHostAddress());
        System.out.println("Total de clients conectados: " + Server.clients.size());

        try {
            Scanner request = null;
            String ultima_msg = null;

            //Cria objeto de resquests do cliente
            request = new Scanner(this.skt.getInputStream());




            //inicia conversa com o cliente
            while (this.skt.isConnected()){
                //recebe mensagem de cliente
                if(request.hasNextLine()){
                    ultima_msg = request.nextLine();
                    Server.respondeClients(ultima_msg);
                    System.out.println(ultima_msg);
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