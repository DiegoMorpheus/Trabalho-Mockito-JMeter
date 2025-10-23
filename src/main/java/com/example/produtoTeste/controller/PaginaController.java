package com.example.produtoTeste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.produtoTeste.model.Produto;
import com.example.produtoTeste.service.ProdutoService;

@Controller
public class PaginaController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/produtos-view")
    public String mostrarPagina(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        model.addAttribute("produtos", produtos);
        return "produtos"; // Nome do arquivo HTML em /templates/produtos.html
    }
}
