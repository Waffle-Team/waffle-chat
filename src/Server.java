import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable{
    public Socket skt;

    public Server(Socket cliente){
        this.skt = cliente;
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
            // Inicia a thread para o cliente conectado
            t.start();
        }while (cliente != null);
    }

    /* A classe Thread, que foi instancia no servidor, implementa Runnable.
       Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'.
    */
    public void run(){
        System.out.println("Nova conexao com o cliente " + this.skt.getInetAddress().getHostAddress());


        try {
            Scanner request = null;
            PrintStream resposta;
            String ultima_msg = null;

            //Cria objeto de resquests do cliente
            request = new Scanner(this.skt.getInputStream());

            //Cria objeto de respostas ao cliente
            resposta = new PrintStream(this.skt.getOutputStream());


            //inicia conversa com o cliente
            /*
            *  DEBUG: a resposta ao client ainda é a
            *  propia mensagem pelo escopo da thead,
            *  é necessario implementar um buffer no escopo global
            *  e varrer thead, para enviar as mensagens recebidas
            * */
            while (this.skt.isConnected()){
                //recebe mensagem de cliente
                if(request.hasNextLine()){
                    ultima_msg = request.nextLine();
                    System.out.println(ultima_msg);
                }
                //responde ao cliente
                if(ultima_msg != null){
                    resposta.println(ultima_msg);
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