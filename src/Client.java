import Actions.EnviarMsg;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    private String nome;
    private Socket skt;
    private boolean modoTerminalBugado;
    private Color corNome;

    public Client(Socket cliente, String nome, Color c){
        this.skt = cliente;
        this.nome = nome + ": ";
        this.modoTerminalBugado = false;
        this.corNome = c;
    }
    public Client(Socket cliente, String nome, boolean interfaceBugada){
        this.skt = cliente;
        this.nome = nome + ": ";
        this.modoTerminalBugado = interfaceBugada;
    }

    public void telaChat(){
        JFrame frame;

        //Janela
        frame = new JFrame("Waffle Chat v-Alfa 1.0");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        //painel client recive
        JPanel panel = new JPanel();

        //Criarndo a text area
        JTextArea textArea = new JTextArea(30, 25);
        textArea.setEditable(false);
        textArea.setAutoscrolls(true);
        textArea.setSize(600, 300);
        textArea.append("Logado no chat com o socket: " + this.skt.getLocalSocketAddress() + "\n");

        //Layout de quebra
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(textArea);


        //painel client send
        JTextField input = new JTextField(30);
        input.setSize(600,10);
        input.setAlignmentX(0);
        panel.add(input);

        //Botao
        JButton enviar = new JButton("Enviar");
        EnviarMsg e = new EnviarMsg();
        e.setClient(this.skt, this.nome);
        e.setTextInput(input);
        enviar.addActionListener(e);
        panel.add(enviar);


        //frame adds
        frame.add(panel);
        frame.setVisible(true);

        //Receber msg
        try{
            this.atualizaChat(textArea);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public void atualizaChat(JTextArea textArea) throws IOException {
        Scanner resposta = null;

        try(Scanner scanner = resposta = new Scanner(this.skt.getInputStream())){
            while(this.skt.isConnected()){
                String texto;
                String nome = new String();
                char aux;

                if(resposta.hasNextLine()){
                    texto = resposta.nextLine() + "\n";

                    int i = 0;
                    do{
                        aux = texto.charAt(i);
                        nome += aux;
                        i++;
                    }while(aux != ':');

                    textArea.setForeground(this.corNome);
                    textArea.append(nome);
//                    textArea.setForeground(Color.black);
                    textArea.append(texto.substring(nome.length()));
                }
            }
        }

    }



    public void run() {
        System.out.println("O cliente conectou ao servidor");
        this.telaChat();

        if(this.modoTerminalBugado){
            try{
                PrintStream request_msg;
                Scanner resposta = null;

                //Objeto para leitura do teclado
                Scanner teclado = new Scanner(System.in);

                //Cria  objeto para enviar a mensagem ao servidor
                request_msg = new PrintStream(this.skt.getOutputStream());

                //Cria objeto de respostas do servidor
                resposta = new Scanner(this.skt.getInputStream());

                //inicia conversa com o servidor
                String client_msg;
                while(this.skt.isConnected()){
                    //Envia mensagem ao servidor
                    if(teclado.hasNextLine()){
                        client_msg =  teclado.nextLine();
                        // TODO: tratar msg do client para saber se é um comando (sugestão utilizar logica igual a do minecraft)
                        if(client_msg.length() < 1337){
                            request_msg.println( this.nome + client_msg);
                        }else{
                            System.out.println("Mensagem é muito grande para ser enviada limite de leet caracteres\n\n\n\n");
                        }
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
        Client c = new Client(socket, "Anonimo2", Color.GREEN);
        Thread t = new Thread(c);
        t.start();
    }

}