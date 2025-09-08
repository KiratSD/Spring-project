package com.example.demo.web.dto;

import java.time.Instant;
import java.util.List;

public class PollDtos {
    public static class CreatePollRequest {
        public String question;
        public Instant publishedAt;
        public Instant validUntil;
        public List<String> options;
        public String createdBy;
        public CreatePollRequest() {}
    }

    public static class PollResponse {
        public long id;
        public String question;
        public Instant publishedAt;
        public Instant validUntil;
        public List<Option> options;
        public PollResponse(long id, String question, Instant publishedAt, Instant validUntil, List<Option> options) {
            this.id = id;
            this.question = question;
            this.publishedAt = publishedAt;
            this.validUntil = validUntil;
            this.options = options;
        }
        public static class Option {
            public long id;
            public String caption;
            public int presentationOrder;
            public Option(long id, String caption, int presentationOrder) {
                this.id = id;
                this.caption = caption;
                this.presentationOrder = presentationOrder;
            }
        }
    }
}
