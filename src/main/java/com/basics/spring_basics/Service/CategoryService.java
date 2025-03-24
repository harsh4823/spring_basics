package com.basics.spring_basics.Service;

import com.basics.spring_basics.Payload.CategoryDTO;
import com.basics.spring_basics.Payload.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sort_By,String sort_Order);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long id);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);
}
