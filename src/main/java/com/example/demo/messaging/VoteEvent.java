package com.example.demo.messaging;

public class VoteEvent {
    private Long pollId;
    private Long optionId;
    private Integer optionOrder;
    private String username;

    public VoteEvent() {}
    public VoteEvent(Long pollId, Long optionId, Integer optionOrder, String username) {
        this.pollId = pollId; this.optionId = optionId; this.optionOrder = optionOrder; this.username = username;
    }
    public Long getPollId() { return pollId; }
    public void setPollId(Long v) { this.pollId = v; }
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long v) { this.optionId = v; }
    public Integer getOptionOrder() { return optionOrder; }
    public void setOptionOrder(Integer v) { this.optionOrder = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }
}
