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
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Agencia> agencias = new ArrayList<>();

    public Servidor(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public static void main(String args[]) {
        
        System.out.println("SERVIDOR SISTEMA BANCÁRIO");
        System.out.println("Uptime: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        
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
                
            System.out.println();
            System.out.println("Um " + tipo + " se conectou!");
            System.out.println("Total de sockets: " + usuarios.size());
            
            String requisicao = entrada.readLine();
            while(requisicao != null && !(requisicao.trim().equals(""))) {

                if(requisicao.equals("SAIR")) {

                    saida.println("DESCONECTADO");
                    break;

                }

                String[] mensagem;
                switch(tipo) {

                    case "CLIENTE":

                        mensagem = requisicao.split("\\|");
                        String numeroConta = String.valueOf(mensagem[0]);

                        Conta c = retornaConta(numeroConta);
                        
                        if (c != null) {
                            
                            if (c.getCorrentistas().size() < 3) {
                                
                                String cpf = String.valueOf(mensagem[1]);
                                
                                if (!c.verificaCorrentista(cpf))
                                    c.getCorrentistas().add(cpf);
                            
                                String operacao = String.valueOf(mensagem[2]);

                                try {

                                    Double valor = Double.valueOf(mensagem[3]);

                                    switch(operacao) {

                                        case "VERIFICAR_SALDO":

                                            saida.println("MENSAGEM|Saldo atual: " 
                                                    + NumberFormat.getCurrencyInstance().format(c.verificarSaldo()));
                                            break;

                                        case "DEPOSITAR":

                                            c.depositar(valor);
                                            saida.println("MENSAGEM|Valor depositado!");
                                            break;

                                        case "SACAR":

                                            if (c.sacar(valor))
                                                saida.println("MENSAGEM|Valor sacado!");
                                            else
                                                saida.println("MENSAGEM_ERRO|Saldo insuficiente!");

                                    }

                                } catch (Exception e) {
                                    saida.println("MENSAGEM_ERRO|Valor inserido inválido! " + e.getMessage());
                                }
                                
                            } else
                                saida.println("MENSAGEM_ERRO|Limite de correntistas (3) atingido!");
                            
                        } else
                            saida.println("MENSAGEM_ERRO|Conta não existe!");

                        break;

                    case "ADMINISTRADOR":

                        mensagem = requisicao.split("\\|");
                        String objeto = String.valueOf(mensagem[0]);
                        String operacaoAdmin = String.valueOf(mensagem[1]);

                        String atributo, valorAtributo;                            
                        if (objeto.equals("AGENCIA")) {

                            String numeroAgencia;
                            Agencia a;
                            switch(operacaoAdmin) {

                                case "LER":

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

                                case "CRIAR":

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

                                case "ALTERAR":

                                    numeroAgencia = String.valueOf(mensagem[2]);
                                    atributo = String.valueOf(mensagem[3]);
                                    valorAtributo = String.valueOf(mensagem[4]);

                                    a = retornaAgencia(numeroAgencia);

                                    if (a != null) {

                                        switch(atributo) {

                                            case "NUMERO":

                                                a.setNumero(valorAtributo);
                                                saida.println("MENSAGEM|Agência alterada!");
                                                break;

                                            case "DESCRICAO":

                                                a.setDescricao(valorAtributo);
                                                saida.println("MENSAGEM|Agência alterada!");

                                        }

                                    } else
                                        saida.println("MENSAGEM_ERRO|Agência não existe!");

                                    break;

                                case "DELETAR":

                                    numeroAgencia = String.valueOf(mensagem[2]);

                                    a = retornaAgencia(numeroAgencia);

                                    if (a != null) {

                                        agencias.remove(retornaAgencia(numeroAgencia));
                                        saida.println("MENSAGEM|Agência e contas associadas foram deletadas!");

                                    } else
                                        saida.println("MENSAGEM_ERRO|Agência não existe!");

                            }

                        } else {

                            switch(operacaoAdmin) {

                                case "LER":

                                    numeroConta = String.valueOf(mensagem[2]);
                                    c = retornaConta(numeroConta);

                                    if (c != null)
                                        saida.println(
                                            "MENSAGEM|Agência " + c.getAgencia().getNumero()
                                            + " / Número: " + c.getNumero()
                                            + " / Cliente: " + c.getCliente().getCpf()
                                            + " / Correntistas: " + c.listaCorrentistas()
                                        );
                                    else
                                        saida.println("MENSAGEM_ERRO|Conta não existe!");

                                    break;

                                case "CRIAR":

                                    String numeroAgencia = String.valueOf(mensagem[2]);
                                    Agencia a = retornaAgencia(numeroAgencia);

                                    if (a != null) {

                                        numeroConta = String.valueOf(mensagem[3]);
                                        c = retornaConta(numeroConta);

                                        if (c == null) {

                                            String cpf = String.valueOf(mensagem[4]);
                                            String nome = String.valueOf(mensagem[5]);

                                            Cliente cl = new Cliente(nome, cpf);
                                            Conta novaConta = new Conta(a, numeroConta, cl);

                                            a.adicionaConta(novaConta);

                                            saida.println("MENSAGEM|Conta criada!");

                                        } else
                                            saida.println("MENSAGEM_ERRO|Conta já existe!");

                                    } else
                                        saida.println("MENSAGEM_ERRO|Agência não existe!");

                                    break;

                                case "ALTERAR":

                                    numeroConta = String.valueOf(mensagem[2]);

                                    c = retornaConta(numeroConta);
                                    if (c != null) {

                                        atributo = String.valueOf(mensagem[3]);
                                        valorAtributo = String.valueOf(mensagem[4]);

                                        switch(atributo) {

                                            case "AGENCIA":
                                                
                                                atributo = String.valueOf(mensagem[3]);
                                                valorAtributo = String.valueOf(mensagem[4]);
                                                
                                                Agencia novaAgencia = retornaAgencia(valorAtributo);

                                                if (novaAgencia != null) {

                                                    c.setAgencia(novaAgencia);
                                                    saida.println("MENSAGEM|Conta alterada!");

                                                } else
                                                    saida.println("MENSAGEM_ERRO|Agência não existe!");

                                                break;

                                        }

                                    } else
                                        saida.println("MENSAGEM_ERRO|Conta não existe!");

                                    break;

                                case "DELETAR":

                                    numeroConta = String.valueOf(mensagem[2]);

                                    c = retornaConta(numeroConta);

                                    if (c != null) {

                                        a = c.getAgencia();
                                        a.removeConta(c);
                                        saida.println("MENSAGEM|Conta foi deletada!");

                                    } else
                                        saida.println("MENSAGEM_ERRO|Conta não existe!");

                            }

                        }

                }

                requisicao = entrada.readLine();

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
        
        System.out.println();
        System.out.println("Usuário " + usuario.getIp() + " se desconectou!");
        System.out.println("Total de sockets: " + usuarios.size());
        
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
    
}