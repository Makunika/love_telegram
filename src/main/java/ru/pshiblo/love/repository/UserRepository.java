package ru.pshiblo.love.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.pshiblo.love.domain.User;
import ru.pshiblo.love.domain.enums.State;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTelegramId(Long id);
    List<User> getAllByState(State state);
}