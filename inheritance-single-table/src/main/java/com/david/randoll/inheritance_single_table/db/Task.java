package com.david.randoll.inheritance_single_table.db;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("TASK")
@Getter
@Setter
@NoArgsConstructor
public class Task extends Relationship {
    private String title;
    private boolean completed;
}