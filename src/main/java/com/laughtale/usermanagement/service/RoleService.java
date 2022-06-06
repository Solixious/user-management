package com.laughtale.usermanagement.service;

public interface RoleService {
    /**
     * Get role saved in the database
     * @param name The exact role being searched for
     * @return The role name if it exists in the database
     */
    String getRole(final String name);

    /**
     * Add role to database
     * @param name Name of the role to be added
     */
    void addRole(final String name);
}
