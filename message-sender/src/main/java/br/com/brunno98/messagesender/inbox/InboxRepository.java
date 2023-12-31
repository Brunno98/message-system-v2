package br.com.brunno98.messagesender.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InboxRepository extends JpaRepository<Inbox, Long> {
    Optional<Inbox> findByFromAndTo(String from, String to);
}
