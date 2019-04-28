package chat_multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ChatClientGui extends Thread {

    private Socket conexao;

    public ChatClientGui(Socket socket) {
        this.conexao = socket;
    }

    public static void main(String args[]) {
        try {
            String IPServer = JOptionPane.showInputDialog("Insira IP do servidor:\n");

            Socket socket = new Socket(IPServer, 2525);

            PrintStream saida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            String meuNome = JOptionPane.showInputDialog("Qual seu nome?:\n");

            saida.println(meuNome.toUpperCase());

            Thread thread = new ChatClientGui(socket);
            thread.start();

            String msg;
            while (true) {

                System.out.print("Mensagem > ");
                msg = teclado.readLine();

                saida.println(msg);
            }
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Falha na conexão\n " + e, "Mensagem", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public void run() {
        try {

            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));

            String msg;
            while (true) {

                msg = entrada.readLine();

                if (msg == null) {

                    JOptionPane.showMessageDialog(null, "Conexão encerrada", "Logout", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
                System.out.println();

                System.out.println(msg);

                System.out.print("Mensagem > ");
            }
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Ocorreu um erro\n " + e, "Mensagem", JOptionPane.ERROR_MESSAGE);
        }

    }
}
