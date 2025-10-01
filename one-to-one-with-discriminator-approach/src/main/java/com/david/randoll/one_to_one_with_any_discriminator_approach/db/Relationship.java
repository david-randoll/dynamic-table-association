package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminator;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Relationship extends BaseEntity {
    public static final String ENTITY_TYPE = "entity_type";
    public static final String ENTITY_ID = "entity_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntityRelationship> childEntityRelationships = new ArrayList<>();

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntityRelationship> parentEntityRelationships = new ArrayList<>();

    @Any
    @AnyDiscriminator(DiscriminatorType.STRING)
    @AnyKeyJavaClass(Long.class)
    @AnyDiscriminatorValue(discriminator = "ORG", entity = Organization.class)
    @AnyDiscriminatorValue(discriminator = "PROJECT", entity = Project.class)
    @AnyDiscriminatorValue(discriminator = "TASK", entity = Task.class)
    @AnyDiscriminatorValue(discriminator = "USER", entity = User.class)
    @JoinColumn(name = ENTITY_ID, nullable = false, foreignKey = @ForeignKey(name = "fk_relationship_owner"))
    @Column(name = ENTITY_TYPE, nullable = false)
    private Relatable owner;


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

    Relationship() {
        // JPA
    }

    public Relationship(Relatable owner) {
        this.owner = owner;
    }
}
