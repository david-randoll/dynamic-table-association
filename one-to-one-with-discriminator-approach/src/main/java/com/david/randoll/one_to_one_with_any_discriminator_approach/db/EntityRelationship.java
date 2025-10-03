package com.david.randoll.one_to_one_with_any_discriminator_approach.db;

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
                @UniqueConstraint(columnNames = {EntityRelationship.RELATIONSHIP_PARENT_ID, EntityRelationship.RELATIONSHIP_CHILD_ID})
        },
        indexes = {
                @Index(name = "idx_entity_relationship_parent", columnList = EntityRelationship.RELATIONSHIP_PARENT_ID),
                @Index(name = "idx_entity_relationship_child", columnList = EntityRelationship.RELATIONSHIP_CHILD_ID)
        }
)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class EntityRelationship extends BaseEntity {
    public static final String RELATIONSHIP_PARENT_ID = "relationship_parent_id";
    public static final String RELATIONSHIP_CHILD_ID = "relationship_child_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = RELATIONSHIP_PARENT_ID, nullable = false, foreignKey = @ForeignKey(name = "fk_entity_relationship_parent"))
    private Relationship parent;

    @ManyToOne
    @JoinColumn(name = RELATIONSHIP_CHILD_ID, nullable = false, foreignKey = @ForeignKey(name = "fk_entity_relationship_child"))
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