package com.haglind.parvus.repository;

import com.haglind.parvus.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long>{

}
