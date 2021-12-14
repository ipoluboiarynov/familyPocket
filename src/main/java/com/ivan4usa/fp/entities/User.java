package com.ivan4usa.fp.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Class for User Object
 */
@Entity
@Table(name = "user", schema = "fp_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    /**
     * Constructor for the class
     * @param id of user
     * @param email of user account
     * @param password of user account
     * @param authorities value for user
     */
    public User(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * User id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User name
     */
    @Column(name = "name", length = 50)
    private String name;

    /**
     * User email
     */
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    /**
     * User password
     */
    @Column(name = "password", length = 50)
    private String password;

    /**
     * User role
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    /**
     * User authorities value
     */
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Equals method for the class
     * @param o object
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return email.equals(user.email);
    }

    /**
     * HashCode method for the class
     * @return number
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * Getter for the password field
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the email field
     * @return email
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Is account not expired method
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Is account not expired locked
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Is credentials not expired method
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Is enabled method
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
