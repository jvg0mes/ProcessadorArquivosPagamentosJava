package service;

import model.Pagamento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAO extends Thread {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-yyyy");
    private static String filesPathName = "arquivos";
    private List<Path> arquivosDaPasta;
    private List<Pagamento> listaPagamentos;


    public PagamentoDAO() throws IOException {
    }

    public void atualizarPagamento(Pagamento pagamento,int threadNumber) throws IOException{

        StringBuilder stringBuilder = new StringBuilder();

        Path arquivo = Paths.get("pagamentosAtualizados_"+
                dateFormatter.format(pagamento.getDataVencimento())+
                ".csv");
        if(!Files.exists(arquivo)){
            Files.createFile(arquivo);
        }else{
            stringBuilder.append(Files.readString(arquivo));
        }

        stringBuilder.append(pagamento.toString());
        stringBuilder.append("\n");

        byte[] bytesTexto = stringBuilder.toString().getBytes();

        System.out.println("[Thread " + threadNumber + "] Atualizando pagamento: " + pagamento);

        Files.write(arquivo, bytesTexto);
    }

    private static Pagamento converter(String[] arquivo) {
        Pagamento pagamento = new Pagamento();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        pagamento.setClienteNome(arquivo[0]);
        pagamento.setDataVencimento(LocalDate.parse(arquivo[1], dateFormatter));
        pagamento.setValor(Double.parseDouble(arquivo[2]));
        pagamento.setClassificacao(Integer.parseInt(arquivo[3]));

        return pagamento;
    }

    private List<Path> lerArquivosDaPasta(String pasta) throws IOException {
        Path pastaArquivos = Paths.get(pasta);
        String[] nomesArquivos = pastaArquivos.toFile().list();
        List<Path> arquivos = new ArrayList<>();
        for (String arquivo : nomesArquivos) {
            arquivos.add(Paths.get(arquivo));
        }
        return arquivos;
    }

    private static String[] lerArquivo(Path arquivo) throws IOException {
        Path caminhoArquivo = Paths.get("arquivos\\" + arquivo);
        // System.out.println(caminhoArquivo);

        String[] conteudo = Files.readString(caminhoArquivo).split(";");
        conteudo[0] = conteudo[0].replaceAll("[^a-zA-Z0-9]", " ").trim();
        conteudo[2] = conteudo[2].replaceAll(",", ".");
        conteudo[3] = conteudo[3].trim();

        return conteudo;
    }

    private List<Pagamento> pegarListaPagamentos(List<Path> arquivosDaPasta){
        List<Pagamento> pagamentos = new ArrayList<>();
        for (int i = 0; i < arquivosDaPasta.size(); i++) {
            String[] colunas;
            try {
                colunas = lerArquivo(arquivosDaPasta.get(i));
                Pagamento pagamento = converter(colunas);
                System.out.println("Lendo pagamento: " + pagamento);


                pagamentos.add(pagamento);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pagamentos;

    }

    public List<Pagamento> getListaPagamentos() throws IOException {
        arquivosDaPasta = this.lerArquivosDaPasta(filesPathName);
        listaPagamentos = this.pegarListaPagamentos(arquivosDaPasta);
        return listaPagamentos;
    }

}
