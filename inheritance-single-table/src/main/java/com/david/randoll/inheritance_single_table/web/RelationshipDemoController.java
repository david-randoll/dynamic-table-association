package com.david.randoll.inheritance_single_table.web;

import com.david.randoll.inheritance_single_table.db.*;
import com.david.randoll.inheritance_single_table.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class RelationshipDemoController {
    private final RelationshipRepository relationshipRepository;
    private final Random random = new Random();
    private final Faker faker = new Faker();

    @PostMapping("/bulk-setup")
    @Transactional
    public String bulkSetupDemo() {
        int total = 10_000;

        List<Organization> orgs = new ArrayList<>(total);
        List<User> users = new ArrayList<>(total);
        List<Project> projects = new ArrayList<>(total);
        List<Task> tasks = new ArrayList<>(total);

        // Generate Orgs
        for (int i = 0; i < total; i++) {
            orgs.add(new Organization().setName(faker.company().name()));
        }

        // Generate Users
        for (int i = 0; i < total; i++) {
            users.add(new User()
                    .setUsername(faker.credentials().username())
                    .setEmail(faker.internet().emailAddress()));
        }

        // Generate Projects
        for (int i = 0; i < total; i++) {
            projects.add(new Project()
                    .setTitle(faker.app().name())
                    .setDescription(faker.lorem().sentence()));
        }

        // Generate Tasks
        for (int i = 0; i < total; i++) {
            tasks.add(new Task().setTitle(faker.job().title()));
        }

        // Randomly assign relationships
        for (User user : users) {
            Organization parentOrg = orgs.get(random.nextInt(orgs.size()));
            parentOrg.addChildRelationship(user);
        }
        for (Project project : projects) {
            User parentUser = users.get(random.nextInt(users.size()));
            parentUser.addChildRelationship(project);
        }
        for (Task task : tasks) {
            Project parentProject = projects.get(random.nextInt(projects.size()));
            parentProject.addChildRelationship(task);
        }

        // Save all — cascade persists relationships
        relationshipRepository.saveAll(tasks);
        relationshipRepository.saveAll(projects);
        relationshipRepository.saveAll(users);
        relationshipRepository.saveAll(orgs);

        return "Inserted " + total + " entities with relationships.";
    }

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
