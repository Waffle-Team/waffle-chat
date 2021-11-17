package actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class EnviarMsg implements ActionListener {
    private Socket skt;
    private String clientName;
    private JTextField input;

    public void setClient(Socket s, String nome){
        this.skt = s;
        this.clientName = nome;
    }

    public void setTextInput(JTextField input){
        this.input = input;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Cria objeto de respostas do servidor
        try{
            PrintStream resposta;
            
            resposta = new PrintStream(this.skt.getOutputStream());
            String texto = input.getText();
            if (texto.length() < 1337){
                resposta.println(this.clientName + texto);
                input.setText("");
            }
            else{
                input.setText("Texto excedeu o tamanho limite!");
            }

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
