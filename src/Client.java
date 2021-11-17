import actions.EnviarMsg;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    private String nome;
    private Socket skt;
    private Color corNome;

    public Client(Socket cliente, String nome, Color c){
        this.skt = cliente;
        this.nome = nome + ": ";
        this.corNome = c;
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

        try(Scanner resposta = new Scanner(this.skt.getInputStream())){
            while(this.skt.isConnected()){
                String texto;
                String name = new String();
                char aux;

                if(resposta.hasNextLine()){
                    texto = resposta.nextLine() + "\n";

                    int i = 0;
                    do{
                        aux = texto.charAt(i);
                        name += aux;
                        i++;
                    }while(aux != ':');

                    textArea.setForeground(this.corNome);
                    textArea.append(name);
                    textArea.append(texto.substring(name.length()));
                }
            }
        }

    }



    public void run() {

        System.out.println("O cliente conectou ao servidor");
        this.telaChat();
    }
}