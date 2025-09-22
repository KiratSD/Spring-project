package no.hvl.dat250.jpa.polls;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    // polls this user created
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Poll> created = new LinkedHashSet<>();

    // votes this user cast
    @OneToMany(mappedBy = "voter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected User() {} // JPA

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public Poll createPoll(String question) {
        Poll p = new Poll(question, this);
        created.add(p);
        return p;
    }


    public Vote voteFor(VoteOption option) {
        Vote v = new Vote(this, option);
        votes.add(v);
        option.addVote(v);
        return v;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Set<Poll> getCreated() { return created; }
}
