package co.com.egonzalias.service;

import co.com.egonzalias.dto.RegisterProductDTO;
import co.com.egonzalias.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    void save(RegisterProductDTO productDTO);
    Optional<Products> findById(Long id);
    Page<Products> search(String name, String category, Pageable pageable);
    void deleteById(Long id);
}
