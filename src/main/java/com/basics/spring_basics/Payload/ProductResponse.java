package com.basics.spring_basics.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private List<ProductsDTO> content;

    private Integer pageNumber;
    private Integer pageSize;
    private long totalItems;
    private Integer totalPages;
    private boolean lastPage;
}
