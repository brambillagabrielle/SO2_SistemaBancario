package SistemaBancario;

/**
 * Classe para objetos do tipo Cliente
 * @author Gabrielle Brambilla
 */
public class Cliente {
    
    private String nome;
    private String cpf;

    /**
     * Método Construtor da classe Cliente que atribui valores para nome e cpf
     * @param nome String
     * @param cpf String
     */
    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
    
    /**
     * Método para retornar nome
     * @return nome String
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método para atribuir valor para nome
     * @param nome String
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Método para retornar cpf
     * @return cpf String
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Método para atribuir valor para cpf
     * @param cpf String
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
}
