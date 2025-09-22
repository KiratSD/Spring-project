package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "vote_options")
public class VoteOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String caption;

    @Column(name = "presentation_order")
    private int presentationOrder;

    @ManyToOne(optional = false)
    private Poll poll;

    @OneToMany(mappedBy = "votesOn", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected VoteOption() {} // JPA

    public VoteOption(Poll poll, String caption, int presentationOrder) {
        this.poll = poll;
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    void addVote(Vote v) { votes.add(v); }

    public Long getId() { return id; }
    public String getCaption() { return caption; }
    public int getPresentationOrder() { return presentationOrder; }
    public Poll getPoll() { return poll; }
}
