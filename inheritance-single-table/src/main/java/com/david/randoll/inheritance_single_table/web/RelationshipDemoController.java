package com.david.randoll.inheritance_single_table.web;

import com.david.randoll.inheritance_single_table.db.*;
import com.david.randoll.inheritance_single_table.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class RelationshipDemoController {
    private final RelationshipRepository relationshipRepository;

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
        relationshipRepository.saveAll(List.of(task, project, user, org));

        return "Demo setup completed";
    }

    /**
     * GET endpoint: fetch all entities with their parent/child relationships
     */
    @GetMapping
    public List<Map<String, Object>> fetchAllRelationships() {
        List<Relationship> entities = relationshipRepository.findAll();

        return entities.stream()
                .map(this::toGraphMap)
                .toList();
    }

    private Map<String, Object> toGraphMap(Relationship entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("type", entity.getClass().getSimpleName());

        map.put("parents", entity.getParentRelationships().stream()
                .map(r -> Map.of("id", r.getId(), "type", r.getClass().getSimpleName()))
                .toList());

        map.put("children", entity.getChildRelationships().stream()
                .map(r -> Map.of("id", r.getId(), "type", r.getClass().getSimpleName()))
                .toList());

        return map;
    }
}
