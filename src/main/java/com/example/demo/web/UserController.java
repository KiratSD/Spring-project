package com.example.demo.web;

import com.example.demo.domain.PollManager;
import com.example.demo.domain.User;
import com.example.demo.web.dto.UserDtos.CreateUserRequest;
import com.example.demo.web.dto.UserDtos.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final PollManager store;
    public UserController(PollManager store) { this.store = store; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody CreateUserRequest req) {
        var u = new User();
        u.setUsername(req.username);
        u.setEmail(req.email);
        long id = store.createUser(u);
        return new UserResponse(id, u.getUsername(), u.getEmail());
    }

    @GetMapping
    public List<UserResponse> list() {
        return store.listUsers().stream()
                .map(e -> new UserResponse(e.getKey(), e.getValue().getUsername(), e.getValue().getEmail()))
                .toList();
    }
}
