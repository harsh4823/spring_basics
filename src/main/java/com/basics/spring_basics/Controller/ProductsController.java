package com.basics.spring_basics.Controller;

import com.basics.spring_basics.Config.ProductConst;
import com.basics.spring_basics.Payload.ProductResponse;
import com.basics.spring_basics.Payload.ProductsDTO;
import com.basics.spring_basics.Service.FileService;
import com.basics.spring_basics.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductsController {

    @Autowired
    private ProductsService service;

    @Autowired
    private FileService fileService;

    @PostMapping("/admin/categories/{category_id}/product")
    public ResponseEntity<ProductsDTO> addProducts(@RequestBody ProductsDTO products
            , @PathVariable Long category_id){
        return new ResponseEntity<>(service.createProduct(products,category_id),HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = ProductConst.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConst.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConst.SORT_CATEGORIES_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConst.SORT_ORDER,required = false) String sortOrder
    ){
        return new ResponseEntity<>(service.getAllProducts(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber",defaultValue = ProductConst.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConst.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConst.SORT_CATEGORIES_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConst.SORT_ORDER,required = false) String sortOrder
            ){
        return new ResponseEntity<>
                (service.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber",defaultValue = ProductConst.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConst.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConst.SORT_CATEGORIES_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConst.SORT_ORDER,required = false) String sortOrder
    ){
        return new ResponseEntity<>
                (service.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductsDTO> updateProduct(@PathVariable Long productId,@RequestBody ProductsDTO product){
        return new ResponseEntity<>(service.updateProduct(productId,product),HttpStatus.OK);
    }

    @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<ProductsDTO> deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(service.deleteProduct(productId),HttpStatus.OK);
    }

    @PutMapping("/products/{productID}/image")
    public ResponseEntity<ProductsDTO> updateProductImage(
            @PathVariable Long productID, @RequestParam("image")MultipartFile image
            ) throws IOException {
        return new ResponseEntity<>(fileService.updateProductImage(productID,image),HttpStatus.OK);
    }

}
