package com.example.demo.web.dto;

public class UserDtos {
    public static class CreateUserRequest {
        public String username;
        public String email;
        public CreateUserRequest() {}
    }
    public static class UserResponse {
        public long id;
        public String username;
        public String email;
        public UserResponse(long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
    }
}
