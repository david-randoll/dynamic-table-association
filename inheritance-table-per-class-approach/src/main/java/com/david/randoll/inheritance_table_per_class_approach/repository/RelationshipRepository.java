package com.david.randoll.inheritance_table_per_class_approach.repository;

import com.david.randoll.inheritance_table_per_class_approach.db.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}