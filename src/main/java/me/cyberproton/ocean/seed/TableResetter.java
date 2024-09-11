package me.cyberproton.ocean.seed;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("seeder")
@AllArgsConstructor
@Component
public class TableResetter {
    private final EntityManager entityManager;

    @Transactional
    public void reset() {
        entityManager
                .createNativeQuery("SELECT tablename FROM pg_tables WHERE schemaname='public'")
                .getResultList()
                .forEach(
                        tableName -> {
                            entityManager
                                    .createNativeQuery(
                                            "TRUNCATE TABLE \"" + tableName + "\" CASCADE")
                                    .executeUpdate();
                        });
    }
}
