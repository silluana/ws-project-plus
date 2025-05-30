package com.client.ws.projectplus.repository.jpa;

import com.client.ws.projectplus.model.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
