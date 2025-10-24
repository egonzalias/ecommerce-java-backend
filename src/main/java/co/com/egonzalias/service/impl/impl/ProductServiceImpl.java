package co.com.egonzalias.service.impl.impl;

import co.com.egonzalias.dto.RegisterProductDTO;
import co.com.egonzalias.entity.Products;
import co.com.egonzalias.repository.ProductRepository;
import co.com.egonzalias.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void save(RegisterProductDTO productDTO){
        Products product = new Products();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCategory(productDTO.getCategory());
        productRepository.save(product);
    }

    @Override
    public Optional<Products> findById(Long id){
        return productRepository.findById(id);
    }

    @Override
    public Page<Products> search(String name, String category, Pageable pageable){
        return productRepository.findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(name, category, pageable);
    }

    @Override
    public void deleteById(Long id){
        productRepository.deleteById(id);
    }
}
