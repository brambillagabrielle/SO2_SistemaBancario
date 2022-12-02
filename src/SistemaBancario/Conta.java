package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para objetos do tipo Conta, contendo uma lista de correntistas, saldo
 * e métodos para manipular o valor do saldo
 * @author Gabrielle Brambilla
 */
public class Conta {
    
    private Agencia agencia;
    private String numero;
    private Cliente cliente;
    private List<String> correntistas = new ArrayList<>();
    private Double saldo;

    /**
     * Método Construtor da classe Conta que atribui valores para agencia, numero e
     * cliente, adiciona o cliente passado como correntista e inicia o saldo como 0.0
     * @param agencia Agencia
     * @param numero String
     * @param cliente Cliente
     */
    public Conta(Agencia agencia, String numero, Cliente cliente) {
        
        this.agencia = agencia;
        this.numero = numero;
        this.cliente = cliente;
        
        correntistas.add(cliente.getCpf());
        
        saldo = 0.0;
        
    }

    /**
     * Método para retornar agencia
     * @return agencia Agencia
     */
    public Agencia getAgencia() {
        return agencia;
    }

    /**
     * Método para atribuir valor para agencia
     * @param agencia Agencia
     */
    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
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
     * Método para retornar cliente
     * @return cliente Cliente
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    /**
     * Método para atribuir valor para cliente
     * @param cliente Cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Método para retornar lista de correntistas
     * @return correntistas List(String)
     */
    public List<String> getCorrentistas() {
        return correntistas;
    }
    
    /**
     * Método para atribuir lista de correntistas
     * @param correntistas List(String)
     */
    public void setCorrentistas(List<String> correntistas) {
        this.correntistas = correntistas;
    }
    
    /**
     * Método para adicionar correntista a lista de correntistas
     * @param cpf String
     */
    public void adicionaCorrentista(String cpf) {
        correntistas.add(cpf);
    }
    
    /**
     * Método para remover correntista de lista de correntistas
     * @param cpf String
     */
    public void removeCorrentista(String cpf) {
        correntistas.remove(cpf);
    }
    
    /**
     * Método para verificar se um correntista existe na conta através do cpf. Retorna
     * true para o caso em que o cpf consta na lista e false se não constar
     * @param cpf String
     * @return boolean
     */
    public boolean verificaCorrentista(String cpf) {
        
        for(String c : correntistas) {
            
            if (c.equals(cpf))
                return true;
            
        }
        
        return false;
        
    }
    
    /**
     * Método para atribuir valor para saldo
     * @param saldo Double
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
    /**
     * Método para verificar (retornar) o valor do saldo atual
     * @return saldo Double
     */
    public Double verificarSaldo() {
        return saldo;
    }
    
    /**
     * Método para depositar (somar) valor do saldo
     * @param valor Double
     */
    public void depositar(Double valor) {
        saldo += valor;
    }
    
    /**
     * Método para sacar (subtrair) valor do saldo. Checa se o valor para subtrair 
     * não é maior do que o saldo, retornando true quando o valor pode ser subtraido
     * e false quando não pode
     * @param valor Double
     * @return boolean
     */
    public boolean sacar(Double valor) {
        
        if (saldo < valor)
            return false;
        
        saldo -= valor;
        return true;
        
    }
    
    /**
     * Método para retornar uma String com uma lista de correntistas
     * @return listaCorrentistas String
     */
    public String listaCorrentistas() {
        
        String listaCorrentistas = "";
        
        for (String c : correntistas)
            listaCorrentistas += c + " ; ";
        
        return listaCorrentistas;
        
    }
    
}
