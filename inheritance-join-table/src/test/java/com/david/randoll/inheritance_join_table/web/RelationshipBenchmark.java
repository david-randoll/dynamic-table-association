package com.david.randoll.inheritance_join_table.web;

import com.david.randoll.inheritance_join_table.db.Organization;
import com.david.randoll.inheritance_join_table.db.Project;
import com.david.randoll.inheritance_join_table.db.Task;
import com.david.randoll.inheritance_join_table.db.User;
import com.david.randoll.inheritance_join_table.repository.RelationshipRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class RelationshipBenchmark {
    private static RelationshipRepository relationshipRepository;

    @Autowired
    void setRelationshipRepository(RelationshipRepository relationshipRepository) {
        RelationshipBenchmark.relationshipRepository = relationshipRepository;
    }

    private Random random = new Random();
    private Faker faker = new Faker();

    private int insertIndex = 0;

    private final int TOTAL = 200_000;

    /**
     * Preload 10k records with random relationships into the database.
     */
    @Setup(Level.Trial)
    @Transactional
    public void prepareData() {
        List<Organization> orgs = new ArrayList<>(TOTAL);
        List<User> users = new ArrayList<>(TOTAL);
        List<Project> projects = new ArrayList<>(TOTAL);
        List<Task> tasks = new ArrayList<>(TOTAL);

        // Generate entities
        for (int i = 0; i < TOTAL; i++)
            orgs.add(new Organization().setName(faker.company().name()));
        for (int i = 0; i < TOTAL; i++)
            users.add(new User().setUsername(faker.credentials().username()).setEmail(faker.internet().emailAddress()));
        for (int i = 0; i < TOTAL; i++)
            projects.add(new Project().setTitle(faker.app().name()).setDescription(faker.lorem().sentence()));
        for (int i = 0; i < TOTAL; i++) tasks.add(new Task().setTitle(faker.job().title()));

        // Randomly assign parent-child relationships
        for (User user : users) orgs.get(random.nextInt(orgs.size())).addChildRelationship(user);
        for (Project project : projects) users.get(random.nextInt(users.size())).addChildRelationship(project);
        for (Task task : tasks) projects.get(random.nextInt(projects.size())).addChildRelationship(task);

        // Save all — cascade persists relationships
        relationshipRepository.saveAll(tasks);
        relationshipRepository.saveAll(projects);
        relationshipRepository.saveAll(users);
        relationshipRepository.saveAll(orgs);

        insertIndex = TOTAL; // all initial records inserted
    }

    /**
     * Benchmark: fetch a random Organization from the DB
     */
    @Benchmark
    @Transactional
    public Organization fetchRandomOrg() {
        long randomId = 1 + random.nextInt(insertIndex);
        return relationshipRepository.findById(randomId)
                .filter(o -> o instanceof Organization)
                .map(o -> (Organization) o)
                .orElse(null);
    }

    /**
     * Benchmark: insert a new Organization
     */
    @Benchmark
    @Transactional
    public void insertNewOrg() {
        Organization org = new Organization().setName(faker.company().name());
        relationshipRepository.save(org);
        insertIndex++;
    }

    private static final Integer MEASUREMENT_ITERATIONS = 3;
    private static final Integer WARMUP_ITERATIONS = 3;

    @Test
    void executeJmhRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RelationshipBenchmark.class.getSimpleName())
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .forks(0)
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();

        new Runner(opt).run();
    }
}