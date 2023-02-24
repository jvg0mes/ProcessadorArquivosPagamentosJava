package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pagamento {

    private String clienteNome;
    private LocalDate dataVencimento;
    private Double valor;
    private Integer classificacao;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pagamento() {
    }

    public Pagamento(String clienteNome, LocalDate dataVencimento, Double valor, Integer classificacao) {
        this.clienteNome = clienteNome;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
        this.classificacao = classificacao;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(Integer classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public String toString() {
        return clienteNome + ";" + dateFormatter.format(dataVencimento) +
        ";" + String.valueOf(valor).replace(".", ",") + ";" + classificacao; 
    }


}
