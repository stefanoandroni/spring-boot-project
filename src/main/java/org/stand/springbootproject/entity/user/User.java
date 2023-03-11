package org.stand.springbootproject.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.stand.springbootproject.entity.role.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @EqualsAndHashCode(callSuper = true)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_unique", columnNames = "username"),
                @UniqueConstraint(name = "email_unique", columnNames = "email")
        }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 48)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 48)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Size(max = 16)
    @Column(name = "username")
    private String username;

    @NotBlank
    // @NaturalId
    @Size(max = 48)
    @Email(message = "Email must be a valid email address")
    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles", // TODO: bad naming
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 256) // (?) length of encoded password
    @Column(name = "password")
    private String password;

    @Builder.Default
    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "enabled")
    private boolean enabled = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private VerificationToken verificationToken;

    public User(String firstName, String lastName, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: correct impl (?) (role.getName().name()))
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
        // return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
