package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public abstract class Relationship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntityRelationship> childEntityRelationships = new ArrayList<>();

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntityRelationship> parentEntityRelationships = new ArrayList<>();


    /*
     * Relationship helpers
     */
    @NonNull
    public List<Relationship> getChildRelationships() {
        return childEntityRelationships.stream()
                .map(EntityRelationship::getChild)
                .toList();
    }

    @NonNull
    public List<Relationship> getParentRelationships() {
        return parentEntityRelationships.stream()
                .map(EntityRelationship::getParent)
                .toList();
    }

    public void addParentRelationship(@NonNull Relationship parentRelationship) {
        var entityRelationship = new EntityRelationship()
                .setParent(parentRelationship)
                .setChild(this);
        if (!getParentEntityRelationships().contains(entityRelationship)) {
            getParentEntityRelationships().add(entityRelationship);
        }
    }

    public void addChildRelationship(@NonNull Relationship childRelationship) {
        var entityRelationship = new EntityRelationship()
                .setParent(this)
                .setChild(childRelationship);
        if (!getChildEntityRelationships().contains(entityRelationship)) {
            getChildEntityRelationships().add(entityRelationship);
        }
    }
}
