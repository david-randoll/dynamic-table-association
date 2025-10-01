package com.david.randoll.inheritance_join_table.repository;

import com.david.randoll.inheritance_join_table.db.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}