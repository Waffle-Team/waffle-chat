import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Array;
import java.util.Random;
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
                    System.out.println("""
                            Selecione a cor que deseja usar:
                            
                            1. Aleatoria
                            2. Vermelho
                            3. Verde
                            4. Amarelo
                            5. Azul
                            """);
                    int colorSelector = leitor_teclado.nextInt();

                    int r = 0, g = 0, b = 0;

                    boolean isValidColor = false;
                    do{
                        switch (colorSelector){
                            case 1:
                                Random random = new Random();
                                r = random.nextInt(255);
                                g = random.nextInt(255);
                                b = random.nextInt(255);
                                isValidColor = true;
                                break;
                            case 2:
                                //vermelo
                                r = 168;
                                g = 50;
                                b = 52;
                                isValidColor = true;
                                break;
                            case 3:
                                //verde
                                r = 40;
                                g = 122;
                                b = 42;
                                isValidColor = true;
                                break;
                            case 4:
                                //amarelo
                                r = 200;
                                g = 195;
                                b = 0;
                                isValidColor = true;
                                break;
                            case 5:
                                //azul
                                r = 28;
                                g = 0;
                                b = 105;
                                isValidColor = true;
                                break;
                            default:
                                System.out.println("cor invalida");
                        }
                    }while(!isValidColor);



                    Thread clientThead = new Thread( new Client(socket, apelido, new Color(r,g,b)));
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
