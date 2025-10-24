package co.com.egonzalias.service.impl;

import co.com.egonzalias.dto.RegisterProductDTO;
import co.com.egonzalias.entity.Products;
import co.com.egonzalias.repository.ProductRepository;
import co.com.egonzalias.service.impl.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ======================================
    // save product test
    // ======================================
    @Test
    void save_shouldSaveProduct() {
        RegisterProductDTO dto = new RegisterProductDTO();
        dto.setName("Laptop");
        dto.setDescription("Gaming Laptop");
        dto.setPrice(BigDecimal.valueOf(1500));
        dto.setCategory("Electronics");

        productService.save(dto);

        ArgumentCaptor<Products> captor = ArgumentCaptor.forClass(Products.class);
        verify(productRepository).save(captor.capture());

        Products saved = captor.getValue();
        assertEquals("Laptop", saved.getName());
        assertEquals("Gaming Laptop", saved.getDescription());
        assertEquals(BigDecimal.valueOf(1500), saved.getPrice());
        assertEquals("Electronics", saved.getCategory());
    }

    // ======================================
    // findById test
    // ======================================
    @Test
    void findById_shouldReturnProduct() {
        Products product = new Products();
        product.setId(1L);
        product.setName("Book");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Products> result = productService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Book", result.get().getName());
    }

    // ======================================
    // search test
    // ======================================
    @Test
    void search_shouldReturnPage() {
        Products p1 = new Products();
        p1.setName("Laptop");
        Products p2 = new Products();
        p2.setName("Laptop Pro");

        Page<Products> page = new PageImpl<>(List.of(p1, p2));
        when(productRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
                eq("Laptop"), eq("Electronics"), any(PageRequest.class)
        )).thenReturn(page);

        Page<Products> result = productService.search("Laptop", "Electronics", PageRequest.of(0, 10));

        assertEquals(2, result.getContent().size());
        assertEquals("Laptop", result.getContent().get(0).getName());
    }

    // ======================================
    // deleteById test
    // ======================================
    @Test
    void deleteById_shouldCallRepository() {
        productService.deleteById(5L);

        verify(productRepository, times(1)).deleteById(5L);
    }
}
