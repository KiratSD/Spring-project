package com.example.demo.domain;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollManager {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> userIdByUsername = new HashMap<>();

    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, List<VoteOption>> optionsByPoll = new HashMap<>();

    private final Map<Long, Map<Long, VoteRecord>> votesByPoll = new HashMap<>();
    public static record VoteRecord(long optionId, Instant publishedAt) {}

    private final AtomicLong userSeq = new AtomicLong(1);
    private final AtomicLong pollSeq = new AtomicLong(1);

    public long createUser(User u) {
        if (userIdByUsername.containsKey(u.getUsername()))
            return userIdByUsername.get(u.getUsername());
        long id = userSeq.getAndIncrement();
        users.put(id, u);
        userIdByUsername.put(u.getUsername(), id);
        return id;
    }

    public List<Map.Entry<Long, User>> listUsers() {
        return new ArrayList<>(users.entrySet());
    }

    public Optional<Long> userIdByUsername(String username) {
        return Optional.ofNullable(userIdByUsername.get(username));
    }

    public long createPoll(Poll p, List<VoteOption> options) {
        long pollId = pollSeq.getAndIncrement();
        polls.put(pollId, p);
        for (int i = 0; i < options.size(); i++) {
            options.get(i).setPresentationOrder(i);
        }
        optionsByPoll.put(pollId, new ArrayList<>(options));
        votesByPoll.put(pollId, new HashMap<>());
        return pollId;
    }

    public List<Map.Entry<Long, Poll>> listPolls() {
        return new ArrayList<>(polls.entrySet());
    }

    public Optional<Poll> getPoll(long pollId) {
        return Optional.ofNullable(polls.get(pollId));
    }

    public List<Map.Entry<Long, VoteOption>> listOptions(long pollId) {
        List<VoteOption> list = optionsByPoll.getOrDefault(pollId, List.of());
        List<Map.Entry<Long, VoteOption>> out = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            out.add(Map.entry((long) (i + 1), list.get(i)));
        }
        return out;
    }

    public boolean optionBelongsToPoll(long pollId, long optionId) {
        var list = optionsByPoll.getOrDefault(pollId, List.of());
        return optionId >= 1 && optionId <= list.size();
    }

    public void deletePoll(long pollId) {
        polls.remove(pollId);
        optionsByPoll.remove(pollId);
        votesByPoll.remove(pollId);
    }

    public void castOrChangeVote(long pollId, long userId, long optionId) {
        var votesForPoll = votesByPoll.get(pollId);
        if (votesForPoll == null) return;
        votesForPoll.put(userId, new VoteRecord(optionId, Instant.now()));
    }

    public Optional<VoteRecord> voteOfUser(long pollId, long userId) {
        var votesForPoll = votesByPoll.getOrDefault(pollId, Map.of());
        return Optional.ofNullable(votesForPoll.get(userId));
    }

    public List<Map.Entry<Long, VoteRecord>> listVotes(long pollId) {
        return new ArrayList<>(votesByPoll.getOrDefault(pollId, Map.of()).entrySet());
    }
}
