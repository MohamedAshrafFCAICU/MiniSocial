package Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "Authenticaions")
public class Authentication extends _BaseEntity
{
    @Column(nullable = true , unique = true )
    private String token  = UUID.randomUUID().toString();

    // Navigational Properties:
    @OneToOne(mappedBy = "authentication")
    private User user;


    public  String getToken() {
        return token;
    }

    public void setToken( String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
