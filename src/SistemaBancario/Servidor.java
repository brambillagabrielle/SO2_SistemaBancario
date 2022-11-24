package SistemaBancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Servidor extends Thread {
    
    private final Usuario usuario;
    private Socket socket;
    private static List<Usuario> usuarios;
    private static TipoUsuario tipoUsuario;

    public Servidor(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public static void main(String args[]) {
        
        System.out.println("* Servidor Sistema Bancário *");
        
        usuarios = new ArrayList<>();
        
        try {
            
            ServerSocket serverSocket = new ServerSocket(6666);
            
            while(true) {
                
                Socket socket = serverSocket.accept();
                
                Usuario usuario = new Usuario();
                usuario.setIp(
                        socket.getRemoteSocketAddress().toString()
                );
                usuario.setSocket(socket);
                usuarios.add(usuario);
                                                
                Thread thread = new Servidor(usuario);
                thread.start();
                
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
                            usuario.getSocket().getInputStream())
            );
            PrintStream saida = new PrintStream(
                    usuario.getSocket().getOutputStream()
            );
            usuario.setSaida(saida);
            
            String tipo = entrada.readLine();
            tipoUsuario = TipoUsuario.valueOf(tipo);
            usuario.setTipoUsuario(tipoUsuario);
            
            System.out.println("Um " + usuario.getTipoUsuario() + " se conectou!");
            
            String requisicao = entrada.readLine();
            while(requisicao != null && !(requisicao.trim().equals(""))) {
                
                saida.println(requisicao);
                saida.println("> ");
                requisicao = entrada.readLine();
                
            }
            
            usuarios.remove(saida);
            socket.close();
            
        } catch(IOException e) {
            
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            
        }
        
    }
    
}
