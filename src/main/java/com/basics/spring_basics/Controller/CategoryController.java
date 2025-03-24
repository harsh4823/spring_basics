package com.basics.spring_basics.Controller;

import com.basics.spring_basics.Config.AppConst;
import com.basics.spring_basics.Payload.CategoryDTO;
import com.basics.spring_basics.Payload.CategoryResponse;
import com.basics.spring_basics.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategoryList(
      @RequestParam(name = "pageNumber",defaultValue = AppConst.PAGE_NUMBER,required = false) Integer pageNumber,
      @RequestParam(name = "pageSize",defaultValue = AppConst.PAGE_SIZE,required = false) Integer pageSize,
      @RequestParam(name = "sortBy",defaultValue = AppConst.SORT_CATEGORIES_BY,required = false) String sortBy,
      @RequestParam(name = "sortOrder",defaultValue = AppConst.SORT_ORDER,required = false) String sortOrder
    ) {
        return new ResponseEntity<>
                (service.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.ACCEPTED);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> AddCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        CategoryDTO saved = service.createCategory(categoryDTO);
        return new ResponseEntity<>(saved,HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<CategoryDTO> DeleteCategory(@PathVariable Long id){
            CategoryDTO old =  service.deleteCategory(id);
            return new ResponseEntity<>(old,HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{id}")
     public ResponseEntity<CategoryDTO> UpdateCategory(@RequestBody @Valid CategoryDTO categoryDTO,@PathVariable Long id){
            CategoryDTO saved = service.updateCategory(categoryDTO,id);
            return new ResponseEntity<>(saved,HttpStatus.OK);
    }
}
