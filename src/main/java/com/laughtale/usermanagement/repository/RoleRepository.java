package com.laughtale.usermanagement.repository;

import com.laughtale.usermanagement.model.entity.RoleEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
