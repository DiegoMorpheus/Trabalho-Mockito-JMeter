package com.example.produtoTeste.service;

import com.example.produtoTeste.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Minimal repository interface used by the tests so Mockito can mock its behavior
 * (placed in the same test package to avoid requiring the main repository class).
 */
interface ProdutoRepository {
    List<Produto> findAll();
    Optional<Produto> findById(Long id);
    Produto save(Produto produto);
    void deleteById(Long id);
}

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Teclado");
        produto.setDescricao("Mecânico RGB");
        produto.setPreco(BigDecimal.valueOf(250.00)); 

        produto.setQuantidade(10);
    }

    @Test
    void deveListarTodosOsProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<Produto> resultado = produtoService.listarTodos();

        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarProdutoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNome());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoService.buscarPorId(99L);
        });

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveSalvarProduto() {
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoService.salvar(produto);

        assertEquals("Teclado", resultado.getNome());
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveAtualizarProduto() {
        Produto atualizado = new Produto();
        atualizado.setNome("Teclado Gamer");
        atualizado.setDescricao("Switch Azul");
        produto.setPreco(BigDecimal.valueOf(300.00)); 

        atualizado.setQuantidade(50);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenReturn(atualizado);

        Produto resultado = produtoService.atualizar(1L, atualizado);

        assertEquals("Teclado Gamer", resultado.getNome());
        assertEquals(50, resultado.getQuantidade());
        verify(produtoRepository).save(any());
    }

    @Test
    void deveDeletarProduto() {
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.deletar(1L);

        verify(produtoRepository).deleteById(1L);
    }
}
