package dao;

import java.io.*;
import java.util.*;
import model.Produto;

public class ProdutoDAO {
    private String filename;
    private List<Produto> produtos;

    public ProdutoDAO(String filename) throws IOException {
        this.filename = filename;
        File file = new File(filename);

        if (file.exists()) {
            produtos = load();
        } else {
            produtos = new ArrayList<>();
            save();
        }
    }

    // 🔹 Salvar lista de produtos no arquivo
    private void save() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(produtos);
        }
    }

    // 🔹 Carregar lista de produtos do arquivo
    @SuppressWarnings("unchecked")
    private List<Produto> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Produto>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // 🔹 Adicionar produto
    public void add(Produto produto) {
        produtos.add(produto);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Buscar por id
    public Produto get(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // 🔹 Atualizar produto
    public void update(Produto produto) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produto.getId()) {
                produtos.set(i, produto);
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // 🔹 Remover produto
    public void remove(int id) {
        produtos.removeIf(p -> p.getId() == id);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Listar todos
    public List<Produto> getAll() {
        return produtos;
    }

    // 🔹 Pegar maior id (pra incrementar no próximo)
    public int getMaxId() {
        int max = 0;
        for (Produto p : produtos) {
            if (p.getId() > max) {
                max = p.getId();
            }
        }
        return max;
    }
}
