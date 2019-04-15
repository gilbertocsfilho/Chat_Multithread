package chat_multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class ChatServer extends Thread{
    private static Vector CLIENTES;
    
    private Socket conexao; 
    
     private String nomeCliente;
   
    private static List LISTA_DE_NOMES = new ArrayList();
    
    public ChatServer(Socket socket) {
        this.conexao = socket;
    }
    
    public boolean armazena(String newName){
    
       for (int i=0; i < LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(newName))
           return true;
       }
       
       LISTA_DE_NOMES.add(newName);
       return false;
    }
    
    public void remove(String oldName) {
       for (int i=0; i< LISTA_DE_NOMES.size(); i++){
         if(LISTA_DE_NOMES.get(i).equals(oldName))
           LISTA_DE_NOMES.remove(oldName);
       }
    }
    
    public static void main(String[] args) {
        CLIENTES = new Vector();
        try {
            
            ServerSocket server = new ServerSocket(5555);
            System.out.println("Servidor rodando na porta 5555");
            
            while (true) {
                
                Socket conexao = server.accept();
                
                Thread t = new ChatServer(conexao);
                t.start();
                
            }
        } catch (IOException e) {
            
            System.out.println("IOException: " + e);
        }
    }
    
    public void run()
    {
        try {
           
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            
	    PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            
            this.nomeCliente = entrada.readLine();
            
            if (armazena(this.nomeCliente)){
              saida.println("Este nome ja existe! Escolha outro, por gentileza");
              CLIENTES.add(saida);
              
              this.conexao.close();
              return;
            } else {
               
               System.out.println(this.nomeCliente + " : conectado ao servidor!");
            }
            
            if (this.nomeCliente == null) {
                return;
            }
            
            CLIENTES.add(saida);
            
            String msg = entrada.readLine();
           
            while (msg != null && !(msg.trim().equals("")))
            {
                
                sendToAll(saida, " escreveu: ", msg);
                
                msg = entrada.readLine();
            }
           
            System.out.println(this.nomeCliente + " saiu do bate-papo!");
            
            sendToAll(saida, " saiu ", " do chat!");
           
            remove(this.nomeCliente);
            
            CLIENTES.remove(saida);
            
            this.conexao.close();
            
        } catch (IOException e)  {
            
            System.out.println("Falha na Conexao (server)... .. ." + e);
            System.out.println(this.nomeCliente + " fechou o chat");
            
        }
    }
    
    public void sendToAll(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = CLIENTES.elements();
        while (e.hasMoreElements()) {
            
            PrintStream chat = (PrintStream) e.nextElement();
            
                chat.println(this.nomeCliente + acao + msg);
            
        }
    }
    
}
