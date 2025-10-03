package com.david.randoll.inheritance_join_table.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Entity
@Table(
        name = "entity_relationship",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"relationship_parent_id", "relationship_child_id"})
        },
        indexes = {
                @Index(name = "idx_parent_child", columnList = "relationship_parent_id, relationship_child_id"),
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

    @ManyToOne
    @JoinColumn(name = "relationship_parent_id", nullable = false)
    private Relationship parent;

    @ManyToOne
    @JoinColumn(name = "relationship_child_id", nullable = false)
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
