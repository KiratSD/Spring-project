package com.example.demo.domain;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PollManager {
    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();

    public Map<Long, Poll> getPolls() { return polls; }
    public Map<Long, User> getUsers() { return users; }
}
