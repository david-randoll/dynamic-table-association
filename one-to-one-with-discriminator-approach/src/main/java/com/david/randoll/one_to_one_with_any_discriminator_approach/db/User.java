package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "user_")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class User extends Relationship {
    private String username;
    private String email;
}
