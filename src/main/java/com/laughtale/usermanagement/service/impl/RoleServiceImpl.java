package com.laughtale.usermanagement.service.impl;

import com.laughtale.usermanagement.model.entity.RoleEntity;
import com.laughtale.usermanagement.repository.RoleRepository;
import com.laughtale.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String getRole(final String name) {
        if (!StringUtils.isEmpty(name)) {
            final RoleEntity roleEntity = roleRepository.findByName(name).orElse(null);
            if (roleEntity != null && !StringUtils.isEmpty(roleEntity.getName())) {
                return roleEntity.getName();
            }
        }
        return null;
    }

    @Override
    public void addRole(final String name) {
        if (!StringUtils.isEmpty(name)) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(name);
            roleRepository.save(roleEntity);
        }
    }

}