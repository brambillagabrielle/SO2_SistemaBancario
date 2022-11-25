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
    private static List<Agencia> agencias;
    private static TipoUsuario tipoUsuario;
    
    private static String nome;
    private static String cpf;
    private static Agencia agencia;
    private static Conta conta;

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
            agencia = new Agencia("0226", "Teste");
            conta = new Conta(agencia, "1234", "Gabrielle", "12345678910");
            agencia.adicionaConta(conta);
            agencias.add(agencia);
            
            String tipo = entrada.readLine();
            tipoUsuario = TipoUsuario.valueOf(tipo);
            
            if (tipoUsuario.equals(TipoUsuario.CLIENTE)) {
            
                nome = entrada.readLine();
                cpf = entrada.readLine();
                String numeroAgencia = entrada.readLine();
                String numeroConta = entrada.readLine();
                
                Agencia pesquisaAgencia = retornaAgencia(numeroAgencia);
                if (pesquisaAgencia != null)
                    agencia = pesquisaAgencia;
                else
                    System.out.println("Agencia não existe!");
                
                Conta pesquisaConta = agencia.retornaConta(numeroConta);
                if (pesquisaConta != null) {
                    conta = pesquisaConta;
                    
                    String pesquisaCpf = conta.retornaCorrentista(cpf);
                    if (pesquisaCpf != null) {
                        System.out.println("Cliente é correntista da conta!");
                    } else
                        System.out.println("Cliente não é correntista da conta!");
                            
                }
                else
                    System.out.println("Conta não existe");
                
                // não está autenticando ainda, mas está conferindo as informações corretamente
                
                System.out.println("\nInformações sobre o cliente conectado: ");
                System.out.println("Nome do cliente: " + nome);
                System.out.println("Número da agência: " + cpf );
                System.out.println("Número da agência: " + agencia.getNumero());
                System.out.println("Número da conta: " + conta.getNumero());
                
            }
            
            System.out.println("\nUm " + tipoUsuario + " se conectou!");
            System.out.println("\nTotal de sockets: " + usuarios.size() + "\n");
            
            String requisicao = entrada.readLine();
            String resposta;
            while(requisicao != null && !(requisicao.trim().equals(""))) {
                
                if(requisicao.equals("sair")) {
                    
                    saida.println("desconectado");
                    break;
                    
                }
                    
                resposta = requisicao;
                saida.println(resposta);
                
                String[] conteudo = requisicao.split("\\|");
                
                switch(tipoUsuario) {
                    
                    case CLIENTE:
                        
                        OperacaoCliente operacao = OperacaoCliente.valueOf(conteudo[0]);
                        Double valor = Double.valueOf(conteudo[1]);
                        
                        switch(operacao) {
                            
                            case VERIFICAR_SALDO:
                                System.out.println(conta.verificarSaldo());
                                break;
                            case DEPOSITAR:
                                conta.depositar(valor);
                                System.out.println("Saldo após o depósito: " + conta.verificarSaldo());
                                break;
                            case SACAR:
                                if (conta.sacar(valor))
                                    System.out.println("Saldo após o saque: " + conta.verificarSaldo());
                                else
                                    System.out.println("Não foi possível realizar o saque: valor maior que o saldo!");
                                break;
                            default:
                                System.out.println("Operação inválida!");
                            
                        }
                        
                        break;
                        
                    case ADMINISTRADOR:
                        // para implementar
                        break;
                        
                }
                
                saida.println("> ");
                requisicao = entrada.readLine();
                
            }
            
            usuarios.remove(usuario);
            
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
    
    private Agencia retornaAgencia(String numeroAgencia) {
        
        for(Agencia a : agencias) {
            
            if (a.getNumero().equals(numeroAgencia))
                return a;
            
        }
        
        return null;
                
    }
    
}
