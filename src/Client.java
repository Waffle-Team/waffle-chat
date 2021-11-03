import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Runnable{
    private String nome;
    private Socket skt;

    public Client(Socket cliente, String nome){
        this.skt = cliente;
        this.nome = nome + ": ";
    }

    public static void main(String args[]) throws IOException {

        // para se conectar ao servidor, cria-se objeto Socket.
        // O primeiro parâmetro é o IP ou endereço da máquina que
        // se quer conectar e o segundo é a porta da aplicação.
        // Neste caso, usa-se o IP da máquina local (127.0.0.1)
        // e a porta da aplicação ServidorDeEco (12345).
        Socket socket = new Socket("127.0.0.1", 12345);

        /*
        * Cria um novo objeto Cliente com a conexão
        * socket para que seja executado em um novo processo.
        * Permitindo assim a conexão de vário clientes com o servidor.
        * */
        Client c = new Client(socket, "marcel");
        Thread t = new Thread(c);
        t.start();
    }

    public void run() {
        System.out.println("O cliente conectou ao servidor");
        try{
            PrintStream request_msg;
            Scanner resposta = null;


            //Prepara para leitura do teclado
            Scanner teclado = new Scanner(System.in);

            //Cria  objeto para enviar a mensagem ao servidor
            request_msg = new PrintStream(this.skt.getOutputStream());

            //Cria objeto de respostas do servidor
            resposta = new Scanner(this.skt.getInputStream());

            //inicia conversa com o servidor
            while(this.skt.isConnected()){
                //Envia mensagem ao servidor
                if(teclado.hasNextLine()){
                    request_msg.println( this.nome + teclado.nextLine());
                }
                //verifica resposta do servidor
                if(resposta.hasNextLine()){
                    System.out.println(resposta.nextLine());
                }
            }


            //Finaliza objetos
            request_msg.close();
            teclado.close();
            this.skt.close();
            System.out.println("Fim do cliente!");

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}