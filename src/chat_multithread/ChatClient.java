package chat_multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends Thread {

    private Socket conexao;

    public ChatClient(Socket socket) {
        this.conexao = socket;
    }

    public static void main(String args[]) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite o IP do servidor: ");
            String IPServer = sc.nextLine();

            Socket socket = new Socket(IPServer, 2525);

            PrintStream saida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome: ");
            String meuNome = teclado.readLine();

            saida.println(meuNome.toUpperCase());

            Thread thread = new ChatClient(socket);
            thread.start();

            String msg;
            while (true) {

                System.out.print("Mensagem > ");
                msg = teclado.readLine();

                saida.println(msg);
            }
        } catch (IOException e) {
            System.out.println("Falha na Conexao " + e);
        }
    }

    public void run() {
        try {

            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));

            String msg;
            while (true) {

                msg = entrada.readLine();

                if (msg == null) {
                    System.out.println("Conexão encerrada!");
                    System.exit(0);
                }
                System.out.println();

                System.out.println(msg);

                System.out.print("Mensagem > ");
            }
        } catch (IOException e) {

            System.out.println("Ocorreu um erro " + e);
        }

    }
}
