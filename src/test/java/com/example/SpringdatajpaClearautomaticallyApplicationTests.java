package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SpringdatajpaClearautomaticallyApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(SpringdatajpaClearautomaticallyApplicationTests.class);

    @Autowired
    private MemberRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void init() {
        repository.save(Member.of("foo"));
        repository.save(Member.of("bar"));
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void bulkUpdateAndClear() {
        final Member foo = repository.findById(1L).get();
        logger.info("조회된 객체: {}", foo);
        repository.bulkUpdateAndClear("empty");
        logger.info("bulkUpdate 후 객체(clearAutomatically=false): {}", foo);
        logger.info("영속성 컨텍스트에 존재여부: {}", entityManager.contains(foo));
        assertThat(foo.getName()).isEqualTo("foo");
        assertThat(entityManager.contains(foo)).isFalse();
    }

    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void bulkUpdate() {
        final Member foo = repository.findById(1L).get();
        logger.info("조회된 객체: {}", foo);
        repository.bulkUpdate("empty");
        logger.info("bulkUpdate 후 객체(clearAutomatically=true): {}", foo);
        logger.info("영속성 컨텍스트에 존재여부: {}", entityManager.contains(foo));
        assertThat(foo.getName()).isEqualTo("foo");
        assertThat(entityManager.contains(foo)).isTrue();
    }

    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void bulkUpdateAndClearAndFind() {
        final Member foo = repository.findById(1L).get();
        logger.info("조회된 객체: {}", foo);
        repository.bulkUpdateAndClear("empty");
        final Member notChangedFoo = repository.findById(1L).get();
        logger.info("bulkUpdate 후 find (clearAutomatically=true): {}", notChangedFoo);
        assertThat(notChangedFoo.getName()).isEqualTo("empty");
    }

    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void bulkUpdateAndFind() {
        final Member foo = repository.findById(1L).get();
        logger.info("조회된 객체: {}", foo);
        repository.bulkUpdate("empty");
        final Member changedFoo = repository.findById(1L).get();
        logger.info("bulkUpdate 후 find (clearAutomatically=false): {}", changedFoo);
        assertThat(changedFoo.getName()).isEqualTo("foo");
    }
}
