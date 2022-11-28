package SistemaBancario;

public class Cliente {
    
    private static String nome;
    private static String cpf;

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        Cliente.nome = nome;
    }

    public static String getCpf() {
        return cpf;
    }

    public static void setCpf(String cpf) {
        Cliente.cpf = cpf;
    }
    
}
