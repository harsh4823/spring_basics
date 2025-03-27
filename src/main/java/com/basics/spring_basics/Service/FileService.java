package com.basics.spring_basics.Service;

import com.basics.spring_basics.Payload.ProductsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    ProductsDTO updateProductImage(Long productID, MultipartFile image) throws IOException;
}
