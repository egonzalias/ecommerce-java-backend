package co.com.egonzalias.repository;

import co.com.egonzalias.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Page<Products> findByNameContainingIgnoreCaseAndCategoryContainingIgnoreCase(
            String name, String category, Pageable pageable);
}