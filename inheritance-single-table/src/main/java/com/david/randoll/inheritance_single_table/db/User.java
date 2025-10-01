package com.david.randoll.inheritance_single_table.db;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("USER")
public class User extends Relationship {
    private String username;
    private String email;
}
