package com.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m set m.name = :name")
    void bulkUpdateAndClear(@Param("name") String name);

    @Modifying
    @Query("UPDATE Member m set m.name = :name")
    void bulkUpdate(@Param("name") String name);
}
