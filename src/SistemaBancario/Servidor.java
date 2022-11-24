package SistemaBancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class Servidor extends Thread {
    
    private final Usuario usuario;
    private static Socket socket;
    private static List<Usuario> usuarios;
    private static TipoUsuario tipoUsuario;

    public Servidor(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public static void main(String args[]) {
        
        System.out.println("SERVIDOR SISTEMA BANCÁRIO");
        System.out.println("Uptime: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        
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
            
            System.out.println("\nUm " + usuario.getTipoUsuario() + " se conectou!");
            System.out.println("Total de sockets: " + usuarios.size() + "\n");
            
            String requisicao = entrada.readLine();
            String resposta;
            while(requisicao != null && !(requisicao.trim().equals(""))) {
                
                if(requisicao.equals("q")) {
                    
                    saida.println("desconectado");
                    usuarios.remove(usuario);
                    break;
                    
                }
                    
                resposta = requisicao;
                saida.println(resposta);
                saida.println("> ");
                
            }
            
        } catch(IOException e) {
            
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            
        }
            
        System.out.println("\nUsuário " + usuario.getIp() + " se desconectou!");
        System.out.println("Total de sockets: " + usuarios.size() + "\n");
        
    }
    
}
