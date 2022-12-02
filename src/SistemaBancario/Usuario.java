package SistemaBancario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Classe Usuario que requisita operações para o Servidor, podendo ser do tipo Cliente
 * ou Administrador. O Cliente pode requisitar para que o saldo de uma conta seja alterado e
 * o Administrador pode criar, alterar, ler ou deletar Agências e Contas. A classe pode requisitar
 * essas ações através de uma implementação de Sockets em Java, se conectado ao endereço do Servidor
 * @author Gabrielle Brambilla
 */
public class Usuario extends Thread {
    
    private String ip;
    private Socket socket;
    private PrintStream saida;
    private static boolean sair = false;
    
    /**
     * Método Construtor da classe Usuário
     */
    public Usuario() {
        
    }
    
    /**
     * Método Construtor da classe Usuário que atribui um valor para Socket
     * @param socket Socket
     */
    public Usuario(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * Método principal que inicia o Usuário. Implementa um Socket para se comunicar
     * com o Servidor através de ip:porta. Inicia pedindo o tipo do Usuário (Cliente
     * ou Administrador) para chamar a função que contenha o fluxo de mensagens para
     * enviar ao Servidor correspondente a cada tipo
     * @param args
     */
    public static void main(String args[]) {
        
        try {
        
            Socket socket = new Socket("127.0.0.1", 6666);

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
            
            if (tipo != null) {
                
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
                        "Operação cancelada",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                saida.println("SAIR");
                sair = true;
                
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
    
    /**
     * Método que forma e envia o fluxo de mensagens de um Usuário do tipo Cliente para
     * o Servidor. Inicia pedindo qual o CPF do Cliente e qual a Conta que ele deseja realizar as
     * operações. Após isso, o usuário pode requisitar quantas operações quiser em cima de saldo
     * da conta informada até que clique em sair
     * @param saida PrintStream - objeto para enviar mensagem para o Servidor
     * @exception IOException
     */
    private static void requisitarServicos_Cliente(PrintStream saida) throws IOException {
        
        String cpf = JOptionPane.showInputDialog(
                "Digite o seu CPF:"
        );
        
        if (cpf != null) {
        
            String numeroConta = JOptionPane.showInputDialog(
                    "Digite o número da conta:"
            );
            
            if (numeroConta != null) {
            
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
                                saida.println(numeroConta + "|" + cpf + "|" + operacao + "|" + valor);
                            else {

                                JOptionPane.showMessageDialog(
                                        null,
                                        "Operação cancelada",
                                        "Erro",
                                        JOptionPane.ERROR_MESSAGE
                                );

                            }

                        } else
                           saida.println(numeroConta + "|" + cpf + "|" + operacao + "|" + "0");

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

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Operação cancelada",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                saida.println("SAIR");
                sair = true;

            }
            
        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Operação cancelada",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            saida.println("SAIR");
            sair = true;
            
        }
        
    }
    
    /**
     * Método que forma e envia o fluxo de mensagens de um Usuário do tipo Administrador para
     * o Servidor. Pode requisitar quantas operações quiser (até que seja interrompido ao clicar em 
     * sair) em relação a criação, leitura, alteração e remoção de Agências e Contas
     * @param saida PrintStream - objeto para enviar mensagem para o Servidor
     * @exception IOException
     */
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
                                
                                if (numero != null) {
                                    
                                    String numeroConta = JOptionPane.showInputDialog(
                                            "Digite o número da conta:"
                                    );
                                    
                                    if (numeroConta != null) {
                                        
                                        String cpf = JOptionPane.showInputDialog(
                                                "Digite o CPF do cliente:"
                                        );
                                        
                                        if (cpf != null) {
                                            
                                            String nome = JOptionPane.showInputDialog(
                                                    "Digite o nome do cliente:"
                                            );

                                            if (nome != null)
                                                saida.println(opcao + "|" + acao + "|" + numero + "|" + numeroConta + "|" + cpf + "|" + nome);
                                            else {

                                                JOptionPane.showMessageDialog(
                                                        null,
                                                        "Operação cancelada",
                                                        "Erro",
                                                        JOptionPane.ERROR_MESSAGE
                                                );

                                            }
                                            
                                        }

                                    }
                                    
                                }
                                
                            }
                            
                            break;
                            
                        case "ALTERAR":
                            
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
                                        "NUMERO"
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
    
    /**
     * Método run que executa como thread, destinado à comunicação com o Servidor em que
     * estiver conectado, recebendo e mostrando as mensagens recebidas do Servidor para
     * as requisições enviadas, tanto de sucesso quanto de erros. REcebe e mostra até que seja
     * recebido a mensagem de desconectado pelo Servidor
     */
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
                            "Desconectado", 
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
                                "Mensagem", 
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
    
    /**
     * Método para retornar ip
     * @return ip String
     */
    public String getIp() {
        return ip;
    }
    
    /**
     * Método para atribuir valor para ip
     * @param ip String
     */
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    /**
     * Método para retornar socket
     * @return socket Socket
     */
    public Socket getSocket() {
        return socket;
    }
    
    /**
     * Método para atribuir valor para socket
     * @param socket Socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Método para retornar saida
     * @return saida PrintStream
     */
    public PrintStream getSaida() {
        return saida;
    }

    /**
     * Método para atribuir valor para saida
     * @param saida PrintStream
     */
    public void setSaida(PrintStream saida) {
        this.saida = saida;
    }
    
}