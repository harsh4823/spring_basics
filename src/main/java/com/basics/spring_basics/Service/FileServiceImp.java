package com.basics.spring_basics.Service;

import com.basics.spring_basics.Exceptions.ResourceNotFoundException;
import com.basics.spring_basics.Model.Products;
import com.basics.spring_basics.Payload.ProductsDTO;
import com.basics.spring_basics.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImp implements FileService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductsDTO updateProductImage(Long productID, MultipartFile image) throws IOException {
        Products product = productRepository.findById(productID)
                .orElseThrow(()->new ResourceNotFoundException("Product","ProductID",productID));
        String fileName = uploadImage(path,image);
        product.setImage(fileName);
        return modelMapper.map(productRepository.save(product), ProductsDTO.class);
    }

    private String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFileName = image.getOriginalFilename();

        String randomID = UUID.randomUUID().toString();
        String fileName = randomID.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        
        File folder = new File(path);
        if (!folder.exists()){
            folder.mkdirs();
        }
        String filePath = path + File.separator + fileName;
        Files.copy(image.getInputStream(), Path.of(filePath));
        return fileName;
    }
}
