package chat_multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class ChatServer extends Thread {

    private static Vector vectorClientes;

    private Socket conexao;

    private String nomeCliente;

    private static List listaNomes = new ArrayList();

    public ChatServer(Socket socket) {
        this.conexao = socket;
    }

    public boolean armazena(String NovoNome) {

        for (int i = 0; i < listaNomes.size(); i++) {
            if (listaNomes.get(i).equals(NovoNome)) {
                return true;
            }
        }

        listaNomes.add(NovoNome);
        return false;
    }

    public void remove(String oldName) {
        for (int i = 0; i < listaNomes.size(); i++) {
            if (listaNomes.get(i).equals(oldName)) {
                listaNomes.remove(oldName);
            }
        }
    }

    public static void main(String[] args) {
        vectorClientes = new Vector();
        try {

            ServerSocket server = new ServerSocket(2525);
            System.out.println("Servidor rodando na porta 2525");

            while (true) {

                Socket conexao = server.accept();

                Thread t = new ChatServer(conexao);
                t.start();

            }
        } catch (IOException e) {

            System.out.println("IOException: " + e);
        }
    }

    public void run() {
        try {

            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));

            PrintStream saida = new PrintStream(this.conexao.getOutputStream());

            this.nomeCliente = entrada.readLine();

            if (armazena(this.nomeCliente)) {
                saida.println("Este nome ja existe! Escolha outro, por gentileza");
                vectorClientes.add(saida);

                this.conexao.close();
                return;
            } else {

                System.out.println(this.nomeCliente + " : conectado ao servidor!");
            }

            if (this.nomeCliente == null) {
                return;
            }

            vectorClientes.add(saida);

            String msg = entrada.readLine();

            while (msg != null && !(msg.trim().equals(""))) {

                EnviarTodos(saida, " escreveu: ", msg);

                msg = entrada.readLine();
            }

            System.out.println(this.nomeCliente + " saiu do bate-papo!");

            EnviarTodos(saida, " saiu ", " do chat!");

            remove(this.nomeCliente);

            vectorClientes.remove(saida);

            this.conexao.close();

        } catch (IOException e) {

            System.out.println("Falha na Conexao (server)... .. ." + e);
            System.out.println(this.nomeCliente + " fechou o chat");
            remove(this.nomeCliente);
        }

    }

    public void EnviarTodos(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = vectorClientes.elements();
        while (e.hasMoreElements()) {

            PrintStream chat = (PrintStream) e.nextElement();
            chat.println(this.nomeCliente + acao + msg);

        }
    }

}
