package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminator;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;

import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "association_unique",
                columnNames = {"parent_type", "parent_id", "child_type", "child_id"}
        ),
        indexes = {
                @Index(name = "idx_entity_relationship_parent", columnList = "parent_type, parent_id"),
                @Index(name = "idx_entity_relationship_child", columnList = "child_type, child_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class EntityRelationship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Parent reference ----
    @Any
    @AnyDiscriminator(DiscriminatorType.STRING)
    @AnyKeyJavaClass(Long.class)
    @AnyDiscriminatorValue(discriminator = "ORG", entity = Organization.class)
    @AnyDiscriminatorValue(discriminator = "PROJECT", entity = Project.class)
    @AnyDiscriminatorValue(discriminator = "TASK", entity = Task.class)
    @AnyDiscriminatorValue(discriminator = "USER", entity = User.class)
    @Column(name = "parent_type", nullable = false, length = 50)
    @JoinColumn(name = "parent_id", nullable = false)
    private Relationship parent;

    // ---- Child reference ----
    @Any
    @AnyDiscriminator(DiscriminatorType.STRING)
    @AnyKeyJavaClass(Long.class)
    @AnyDiscriminatorValue(discriminator = "ORG", entity = Organization.class)
    @AnyDiscriminatorValue(discriminator = "PROJECT", entity = Project.class)
    @AnyDiscriminatorValue(discriminator = "TASK", entity = Task.class)
    @AnyDiscriminatorValue(discriminator = "USER", entity = User.class)
    @Column(name = "child_type", nullable = false, length = 50)
    @JoinColumn(name = "child_id", nullable = false)
    private Relationship child;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityRelationship that)) return false;
        return parent != null && child != null &&
               parent.getId() != null && parent.getId().equals(that.getParent().getId()) &&
               child.getId() != null && child.getId().equals(that.getChild().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent != null ? parent.getId() : null,
                child != null ? child.getId() : null);
    }
}
