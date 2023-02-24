package service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import model.Pagamento;

public class PagamentoService extends Thread {
    private Pagamento pagamento;
    private Integer numero;

    public Map<String, Object> VerificaData(LocalDate dataVencimento) {
        Map<String, Object> dataVerify = new HashMap<String, Object>();
        LocalDate dataHoje = LocalDate.now();
        dataVerify.put("dataHoje",LocalDate.now());
        dataVerify.put("semanasAtraso",(int) (Period.between(dataVencimento, dataHoje).toTotalMonths()) * 4);
        dataVerify.put("mesesAtraso",Period.between(dataVencimento, dataHoje).getMonths()
                + (Period.between(dataVencimento, dataHoje).getYears() * 12));
        if (dataVencimento.isBefore(dataHoje)) {
            dataVerify.put("estaAtrasado",true);
        } else {
            dataVerify.put("estaAtrasado",false);
        }

        return dataVerify;
    }
    
    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void processaPagamento() {
        Map<String, Object> verificaData = VerificaData(pagamento.getDataVencimento());
        System.out.println("[Thread " + numero +"] Processando pagamento: " + pagamento);
        if ((boolean) verificaData.get("estaAtrasado")) {
            aplicaMulta(pagamento);
            if ((int) verificaData.get("mesesAtraso") > 0) {
                int classificacao = (pagamento.getClassificacao() >= (int) verificaData.get("mesesAtraso"))
                        ? (pagamento.getClassificacao() - (int) verificaData.get("mesesAtraso"))
                        : 0;
                pagamento.setClassificacao(classificacao);
            }
        } else {
            aplicaDesconto(pagamento);
        }

    }

    public void aplicaMulta(Pagamento pagamento) {
        Map<String, Object> verificaData = VerificaData(pagamento.getDataVencimento());
        Double multa = 50.0;
        multa += (pagamento.getValor() * 0.01) * (int) verificaData.get("semanasAtraso");
        pagamento.setValor((double) Math.round((pagamento.getValor() + multa) * 100.0) / 100.0);
    } 

    public void aplicaDesconto(Pagamento pagamento) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);

        Double desconto = pagamento.getValor() * (pagamento.getClassificacao() / 100.0);
        if (desconto > 500) {
            desconto = 500.00;
        }
        pagamento.setValor((double) Math.round((pagamento.getValor() - desconto) * 100.0) / 100.0);

    }

    @Override
    public void run() {
        processaPagamento();
        super.run();
    }
}
