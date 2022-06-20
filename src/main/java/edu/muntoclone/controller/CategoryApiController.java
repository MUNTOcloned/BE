package edu.muntoclone.controller;

import edu.muntoclone.entity.Category;
import edu.muntoclone.service.CategoryService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    @GetMapping("/categories")
    public List<CategoryDetailResponse> findCategoryAll() {
        return categoryService.findAll()
                .stream()
                .map(c -> new CategoryDetailResponse(c.getId(), c.getName()))
                .collect(toList());
    }

    @Getter
    @NoArgsConstructor
    static class CategoryDetailResponse {
        private Long id;
        private String name;

        public CategoryDetailResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class CategoryAddRequest {

        @NotBlank
        private String name;
    }
}
