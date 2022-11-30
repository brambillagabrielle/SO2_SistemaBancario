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
    private static boolean sair = false;
    
    public Usuario() {
        
    }

    public Usuario(Socket socket) {
        this.socket = socket;
    }
    
    public static void main(String args[]) {
        
        try {
        
            Socket socket = new Socket("127.0.0.1", 6666);

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream())
            );
            PrintStream saida = new PrintStream(
                    socket.getOutputStream()
            );
            
            String tipo = (String) JOptionPane.showInputDialog(
                    null,
                    "Logar como...",
                    "Login",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {
                        "CLIENTE",
                        "ADMINISTRADOR"
                    },
                    "CLIENTE"
            );
            saida.println(tipo);
            
            String cpf;
            if (tipo.equals("CLIENTE")) {
                
                cpf = JOptionPane.showInputDialog(
                    "Digite seu CPF:"
                );
                
            } else
                cpf = "00000000000";
            
            saida.println(cpf);
            String resposta = entrada.readLine();
            
            if (resposta.equals("CONECTADO") || tipo.equals("ADMINISTRADOR")) {
                
                Thread thread = new Usuario(socket);
                thread.start();
                
                switch(tipo) {
                
                    case "CLIENTE":
                        requisitarServicos_Cliente(saida);
                        break;
                        
                    case "ADMINISTRADOR":
                        requisitarServicos_Administrador(saida);
                        
                }
                
            } else {
                
                JOptionPane.showMessageDialog(
                        null,
                        "CPF não registrado no sistema\nUsuário desconectado!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                
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
    
    private static void requisitarServicos_Cliente(PrintStream saida) throws IOException {

        while(true) {

            if (sair)
                break;

            String operacao = (String) JOptionPane.showInputDialog(
                    null,
                    "Que operação deseja fazer?",
                    "Operação",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {
                        "VERIFICAR_SALDO",
                        "DEPOSITAR",
                        "SACAR"
                    },
                    "VERIFICAR_SALDO"
            );

            if (operacao != null) {

                if(!(operacao.equals("VERIFICAR_SALDO"))) {

                    String valor = JOptionPane.showInputDialog(
                        "Digite o valor para " + operacao + ":"
                    );

                    if (valor != null)
                        saida.println(operacao + "|" + valor);
                    else {

                        JOptionPane.showMessageDialog(
                                null,
                                "Operação cancelada",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );

                    }

                } else
                    saida.println(operacao + "|" + "0");

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Operação cancelada",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );

            }

            int acao = JOptionPane.showOptionDialog(
                    null, 
                    "Deseja continuar a realizar operações?", 
                    "Continuar?", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    new String[] {
                        "CONTINUAR",
                        "SAIR"
                    },
                    "CONTINUAR"
            );

            if (acao != 0) {
                saida.println("SAIR");
                sair = true;
            }

        }
        
    }
    
    private static void requisitarServicos_Administrador(PrintStream saida) throws IOException {
        
        while(true) {

            if (sair)
                break;

            String opcao = (String) JOptionPane.showInputDialog(
                    null,
                    "Deseja criar/alterar/ver um(a): ",
                    "Opção",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {
                        "CONTA",
                        "AGENCIA"
                    },
                    "CONTA"
            );
            
            if (opcao != null) {
                
                String acao = (String) JOptionPane.showInputDialog(
                        null,
                        "Que ação deseja realizar em " + opcao + "?",
                        "Opção",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[] {
                            "LER",
                            "CRIAR",
                            "ALTERAR",
                            "DELETAR"
                        },
                        "LER"
                );
                
                if (acao != null) {
                    
                    String numero;
                    switch (acao) {
                        
                        case "LER":
                        case "DELETAR":
                            
                            numero = JOptionPane.showInputDialog(
                                    "Digite o número da " + opcao + ":"
                            );
                            
                            if (numero != null)
                                saida.println(opcao + "|" + acao + "|" + numero);
                            else {

                                JOptionPane.showMessageDialog(
                                        null,
                                        "Operação cancelada",
                                        "Erro",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                
                            }
                            
                            break;
                            
                        case "CRIAR":
                            
                            if (opcao.equals("AGENCIA")) {
                                
                                numero = JOptionPane.showInputDialog(
                                        "Digite o número da agência:"
                                );
                                
                                String descricao = JOptionPane.showInputDialog(
                                        "Digite a descrição da agência:"
                                );
                                
                                if (numero != null || descricao != null)
                                    saida.println(opcao + "|" + acao + "|" + numero + "|" + descricao);
                                else {
                                    
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Operação cancelada",
                                            "Erro",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                    
                                }
                                
                            } else {
                                
                                numero = JOptionPane.showInputDialog(
                                        "Digite o número da agência:"
                                );
                                
                                String numeroConta = JOptionPane.showInputDialog(
                                        "Digite o número da conta:"
                                );
                                
                                String cpf = JOptionPane.showInputDialog(
                                        "Digite o CPF do cliente:"
                                );
                                
                                if (numero != null || numeroConta != null || cpf != null)
                                    saida.println(opcao + "|" + acao + "|" + numero + "|" + numeroConta + "|" + cpf);
                                else {
                                    
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Operação cancelada",
                                            "Erro",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                                    
                                }
                                
                            }
                            
                            break;
                            
                        default:
                            
                            numero = JOptionPane.showInputDialog(
                                    "Digite o número da " + opcao + ":"
                            );
                            
                            if (numero != null) {
                                
                                String[] opcoesAtributo;
                                if (opcao.equals("AGENCIA")) {

                                    opcoesAtributo = new String[]{
                                        "NUMERO",
                                        "DESCRICAO"
                                    };

                                } else {

                                    opcoesAtributo = new String[]{
                                        "AGENCIA",
                                        "NUMERO",
                                        "CLIENTE"
                                    };

                                }

                                String atributo = (String) JOptionPane.showInputDialog(
                                        null,
                                        "O que deseja alterar em " + opcao + "?",
                                        "Opção",
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        opcoesAtributo,
                                        opcoesAtributo[0]
                                );
                                
                                if (atributo != null) {
                                        
                                    String valorAtributo = JOptionPane.showInputDialog(
                                            "Digite o(a) " + atributo + " para alterar:"
                                    );

                                    if (valorAtributo != null)
                                        saida.println(opcao + "|" + acao + "|" + numero + "|" + atributo + "|" + valorAtributo);
                                    else {

                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Operação cancelada",
                                                "Erro",
                                                JOptionPane.ERROR_MESSAGE
                                        );

                                    }

                                } else {

                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Operação cancelada",
                                            "Erro",
                                            JOptionPane.ERROR_MESSAGE
                                    );

                                }
                                
                            } else {
                                
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Operação cancelada",
                                        "Erro",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                
                            }
                            
                            break;
                    }
                    
                }
                
            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Operação cancelada",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );

            }

            int acao = JOptionPane.showOptionDialog(
                    null, 
                    "Deseja continuar a realizar operações?", 
                    "Continuar?", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    new String[] {
                        "CONTINUAR",
                        "SAIR"
                    },
                    "CONTINUAR"
            );

            if (acao != 0) {
                saida.println("SAIR");
                sair = true;
            }

        }
        
    }
    
    @Override
    public void run() {
        
        try {
        
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream())
            );
                
            String resposta, tipoResposta;
            while(true) {

                resposta = entrada.readLine();

                if(resposta.equals("DESCONECTADO")) {

                    sair = true;
                    JOptionPane.showMessageDialog(
                            null, 
                            "Conexão encerrada!", 
                            "Sucesso", 
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;

                } else {

                    String[] mensagem = resposta.split("\\|");
                    tipoResposta = mensagem[0];
                    resposta = mensagem[1];

                    if(tipoResposta.equals("MENSAGEM")) {

                        JOptionPane.showMessageDialog(
                                null, 
                                resposta, 
                                "Sucesso", 
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                                null, 
                                resposta, 
                                "Erro", 
                                JOptionPane.ERROR_MESSAGE
                        );

                    }

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
    
}
