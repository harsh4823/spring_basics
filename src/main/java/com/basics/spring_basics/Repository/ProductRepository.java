package com.basics.spring_basics.Repository;

import com.basics.spring_basics.Model.CategoryModel;
import com.basics.spring_basics.Model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {

    Products findByproductName(String productName);

    Page<Products> findByproductNameLikeIgnoreCase(Pageable pageDetails, String keyword);

    Page<Products> findByCategoryOrderByPriceAsc(Pageable pageDetails, CategoryModel categoryId);
}
