import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        //variaveis de controle do app
        boolean running;
        String apelido;
        int opapp;

        //inicialização
        Scanner leitorteclado = new Scanner(System.in);
        System.out.print("Digite seu apelido: ");
        apelido = leitorteclado.next();
        System.out.flush();
        System.out.println("O apelido escolhido foi -> " + apelido);

        //main da main -> aqui é o app rodando
        do{
            running = true;
            System.out.println("1. Logar no chat");
            System.out.println("2. Abrir servidor");
            System.out.println("3. Sair");
            System.out.print("Escolha uma das opcoes acima: ");
            opapp = leitorteclado.nextInt();

            switch (opapp){
                case 1:
                    /* * * * * * * * * *
                     *  Logar no chat  *
                     * * * * * * * * * */
                    Socket socket = new Socket("127.0.0.1", 12345);
                    System.out.println("Selecione a cor que deseja usar:");
                    System.out.println("1. Aleatoria");
                    System.out.println("2. Vermelho");
                    System.out.println("3. Verde");
                    System.out.println("4. Amarelo");
                    System.out.println("5. Azul");
                    int colorSelector = leitorteclado.nextInt();
                    int r = 0;
                    int g = 0;
                    int b = 0;
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
                        Server.clients.add(cliente);

                        // Inicia a thread para o cliente conectado
                        t.start();
                    }while (cliente != null);
                    break;
                case 3:
                    /* * * * * *
                     *  Sair   *
                     * * * * * */

                    running = false;
                    break;
                default:
                    System.out.println("Opcao invalida");
                    break;
            }

        }while(running);
    }
}
