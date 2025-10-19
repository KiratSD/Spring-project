package com.example.demo.web;

import com.example.demo.domain.*;
import com.example.demo.web.dto.PollDtos.CreatePollRequest;
import com.example.demo.web.dto.PollDtos.PollResponse;
import com.example.demo.web.dto.VoteDtos.CastVoteRequest;
import com.example.demo.web.dto.VoteDtos.VoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static com.example.demo.messaging.RabbitConfig.POLLS_EXCHANGE;
import com.example.demo.messaging.VoteEvent;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollManager store;
    private final RabbitTemplate rabbitTemplate;

    public PollController(PollManager store, RabbitTemplate rabbitTemplate) {
        this.store = store;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponse create(@RequestBody CreatePollRequest req) {
        var p = new Poll();
        p.setQuestion(req.question);
        p.setPublishedAt(req.publishedAt);
        p.setValidUntil(req.validUntil);

        var opts = new ArrayList<VoteOption>();
        for (int i = 0; i < req.options.size(); i++) {
            var vo = new VoteOption();
            vo.setCaption(req.options.get(i));
            vo.setPresentationOrder(i);
            opts.add(vo);
        }
        long id = store.createPoll(p, opts);
        return get(id);
    }

    @GetMapping
    public List<PollResponse> list() {
        return store.listPolls().stream()
                .map(e -> buildPollResponse(e.getKey(), e.getValue()))
                .toList();
    }

    @GetMapping("/{id}")
    public PollResponse get(@PathVariable long id) {
        var p = store.getPoll(id).orElseThrow();
        return buildPollResponse(id, p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        store.deletePoll(id);
    }

    @PostMapping("/{pollId}/votes")
    public ResponseEntity<Void> vote(@PathVariable long pollId, @RequestBody CastVoteRequest req) {
        rabbitTemplate.convertAndSend(
                POLLS_EXCHANGE,
                "poll." + pollId + ".vote",
                new VoteEvent(
                        pollId,
                         req.optionId,
                         null,
                         req.username
                )
        );
        return ResponseEntity.accepted().build();
    }


    @GetMapping("/{pollId}/votes")
    public List<VoteResponse> listVotes(@PathVariable long pollId,
                                        @RequestParam(required = false) String username) {
        if (username != null && !username.isBlank()) {
            return store.userIdByUsername(username)
                    .flatMap(id -> store.voteOfUser(pollId, id)
                            .map(v -> new VoteResponse(id, username, v.optionId(), v.publishedAt())))
                    .map(List::of)
                    .orElse(List.of());
        }
        return store.listVotes(pollId).stream()
                .map(e -> new VoteResponse(e.getKey(), "(user)", e.getValue().optionId(), e.getValue().publishedAt()))
                .toList();
    }

    private PollResponse buildPollResponse(long id, Poll p) {
        var opts = store.listOptions(id);
        var dtoOpts = opts.stream()
                .map(o -> new PollResponse.Option(o.getKey(), o.getValue().getCaption(), o.getValue().getPresentationOrder()))
                .toList();
        return new PollResponse(id, p.getQuestion(), p.getPublishedAt(), p.getValidUntil(), dtoOpts);
    }
}
