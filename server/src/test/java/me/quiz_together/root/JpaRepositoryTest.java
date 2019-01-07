package me.quiz_together.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class JpaRepositoryTest {
    @Autowired
    protected TestEntityManager em;

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
