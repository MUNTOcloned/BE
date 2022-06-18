package edu.muntoclone.controller;

import edu.muntoclone.entity.Category;
import edu.muntoclone.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryApiController {

    private final CategoryService categoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    public String addCategory(@RequestBody @Valid CategoryAddRequest categoryAddRequest) {
        final Category category = Category.builder()
                .name(categoryAddRequest.getName())
                .build();

        categoryService.add(category);
        return String.valueOf(category.getId());
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class CategoryAddRequest {

        @NotBlank
        private String name;
    }
}
