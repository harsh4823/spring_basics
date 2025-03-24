package com.basics.spring_basics.Service;

import com.basics.spring_basics.Exceptions.APIExceptions;
import com.basics.spring_basics.Exceptions.ResourceNotFoundException;
import com.basics.spring_basics.Model.CategoryModel;
import com.basics.spring_basics.Payload.CategoryDTO;
import com.basics.spring_basics.Payload.CategoryResponse;
import com.basics.spring_basics.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sort_By,String sort_Order) {
        Sort sortByAndOrder = sort_Order.equalsIgnoreCase("asc")
                ? Sort.by(sort_By).ascending()
                :Sort.by(sort_By).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<CategoryModel> categoryPage = categoryRepository.findAll(pageDetails);
        List<CategoryModel> categories = categoryPage.getContent();
        if (categories.isEmpty()){
            throw new APIExceptions("Database is Empty");
        }
        List<CategoryDTO> categoryDTOS = categories.stream().
                map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalItems(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        CategoryModel category = modelMapper.map(categoryDTO, CategoryModel.class);
        CategoryModel saved = categoryRepository.findByCategoryName(category.getCategoryName());
        if(saved!=null){
            throw new APIExceptions("Category With The Name : '"+category.getCategoryName()+"' already exists");
        }
        CategoryModel savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        CategoryModel old = categoryRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Category","categoryId",id));
        categoryRepository.delete(old);
        return modelMapper.map(old, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
        CategoryModel category = modelMapper.map(categoryDTO, CategoryModel.class);
        CategoryModel saved = categoryRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("category","categoryName",id));
        CategoryModel check = categoryRepository.findByCategoryName(category.getCategoryName());
        if(check!=null){
            throw new APIExceptions("Category With The Name : '"+category.getCategoryName()+"' already exists");
        }
        saved.setCategoryName(category.getCategoryName());
        CategoryModel savedDB = categoryRepository.save(saved);
        return modelMapper.map(savedDB, CategoryDTO.class);
    }
}
