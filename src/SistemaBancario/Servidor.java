package SistemaBancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class Servidor extends Thread {
    
    private final Usuario usuario;
    private static Socket socket;
    
    private static List<Usuario> usuarios;
    private static List<Agencia> agencias;
    
    private static TipoUsuario tipoUsuario;

    public Servidor(Usuario usuario) {
        this.usuario = usuario;
        agencias = new ArrayList<>();
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
            
            // ********* TESTE *********
            Cliente cliente = new Cliente("Gabrielle", "12345678910");
            Agencia agencia = new Agencia("0226", "Teste");
            Conta conta = new Conta(agencia, "1234", cliente);
            agencia.adicionaConta(conta);
            agencias.add(agencia);
            
            String tipo = entrada.readLine();
            tipoUsuario = TipoUsuario.valueOf(tipo);
            
            System.out.println();
            System.out.println("Um " + tipoUsuario + " se conectou!");
            System.out.println("Total de sockets: " + usuarios.size());
            
            String cpf = entrada.readLine();
            conta = retornaContaCpf(cpf);
            
            if(tipoUsuario.equals(TipoUsuario.ADMINISTRADOR) || conta != null) {
                
                saida.println("CONECTADO");
                
                String requisicao = entrada.readLine();
                while(requisicao != null && !(requisicao.trim().equals(""))) {

                    if(requisicao.equals("SAIR")) {

                        saida.println("DESCONECTADO");
                        break;

                    }
                    
                    switch(tipoUsuario) {
                        
                        case CLIENTE:
                            
                            String[] mensagem = requisicao.split("\\|");
                            OperacaoCliente operacao = OperacaoCliente.valueOf(mensagem[0]);
                            Double valor = Double.valueOf(mensagem[1]);
                            
                            switch(operacao) {
                                
                                case VERIFICAR_SALDO:
                                    
                                    saida.println("MENSAGEM|Saldo atual: " 
                                            + NumberFormat.getCurrencyInstance().format(conta.verificarSaldo()));
                                    break;
                                    
                                case DEPOSITAR:
                                    
                                    conta.depositar(valor);
                                    saida.println("MENSAGEM|Valor depositado!");
                                    break;
                                    
                                case SACAR:
                                    
                                    if (conta.sacar(valor))
                                        saida.println("MENSAGEM|Valor sacado!");
                                    else
                                        saida.println("MENSAGEM_ERRO|Saldo insuficiente!");
                                
                            }
                            
                            break;
                            
                        case ADMINISTRADOR:
                            // para implementar
                        
                    }
                    
                    requisicao = entrada.readLine();

                }
                
            } else {
                
                saida.println("MENSAGEM_ERRO|Cliente não existe!");
                saida.println("DESCONECTADO");
                
            }
            
        } catch(IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

        }

        usuarios.remove(usuario);
            
        System.out.println("\nUsuário " + usuario.getIp() + " se desconectou!");
        System.out.println("Total de sockets: " + usuarios.size() + "\n");
        
    }
    
    private Agencia retornaAgencia(String numeroAgencia) {
        
        for(Agencia a : agencias) {
            
            if (a.getNumero().equals(numeroAgencia))
                return a;
            
        }
        
        return null;
                
    }
    
    private Conta retornaContaCpf(String cpf) {
        
        Cliente cliente = null;
        
        for(Agencia a : agencias) {
            
            for(Conta c : a.getContas()) {
                
                if (c.retornaCorrentista(cpf) != null)
                    return c;
                
            }
            
        }
        
        return null;
        
    }
    
}
