# Inheritance & Polymorphic Association Strategies with Hibernate

This repository demonstrates five approaches for modeling inheritance and polymorphic associations in relational
databases using JPA/Hibernate:

1. **Single Table Inheritance**
2. **Join Table (Joined) Inheritance**
3. **Table Per Class Inheritance**
4. **Any Discriminator Approach**
5. **One-to-One with Any Discriminator Approach**

## Comparison Table

| Approach                           | Flexibility | Referential Integrity | Query Performance | Schema Simplicity | Best Use Case                                        |
|------------------------------------|-------------|-----------------------|-------------------|-------------------|------------------------------------------------------|
| **Single Table Inheritance**       | Low         | Yes                   | Fast (no joins)   | Simple            | Small hierarchies with many shared fields            |
| **Join Table Inheritance**         | Medium      | Yes                   | Slower (joins)    | Moderate          | Complex hierarchies with distinct fields             |
| **Table Per Class**                | Low         | No                    | Slower (UNION)    | Fragmented        | Independent subclasses, rarely queried together      |
| **Any Discriminator**              | High        | No (app-level only)   | Moderate          | Simple generic    | Generic relationships, activity feeds                |
| **One-to-One + Any Discriminator** | Medium      | Yes                   | Moderate          | Simple            | Polymorphic one-to-one links with enforced integrity |

---

## 1. Single Table Inheritance

All entities in a hierarchy are stored in a **single table**, distinguished by a discriminator column.

### Key Points

- **Single Table**: One table holds all subclass data.
- **Discriminator Column**: Identifies which subclass a row represents.
- **Nullable Columns**: Non-shared attributes appear as nullable for unrelated types.

### Pros

- Simplifies schema (one table only).
- Easy to query across hierarchy.
- Good performance for small/medium hierarchies.

### Cons

- Lots of null columns if subclasses differ heavily.
- Schema can become “wide” and messy.
- Harder to evolve when subclasses diverge a lot.

### ERD Diagram

![img.png](images/img.png)

entity_relationship table:

![img_12.png](images/img_12.png)

relationship table:

![img_1.png](images/img_1.png)

---

## 2. Join Table (Joined) Inheritance

Each subclass gets its own table. The base class table holds shared attributes, and subclass tables hold specific ones.

### Key Points

- **Joined Tables**: Queries reconstruct full entities with joins.
- **No Null Columns**: Each table has only its relevant attributes.

### Pros

- Clean separation of subclass fields.
- No wasted space.
- Referential integrity is preserved between base and subclass.

### Cons

- Requires joins for polymorphic queries.
- More complex queries (can impact performance).

### ERD Diagram

![img_2.png](images/img_2.png)

entity_relationship table:

![img_10.png](images/img_10.png)

relationship table:

![img_11.png](images/img_11.png)

---

## 3. Table Per Class Inheritance

Each concrete class has its own full table, including inherited fields. No shared base table.

### Key Points

- **Independent Tables**: No foreign keys between subclass tables.
- **Polymorphic Queries**: Hibernate generates `UNION` across tables.

### Pros

- Each table is self-contained.
- Good if subclasses are rarely queried together.

### Cons

- No referential integrity at the DB level for the hierarchy.
- Polymorphic queries can be slow (use UNION).
- Duplicate schema between tables.

### ERD Diagram

![img_3.png](images/img_3.png)

entity_relationship table:

![img_9.png](images/img_9.png)

There is no relationship table in this approach.

---

## 4. Any Discriminator Approach

A **generic relationship table** stores a `type` and `id` pair to reference arbitrary entities. Uses Hibernate’s
`@Any` / `@AnyDiscriminator`.

### Key Points

- **Flexible**: Can link to any entity type dynamically.
- **No Foreign Keys**: Database does not enforce integrity. Hibernate resolves references.

### Pros

- Maximum flexibility for polymorphic relationships.
- Reduces need for multiple relationship tables.
- Useful for activity feeds, audit logs, metadata, etc.

### Cons

- No DB-level referential integrity (orphaned references possible).
- Harder to enforce constraints.
- Queries require extra care (joins by type/id).

### ERD Diagram

![img_4.png](images/img_4.png)

entity_relationship table:

![img_8.png](images/img_8.png)

There is no relationship table in this approach.

---

## 5. One-to-One with Any Discriminator Approach

A refinement of the Any Discriminator pattern, but applied in a **one-to-one** relationship context.

### Key Points

- **One-to-One Polymorphism**: Each row in an entity (e.g. `User`, `Project`, `Task`, `Organization`) links to exactly
  one row in the `Relationship` table.
- **Discriminator Column**: The `Relationship` table holds a discriminator (`entity_type`) and target key (`entity_id`)
  to resolve back to the owner.
- **Referential Integrity**: Unlike the pure Any Discriminator approach, this design **does enforce DB-level integrity**
  via `relationship_id` foreign keys from entity tables into the `Relationship` table.

### Pros

- Retains flexibility of `@Any` while gaining stronger integrity guarantees.
- DB-level referential integrity is enforced (`relationship_id` is a proper FK).
- Clear one-to-one semantics: each entity has exactly one `Relationship`.

### Cons

- More rigid than the pure Any approach (not many-to-any).
- Slightly more tables and joins involved compared to simpler inheritance.

### ERD Diagram

![img_5.png](images/img_5.png)

entity_relationship table:

![img_6.png](images/img_6.png)
relationship table:

![img_7.png](images/img_7.png)

---

# Recommendation

**Primary:** Use **One-to-One with Any Discriminator**.  
It gives you both flexibility (add new entity types without schema changes) and safety (DB-level referential integrity
via the `Relationship` table). Think of it like join-table inheritance but cleaner.

**Secondary:** Use **Any Discriminator Only** if you want a lighter schema and faster iteration.  
It’s simpler, but you lose DB-enforced integrity and must rely on Hibernate to manage references.
