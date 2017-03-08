package org.poc.client1.repository;

import org.poc.client1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

 
/**
 * @author SPrassanna
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
