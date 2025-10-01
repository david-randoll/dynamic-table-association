package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public abstract class Relatable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "relationship_id", unique = true, nullable = true, foreignKey = @ForeignKey(name = "fk_entity_relationship"))
    @Delegate
    private Relationship relationship = new Relationship(this);

    public void addChildRelationship(@NonNull Relatable entity) {
        this.getRelationship().addChildRelationship(entity.getRelationship());
    }

    public void addParentRelationship(@NonNull Relatable entity) {
        this.getRelationship().addParentRelationship(entity.getRelationship());
    }

    public Long getId() {
        return id;
    }
}