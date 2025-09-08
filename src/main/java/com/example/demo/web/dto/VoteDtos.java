package com.example.demo.web.dto;

import java.time.Instant;

public class VoteDtos {
    public static class CastVoteRequest {
        public String username;
        public long optionId;
        public CastVoteRequest() {}
    }
    public static class VoteResponse {
        public long userId;
        public String username;
        public long optionId;
        public Instant publishedAt;
        public VoteResponse(long userId, String username, long optionId, Instant publishedAt) {
            this.userId = userId;
            this.username = username;
            this.optionId = optionId;
            this.publishedAt = publishedAt;
        }
    }
}
