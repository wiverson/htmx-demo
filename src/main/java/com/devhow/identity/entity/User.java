package com.devhow.identity.entity;

import com.devhow.identity.user.TimeUtil;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "users")
@Table(name = "users")
public class User {

    private String password;
    private String username;
    private Long version;
    private Long id;
    private boolean test;

    private Timestamp tokenValidation;

    private Timestamp creation;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, boolean test) {
        this.username = username;
        this.password = password;
        this.test = test;
    }

    public User(boolean test) {
        this.test = test;
    }

    static public Long id(Authentication authentication) {
        return Long.parseLong(authentication.getAuthorities().toArray()[0].toString());
    }

    public org.springframework.security.core.userdetails.User securityUser() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(id.toString()));
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(this.getUsername(), this.getPassword(), grantedAuthorities);
    }

    @Column(name = "pass")
    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        username = user;
    }

    public boolean validated() {
        return tokenValidation != null;
    }

    @Version
    @Column(name = "entity_version", nullable = false)
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTokenValidation() {
        return tokenValidation;
    }

    public void setTokenValidation(Timestamp tokenValidation) {
        this.tokenValidation = tokenValidation;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void markTokenAsValid() {
        TimeUtil time = new TimeUtil();
        setTokenValidation(time.now());
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", version=" + version +
                ", id=" + id +
                ", test=" + test +
                ", tokenValidation=" + tokenValidation +
                ", creation=" + creation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return test == user.test && Objects.equals(password, user.password) && Objects.equals(username, user.username) && Objects.equals(version, user.version) && Objects.equals(id, user.id) && Objects.equals(tokenValidation, user.tokenValidation) && Objects.equals(creation, user.creation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username, version, id, test, tokenValidation, creation);
    }

    @CreationTimestamp
    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }
}
