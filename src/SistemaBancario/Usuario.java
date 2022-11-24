package SistemaBancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Usuario extends Thread {
    
    private String ip;
    private Socket socket;
    private PrintStream saida;
    private TipoUsuario tipoUsuario;
    private static boolean sair;
    
    public Usuario() {
        
    }

    public Usuario(Socket socket) {
        this.socket = socket;
    }
    
    public static void main(String args[]) {
        
        try {
        
            Socket socket = new Socket("127.0.0.1", 6666);

            PrintStream saida = new PrintStream(
                    socket.getOutputStream()
            );
            BufferedReader teclado = new BufferedReader(
                    new InputStreamReader(System.in)
            );

            saida.println(TipoUsuario.CLIENTE.toString());
            
            System.out.println("Entre com as requisições: ");
            System.out.println("> ");

            Thread thread = new Usuario(socket);
            thread.start();

            String requisicao;
            while(!sair) {

                requisicao = teclado.readLine();
                saida.println(requisicao);

            }
            
        } catch(IOException e) {
            
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            
        }
        
    }
    
    @Override
    public void run() {
        
        try {
        
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream())
            );
            
            String resposta;
            while(true) {
                
                resposta = entrada.readLine();
                
                if (resposta.equals("desconectado")) {
                    sair = true;
                    System.out.println("\nConexão encerrada");
                    System.out.println("\nAperte ENTER para SAIR");
                    break;
                }
                
            }
            
        } catch(IOException e) {
            
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            
        }
        
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintStream getSaida() {
        return saida;
    }

    public void setSaida(PrintStream saida) {
        this.saida = saida;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
}
