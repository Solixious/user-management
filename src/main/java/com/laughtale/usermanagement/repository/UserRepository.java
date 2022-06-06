package com.laughtale.usermanagement.repository;

import com.laughtale.usermanagement.model.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByEmail(String email);
}
