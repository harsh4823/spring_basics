package com.basics.spring_basics.Service;

import com.basics.spring_basics.Payload.ProductResponse;
import com.basics.spring_basics.Payload.ProductsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductsService {

    ProductsDTO createProduct(ProductsDTO products, Long id);

    ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse getProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductsDTO updateProduct(Long productId,ProductsDTO product);

    ProductsDTO deleteProduct(Long productId);

}
