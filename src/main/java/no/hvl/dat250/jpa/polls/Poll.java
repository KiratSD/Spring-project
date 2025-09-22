package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "polls")
public class Poll {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @ManyToOne(optional = false)
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoteOption> options = new LinkedHashSet<>();

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected Poll() {} // JPA

    public Poll(String question, User createdBy) {
        this.question = question;
        this.createdBy = createdBy;
    }


    public VoteOption addVoteOption(String caption) {
        VoteOption o = new VoteOption(this, caption, options.size());
        options.add(o);
        return o;
    }

    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public User getCreatedBy() { return createdBy; }
    public Set<VoteOption> getOptions() { return options; }
}
