package com.david.randoll.one_to_one_with_any_discriminator_approach.repository;

import com.david.randoll.one_to_one_with_any_discriminator_approach.db.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
