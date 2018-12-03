package me.quiz_together.root.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.model.JpaModel;

@Slf4j
@DataJpaTest
class JpaModelRepositoryTest {

    @Autowired
    private JpaModelRepository jpaModelRepository;

    @Test
    void insertJpaTest() {
        JpaModel jpaModel = new JpaModel();
        jpaModel.setName("woojin");
        jpaModelRepository.save(jpaModel);
        Optional<JpaModel> byId = jpaModelRepository.findById(jpaModel.getId());
        log.debug("{}", byId.get().toString());
        assertThat(byId.get().getName()).isEqualTo("woojin");
    }
}