package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Organization extends Relationship {
    private String name;
}
