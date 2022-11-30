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
            Cliente cliente = new Cliente("12345678910");
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
            Cliente clienteExiste = retornaCliente(cpf);
            
            if(tipoUsuario.equals(TipoUsuario.ADMINISTRADOR) || clienteExiste != null) {
                
                saida.println("CONECTADO");
                
                String requisicao = entrada.readLine();
                while(requisicao != null && !(requisicao.trim().equals(""))) {

                    if(requisicao.equals("SAIR")) {

                        saida.println("DESCONECTADO");
                        break;

                    }
                    
                    String[] mensagem;
                    switch(tipoUsuario) {
                        
                        case CLIENTE:
                            
                            mensagem = requisicao.split("\\|");
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
                            
                            mensagem = requisicao.split("\\|");
                            String objeto = String.valueOf(mensagem[0]);
                            OperacaoAdmin operacaoAdmin = OperacaoAdmin.valueOf(mensagem[1]);
                            
                            String atributo, valorAtributo;                            
                            if (objeto.equals("AGENCIA")) {
                                
                                String numeroAgencia;
                                Agencia a;
                                switch(operacaoAdmin) {
                                
                                    case LER:
                                        
                                        numeroAgencia = String.valueOf(mensagem[2]);
                                        a = retornaAgencia(numeroAgencia);
                                        
                                        if (a != null)
                                            saida.println(
                                                    "MENSAGEM|Número: " + a.getNumero()
                                                    + " / Descrição: " + a.getDescricao()
                                            );
                                        else
                                            saida.println("MENSAGEM_ERRO|Agência não existe!");
                                        
                                        break;

                                    case CRIAR:
                                        
                                        numeroAgencia = String.valueOf(mensagem[2]);
                                        a = retornaAgencia(numeroAgencia);
                                        
                                        if (a == null) {
                                            
                                            String descricao = String.valueOf(mensagem[3]);
                                            
                                            Agencia novaAgencia = new Agencia(numeroAgencia, descricao);
                                            agencias.add(novaAgencia);
                                            
                                            saida.println("MENSAGEM|Agência criada!");
                                            
                                        } else
                                            saida.println("MENSAGEM_ERRO|Agência já existe!");
                                        
                                        break;

                                    case ALTERAR:

                                        numeroAgencia = String.valueOf(mensagem[2]);
                                        atributo = String.valueOf(mensagem[3]);
                                        valorAtributo = String.valueOf(mensagem[4]);
                                        
                                        a = retornaAgencia(numeroAgencia);
                                        agencias.remove(a);
                                        
                                        switch(atributo) {
                                            
                                            case "NUMERO":
                                                
                                                a.setNumero(valorAtributo);
                                                break;
                                                
                                            case "DESCRICAO":
                                                
                                                a.setDescricao(valorAtributo);
                                                
                                        }
                                        
                                        agencias.add(a);
                                        
                                        saida.println("MENSAGEM|Agência alterada!");
                                        break;

                                    case DELETAR:
                                        
                                        numeroAgencia = String.valueOf(mensagem[2]);
                                        agencias.remove(retornaAgencia(numeroAgencia));
                                        saida.println("MENSAGEM|Agência e contas associadas foram deletadas!");

                                }
                                
                            } else {
                                
                                String numeroConta;
                                Conta c;
                                switch(operacaoAdmin) {
                                
                                    case LER:

                                        numeroConta = String.valueOf(mensagem[2]);
                                        c = retornaConta(numeroConta);
                                        
                                        if (c != null)
                                            saida.println(
                                                "MENSAGEM|Agência " + c.getAgencia().getNumero()
                                                + " / Número: " + c.getNumero()
                                                + " / Cliente: " + c.getCliente().getCpf()
                                            );
                                        else
                                            saida.println(
                                                    "MENSAGEM_ERRO|Conta não existe!");
                                        
                                        break;

                                    case CRIAR:
                                        
                                        String numeroAgencia = String.valueOf(mensagem[2]);
                                        Agencia a = retornaAgencia(numeroAgencia);
                                        
                                        if (a != null) {
                                            
                                            numeroConta = String.valueOf(mensagem[3]);
                                            c = retornaConta(numeroConta);
                                            
                                            if (c == null) {
                                                
                                                cpf = String.valueOf(mensagem[4]);
                                                
                                                agencias.remove(a);
                                                
                                                Cliente cl = new Cliente(cpf);
                                                Conta novaConta = new Conta(a, numeroConta, cl);
                                                a.adicionaConta(novaConta);
                                                
                                                agencias.add(a);
                                                
                                                saida.println("MENSAGEM|Conta criada!");
                                                
                                            } else
                                                saida.println("MENSAGEM_ERRO|Conta já existe!");
                                            
                                        } else
                                            saida.println("MENSAGEM_ERRO|Agência não existe!");
                                        
                                        break;

                                    case ALTERAR:
                                        
                                        numeroConta = String.valueOf(mensagem[2]);
                                        atributo = String.valueOf(mensagem[3]);
                                        valorAtributo = String.valueOf(mensagem[4]);
                                        
                                        // para implementar
                                        
                                        break;

                                    case DELETAR:
                                        
                                        numeroConta = String.valueOf(mensagem[2]);
                                        
                                        // para implementar
                                        
                                        saida.println("");
                                        
                                     
                                    // PRECISA IMPLEMENTAR A PARTE DOS CORRENTISTAS E MELHORAR A PARTE DOS CLIENTES

                                }
                                
                            }
                        
                    }
                    
                    requisicao = entrada.readLine();

                }
                
            } else
                saida.println("DESCONECTADO");
            
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
    
    private Conta retornaConta(String numeroConta) {
        
        for(Agencia a : agencias) {
            
            for(Conta c : a.getContas()) {
                
                if (c.getNumero().equals(numeroConta))
                    return c;
                
            }
            
        }
        
        return null;
        
    }
    
    private Cliente retornaCliente(String cpf) {
        
        Cliente cliente;
        
        for(Agencia a : agencias) {
            
            for(Conta c : a.getContas()) {
                
                cliente = c.retornaCorrentista(cpf);
                if (c.retornaCorrentista(cpf) != null)
                    return cliente;
                
            }
            
        }
        
        return null;
        
    }
    
}
