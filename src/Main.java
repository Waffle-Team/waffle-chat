import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        //variaveis de controle do app
        boolean running;
        String apelido;
        int op_app;

        //inicialização
        Scanner leitor_teclado = new Scanner(System.in);
        System.out.print("Digite seu apelido: ");
        apelido = leitor_teclado.next();
        System.out.flush();
        System.out.println("O apelido escolhido foi -> " + apelido);

        //main da main -> aqui é o app rodando
        do{

            running = true;
            System.out.println("""
                    1. Logar no chat
                    2. Abrir servidor
                    3. Sair
                    """);
            System.out.print("Escolha uma das opções acima: ");
            op_app = leitor_teclado.nextInt();

            switch (op_app){
                case 1:
                    /* * * * * * * * * *
                     *  Logar no chat  *
                     * * * * * * * * * */
                    Socket socket = new Socket("127.0.0.1", 12345);

                    Thread clientThead = new Thread( new Client(socket, apelido));
                    clientThead.start();
                    clientThead.join();
                    break;
                case 2:

                    /* * * * * * * * * *
                    *  abrir servidor  *
                    * * * * * * * * * */

                    //Cria um socket na porta 12345
                    ServerSocket servidor = new ServerSocket (12345);
                    System.out.println("Aguardando conexão do cliente...");
                    Socket cliente;
                    do{
                        cliente = servidor.accept();
                        // Cria uma thread do servidor para tratar a conexão

                        Thread threadServer = new Thread(new Server(cliente));
                        // Inicia a thread para o cliente conectado
                        threadServer.start();
                    }while (cliente != null);
                    break;
                case 3:
                    /* * * * * *
                     *  Sair   *
                     * * * * * */

                    running = false;
                    break;
                default:
                    System.out.println("Opção invalida");
                    break;
            }

        }while(running);
    }
}
