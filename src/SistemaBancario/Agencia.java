package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

public class Agencia {
    
    private String numero;
    private String descricao;
    private List<Conta> contas = new ArrayList<>();
    
    public Agencia() {
        
    }

    public Agencia(String numero, String descricao) {
        
        this.numero = numero;
        this.descricao = descricao;
        
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
    
    public void adicionaConta(Conta conta) {
        contas.add(conta);
    }
    
    public void removeConta(Conta conta) {
        contas.remove(conta);
    }
    
    public Conta retornaConta(String numeroConta) {
        
        for(Conta c : contas) {
            
            if (c.getNumero().equals(numeroConta)) {
                return c;
            }
            
        }
        
        return null;
                
    }
    
}
