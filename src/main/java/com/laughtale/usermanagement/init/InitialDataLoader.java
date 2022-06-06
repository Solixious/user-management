package com.laughtale.usermanagement.init;

import com.laughtale.usermanagement.constants.Role;
import com.laughtale.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RoleService roleService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (Role role : Role.values()) {
            createRoleIfNotFound(role.name());
        }
    }

    @Transactional
    public String createRoleIfNotFound(final String name) {
        final String roleName = roleService.getRole(name);
        if (roleName == null) {
            roleService.addRole(name);
        }
        return roleName;
    }
}
