package service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Novo Import

import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;

public class ProdutoService {
    private ProdutoDAO produtoDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public ProdutoService() {
        try {
            produtoDAO = new ProdutoDAO("produto.dat");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Object add(Request request, Response response) {
        String descricao = request.queryParams("descricao");
        float preco = Float.parseFloat(request.queryParams("preco"));
        int quantidade = Integer.parseInt(request.queryParams("quantidade"));
        
        // CORREÇÃO: Usando o formatador para aceitar o formato de input HTML
        LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"), formatter);
        
        LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));

        int id = produtoDAO.getMaxId() + 1;
        Produto produto = new Produto(id, descricao, preco, quantidade, dataFabricacao, dataValidade);

        produtoDAO.add(produto);

        response.status(201);
        return "Produto adicionado com sucesso!";
    }
    
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);
        if (produto != null) {
            return produto.toString(); 
        } else {
            response.status(404);
            return "Produto não encontrado!";
        }
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);

        if (produto != null) {
            produto.setDescricao(request.queryParams("descricao"));
            produto.setPreco(Float.parseFloat(request.queryParams("preco")));
            
            // CORREÇÃO: Setter setQuant (consistente com Produto.java)
            produto.setQuant(Integer.parseInt(request.queryParams("quantidade")));
            
            // CORREÇÃO: Usando o formatador para aceitar o formato de input HTML
            produto.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao"), formatter));
            
            produto.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));

            produtoDAO.update(produto);
            return "Produto atualizado com sucesso!";
        } else {
            response.status(404);
            return "Produto não encontrado!";
        }
    }

    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);

        if (produto != null) {
            // CORREÇÃO: Passando o ID para o método remove do DAO
            produtoDAO.remove(id);
            return "Produto removido com sucesso!";
        } else {
            response.status(404);
            return "Produto não encontrado!";
        }
    }

    public Object getAll(Request request, Response response) {
        // Retorna todos os produtos, separados por uma quebra de linha HTML <br>
        return produtoDAO.getAll().stream()
                         .map(Produto::toString)
                         .collect(java.util.stream.Collectors.joining("<br>")); 
    }
}