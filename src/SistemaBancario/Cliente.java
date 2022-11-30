package SistemaBancario;

public class Cliente {
    
    private static String cpf;

    public Cliente(String cpf) {
        this.cpf = cpf;
    }

    public static String getCpf() {
        return cpf;
    }

    public static void setCpf(String cpf) {
        Cliente.cpf = cpf;
    }
    
}
