import model.Pagamento;
import service.AtualizadorDePagamento;
import service.PagamentoDAO;
import service.PagamentoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        var pagamentoDao = new PagamentoDAO();
        var pagamentos = pagamentoDao.getListaPagamentos();
        List<Thread> threads = new ArrayList<>();

        int pagamentosPorThread = 2;
        
        List<List<Pagamento>> listaDeListas = new ArrayList<List<Pagamento>>();
        for (int i = 0; i < pagamentos.size(); i += pagamentosPorThread) {
            listaDeListas.add(pagamentos.subList(i,
                    Math.min(i + pagamentosPorThread, pagamentos.size())));
        }

        for (int i = 0; i < listaDeListas.size(); i++) {
            PagamentoService pagamentoService = new PagamentoService();
            AtualizadorDePagamento atualizadorDePagamentos = new AtualizadorDePagamento();
            final int h = i;
            Runnable runnable = () -> {
                for (int j = 0; j < listaDeListas.get(h).size(); j++) {
                    pagamentoService.setPagamento(listaDeListas.get(h).get(j));
                    pagamentoService.setNumero(h);
                    pagamentoService.run();
                    atualizadorDePagamentos.setPagamento(listaDeListas.get(h).get(j));
                    atualizadorDePagamentos.setNumero(h);
                    atualizadorDePagamentos.run();

                }
            };

            Thread thread = new Thread(runnable);
            thread.setName("Thread " + (threads.size() + 1));
            threads.add(thread);
        }

        threads.forEach(thread -> {
            thread.start();
        });

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
