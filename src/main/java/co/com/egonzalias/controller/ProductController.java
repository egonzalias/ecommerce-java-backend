package co.com.egonzalias.controller;

import co.com.egonzalias.dto.RegisterProductDTO;
import co.com.egonzalias.entity.Products;
import co.com.egonzalias.service.impl.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<Products> create(@Valid @RequestBody RegisterProductDTO product){
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Products> get(@PathVariable Long id){
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Products>> search(
            @RequestParam(defaultValue="") String name,
            @RequestParam(defaultValue="") String category,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(productService.search(name, category, PageRequest.of(page,size)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}