package org.stand.springbootproject.entity.role;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    // @NaturalId
    @Column(name = "name")
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
