package com.david.randoll.one_to_one_with_any_discriminator_approach.web;

import com.david.randoll.one_to_one_with_any_discriminator_approach.db.*;
import com.david.randoll.one_to_one_with_any_discriminator_approach.repository.EntityRelationshipRepository;
import com.david.randoll.one_to_one_with_any_discriminator_approach.repository.RelatableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class RelationshipDemoController {
    private final RelatableRepository relatableRepository;
    private final EntityRelationshipRepository entityRelationshipRepository;

    @PostMapping
    @Transactional
    public String setupDemo() {
        Organization org = new Organization().setName("Acme Corp");
        User user = new User().setUsername("david").setEmail("david@example.com");
        Project project = new Project().setTitle("New Backend").setDescription("Spring Boot overhaul");
        Task task = new Task().setTitle("Implement dynamic associations");

        // Link relationships
        org.addChildRelationship(user);      // Org → User
        user.addChildRelationship(project);  // User → Project
        project.addChildRelationship(task);  // Project → Task

        // Save all (cascading will persist EntityRelationship)
        relatableRepository.saveAll(List.of(task, project, user, org));

        return "Demo setup completed";
    }

    /**
     * GET endpoint: fetch all entities with their parent/child relationships
     */
    @GetMapping
    public List<Map<String, Object>> fetchAllRelationships() {
        List<Relatable> entities = entityRelationshipRepository.findAll().stream()
                .flatMap(er -> Stream.of(er.getParent().getOwner(), er.getChild().getOwner()))
                .distinct()
                .toList();

        return entities.stream()
                .map(this::toGraphMap)
                .toList();
    }

    private Map<String, Object> toGraphMap(Relatable entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("type", entity.getClass().getSimpleName());

        map.put("parents", entity.getParentRelationships().stream()
                .map(Relationship::getOwner)
                .map(r -> Map.of("id", r.getId(), "type", r.getClass().getSimpleName()))
                .toList());

        map.put("children", entity.getChildRelationships().stream()
                .map(Relationship::getOwner)
                .map(r -> Map.of("id", r.getId(), "type", r.getClass().getSimpleName()))
                .toList());

        return map;
    }
}
