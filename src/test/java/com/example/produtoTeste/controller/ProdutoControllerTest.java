package com.example.produtoTeste.controller;

import com.example.produtoTeste.model.Produto;
import com.example.produtoTeste.service.ProdutoService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

@MockitoBean
private ProdutoService service;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveListarProdutos() throws Exception {
        Produto p1 = new Produto(1L, "Teclado", "Mecânico", new BigDecimal("250.00"), 10);
        Produto p2 = new Produto(2L, "Mouse", "Óptico", new BigDecimal("120.00"), 20);

        Mockito.when(service.listarTodos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Teclado"));
    }

    @Test
    public void deveCriarProduto() throws Exception {
        Produto produto = new Produto(null, "Monitor", "Full HD", new BigDecimal("800.00"), 5);
        Produto salvo = new Produto(3L, "Monitor", "Full HD", new BigDecimal("800.00"), 5);

        Mockito.when(service.salvar(Mockito.any())).thenReturn(salvo);

        mockMvc.perform(post("/api/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("Monitor"));
    }
}