package com.basics.spring_basics.Repository;

import com.basics.spring_basics.Model.CategoryModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel,Long> {

    CategoryModel findByCategoryName(@NotBlank(message = "name cant be blank") @Size(min = 5,message = "name should contain at least 5 letters") String categoryName);
}
