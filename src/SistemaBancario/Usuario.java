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

            PrintStream saida = new PrintStream(
                    socket.getOutputStream()
            );
            BufferedReader teclado = new BufferedReader(
                    new InputStreamReader(System.in)
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

            Thread thread = new Usuario(socket);
            thread.start();
            
            switch(tipo) {
                
                case "CLIENTE":
                    
                    String cpf = JOptionPane.showInputDialog(
                        "Digite seu CPF:"
                    );
                    saida.println(cpf);

                    requisitarServicos_Cliente(socket);
                    break;
                    
                case "ADMINISTRADOR":
                    
                    saida.println("0");
                    requisitarServicos_Administrador(socket);
                
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
    
    private static void requisitarServicos_Cliente(Socket socket) {
        
        try {
        
            PrintStream saida = new PrintStream(
                    socket.getOutputStream()
            );

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
            
        } catch(IOException e) {
            
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            
        }
        
    }
    
    private static void requisitarServicos_Administrador(Socket socket) {
        
        try {
        
            PrintStream saida = new PrintStream(
                    socket.getOutputStream()
            );
            BufferedReader teclado = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            
            String requisicao;
            while(true) {

                if (sair)
                    break;

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
                
            String resposta = entrada.readLine(), tipoResposta;
            if (resposta.equals("CONECTADO")) {
                
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
