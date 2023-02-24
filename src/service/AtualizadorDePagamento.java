package service;

import model.Pagamento;

import java.io.IOException;

public class AtualizadorDePagamento extends Thread {
    private Pagamento pagamento;
    private PagamentoDAO pagamentoDao = new PagamentoDAO();
    private Integer numero;

    public AtualizadorDePagamento() throws IOException {
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    public void setNumero(){
        numero++;
    }
    public void setPagamento(Pagamento pagamento){
        this.pagamento = pagamento;
    }

    @Override
    public void run() {
        try {
            pagamentoDao.atualizarPagamento(pagamento,numero);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.run();
    }

    
}
