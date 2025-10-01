package com.david.randoll.any_discriminator_approach.repository;

import com.david.randoll.any_discriminator_approach.db.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}