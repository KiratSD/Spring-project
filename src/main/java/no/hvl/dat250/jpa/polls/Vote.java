package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User voter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private VoteOption votesOn;

    @ManyToOne(optional = false)
    private Poll poll;

    protected Vote() {} // JPA

    public Vote(User voter, VoteOption option) {
        this.voter = voter;
        this.votesOn = option;
        this.poll = option.getPoll();
    }

    public Long getId() { return id; }
    public User getVoter() { return voter; }
    public VoteOption getVotesOn() { return votesOn; }
    public Poll getPoll() { return poll; }
}
