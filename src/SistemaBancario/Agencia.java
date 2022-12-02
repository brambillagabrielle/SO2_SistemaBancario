package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para objetos do tipo Agência, contendo uma lista de contas
 * @author Gabrielle Brambilla
 */
public class Agencia {
    
    private String numero;
    private String descricao;
    private List<Conta> contas = new ArrayList<>();
    
    /**
     * Método Construtor da classe Agência
     */
    public Agencia() {
        
    }

    /**
     * Método Construtor da classe Agência que atribui valores para numero e
     * descricao
     * @param numero String
     * @param descricao String
     */
    public Agencia(String numero, String descricao) {
        
        this.numero = numero;
        this.descricao = descricao;
        
    }

    /**
     * Método para retornar numero
     * @return numero String
     */
    public String getNumero() {
        return numero;
    }
    
    /**
     * Método para atribuir valor para numero
     * @param numero String
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    /**
     * Método para retornar descricao
     * @return descricao String
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Método para atribuir valor para descricao
     * @param descricao String
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Método para retornar lista de contas
     * @return contas List(Conta)
     */
    public List<Conta> getContas() {
        return contas;
    }

    /**
     * Método para atribuir lista de contas
     * @param contas List(Conta)
     */
    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
    
    /**
     * Método para adicionar conta a lista de contas
     * @param conta Conta
     */
    public void adicionaConta(Conta conta) {
        contas.add(conta);
    }
    
    /**
     * Método para remover conta de lista de contas
     * @param conta Conta
     */
    public void removeConta(Conta conta) {
        contas.remove(conta);
    }
    
}
