package SistemaBancario;

import java.util.ArrayList;
import java.util.List;

public class Conta {
    
    private Agencia agencia;
    private String numero;
    private Cliente cliente;
    private List<String> correntistas = new ArrayList<>();
    private Double saldo;

    public Conta(Agencia agencia, String numero, Cliente cliente) {
        
        this.agencia = agencia;
        this.numero = numero;
        this.cliente = cliente;
        
        correntistas.add(cliente.getCpf());
        
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

    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
    
    public void removeCorrentista(Cliente cliente) {
        correntistas.remove(cliente);
    }
    
    public boolean verificaCorrentista(String cpf) {
        
        for(String c : correntistas) {
            
            if (c.equals(cpf))
                return true;
            
        }
        
        return false;
        
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
    
    public String listaCorrentistas() {
        
        String listaCorrentistas = "";
        
        for (String c : correntistas)
            listaCorrentistas += c + " ; ";
        
        return listaCorrentistas;
        
    }
    
}
