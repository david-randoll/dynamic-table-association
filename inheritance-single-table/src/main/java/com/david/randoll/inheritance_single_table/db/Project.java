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
@DiscriminatorValue("PROJECT")
public class Project extends Relationship {
    private String title;
    private String description;
}