package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private Float price;
    private String thumbnail;

    @JsonProperty("category_id")
    private Long categoryId;
    private List<MultipartFile> files;
}
