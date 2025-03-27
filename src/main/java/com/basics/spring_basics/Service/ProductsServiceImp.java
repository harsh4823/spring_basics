package com.basics.spring_basics.Service;

import com.basics.spring_basics.Exceptions.APIExceptions;
import com.basics.spring_basics.Exceptions.ResourceNotFoundException;
import com.basics.spring_basics.Model.CategoryModel;
import com.basics.spring_basics.Model.Products;
import com.basics.spring_basics.Payload.ProductResponse;
import com.basics.spring_basics.Payload.ProductsDTO;
import com.basics.spring_basics.Repository.CategoryRepository;
import com.basics.spring_basics.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsServiceImp implements ProductsService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductsDTO createProduct(ProductsDTO productsDTO, Long id) {
        CategoryModel category = categoryRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",id));
        Products products = modelMapper.map(productsDTO, Products.class);
        Products saved = productRepository.findByproductName(products.getProductName());
        if(saved!=null){
            throw new APIExceptions("This Product Already exist");
        }

        products.setCategory(category);
        products.setImage("default.png");
        double price = products.getPrice();
        double discount = products.getDiscount();
        double specialPrice = price - (discount/100)*price;
        products.setSpecialPrice(specialPrice);
        productRepository.save(products);
        return modelMapper.map(products, ProductsDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Products> productsPage = productRepository.findAll(pageDetails);
        List<Products> products = productsPage.getContent();
        if(products.isEmpty()){
            throw new APIExceptions("there are no products");
        }
        List<ProductsDTO> productsDTOs = products.stream()
                .map(product ->modelMapper.map(product, ProductsDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setTotalItems(productsPage.getTotalElements());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Products> productsPage = productRepository.findByCategoryOrderByPriceAsc(pageDetails,category);
        List<Products> products = productsPage.getContent();
        if (products.isEmpty()){
            throw new APIExceptions("This category does not contain any products");
        }
        List<ProductsDTO> productsDTOs = products.stream()
                .map(product ->modelMapper.map(product, ProductsDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setTotalItems(productsPage.getTotalElements());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Products> productsPage = productRepository.findByproductNameLikeIgnoreCase(pageDetails,'%'+keyword+'%');
        List<Products> products = productsPage.getContent();
        if (products.isEmpty()){
            throw new APIExceptions("No Product with the keyword "+keyword);
        }
        List<ProductsDTO> productsDTOs = products.stream()
                .map(product ->modelMapper.map(product, ProductsDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productsDTOs);
        productResponse.setContent(productsDTOs);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setTotalItems(productsPage.getTotalElements());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }

    @Override
    public ProductsDTO updateProduct(Long productId,ProductsDTO productsDTO) {
        Products product1 = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product","productId",productId));
        Products product = modelMapper.map(productsDTO, Products.class);
        Products saved = productRepository.findByproductName(product.getProductName());
        if (saved!=null){
            throw new APIExceptions("this product already exists");
        }
        product1.setProductName(product.getProductName());
        product1.setDescription(product.getDescription());
        product1.setQuantity(product.getQuantity());
        product1.setPrice(product.getPrice());
        product1.setDiscount(product.getDiscount());
        double price = product.getPrice();
        double discount = product.getDiscount();
        double specialPrice = price - (discount/100)*price;
        product1.setSpecialPrice(specialPrice);
        productRepository.save(product1);
        return modelMapper.map(product1, ProductsDTO.class);
    }

    @Override
    public ProductsDTO deleteProduct(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","ProductID",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductsDTO.class);
    }
}
