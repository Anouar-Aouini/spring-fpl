package com.fivepoints.spring.payload.requests;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class BookRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String author;
    @NotBlank
    private String content;
}
