package com.demo.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class UserDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid address")
        private String email;
    }

    @Data
    public static class UpdateRequest {
        private String name;

        @Email(message = "Email must be a valid address")
        private String email;
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String email;
        private String createdAt;
        private String updatedAt;
    }
}