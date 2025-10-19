package com.example.demo.messaging;

import com.example.demo.domain.PollManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class VoteEventListener {
    private final PollManager store;
    public VoteEventListener(PollManager store) { this.store = store; }

    @RabbitListener(queues = "#{votesQueue.name}")
    public void handle(VoteEvent e) {
        long pollId = e.getPollId();

        Long optionId = e.getOptionId();
        if (optionId == null && e.getOptionOrder() != null) {
            optionId = store.optionIdByOrder(pollId, e.getOptionOrder())
                    .orElseThrow(() -> new IllegalArgumentException("Option/order invalid"));
        }
        if (optionId == null) throw new IllegalArgumentException("Missing optionId/optionOrder");

        long userId = store.userIdByUsername(e.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + e.getUsername()));

        if (!store.optionBelongsToPoll(pollId, optionId))
            throw new IllegalArgumentException("Option not in poll");

        store.castOrChangeVote(pollId, userId, optionId);
    }
}
