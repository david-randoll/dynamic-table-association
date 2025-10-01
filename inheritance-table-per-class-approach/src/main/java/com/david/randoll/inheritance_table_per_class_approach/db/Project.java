package com.david.randoll.inheritance_table_per_class_approach.db;

import jakarta.persistence.DiscriminatorValue;
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
@DiscriminatorValue("PROJECT")
public class Project extends Relationship {
    private String title;
    private String description;
}