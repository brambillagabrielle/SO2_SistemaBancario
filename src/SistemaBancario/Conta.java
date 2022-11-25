package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

public class Conta {
    
    private Agencia agencia;
    private String numero;
    private String nomeCliente;
    private String cpf;
    private List<String> correntistas;
    private Double saldo;

    public Conta(Agencia agencia, String numero, String nomeCliente, String cpf) {
        
        this.agencia = agencia;
        this.numero = numero;
        
        this.nomeCliente = nomeCliente;
        this.cpf = cpf;
        
        correntistas = new ArrayList<>(3);
        correntistas.add(cpf);
        
        saldo = 0.0;
        
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<String> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(List<String> correntistas) {
        this.correntistas = correntistas;
    }
    
    public void adicionaCorrentista(String cpf) {
        correntistas.add(cpf);
    }
    
    public void removeCorrentista(String cpf) {
        correntistas.remove(cpf);
    }
    
    public String retornaCorrentista(String cpf) {
        
        for(String c : correntistas) {
            
            if (c.equals(cpf)) {
                return c;
            }
            
        }
        
        return null;
        
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double verificarSaldo() {
        return saldo;
    }
    
    public void depositar(Double valor) {
        saldo += valor;
    }
    
    public boolean sacar(Double valor) {
        
        if (saldo < valor)
            return false;
        
        saldo -= valor;
        return true;
        
    }
    
}
