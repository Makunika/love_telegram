package ru.pshiblo.love.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pshiblo.love.domain.LoveMessage;

@Repository
public interface LoveMessageRepository extends JpaRepository<LoveMessage, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM messages ORDER BY random() LIMIT 1")
    LoveMessage getRandomMsg();
}