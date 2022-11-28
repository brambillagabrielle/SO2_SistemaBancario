package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

public class Conta {
    
    private Agencia agencia;
    private String numero;
    private Cliente cliente;
    private List<Cliente> correntistas;
    private Double saldo;

    public Conta(Agencia agencia, String numero, Cliente cliente) {
        
        this.agencia = agencia;
        this.numero = numero;
        this.cliente = cliente;
        
        correntistas = new ArrayList<>(3);
        correntistas.add(cliente);
        
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

    public List<Cliente> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(List<Cliente> correntistas) {
        this.correntistas = correntistas;
    }
    
    public void adicionaCorrentista(Cliente cliente) {
        correntistas.add(cliente);
    }
    
    public void removeCorrentista(Cliente cliente) {
        correntistas.remove(cliente);
    }
    
    public Cliente retornaCorrentista(String cpf) {
        
        for(Cliente c : correntistas) {
            
            if (c.getCpf().equals(cpf))
                return c;
            
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
