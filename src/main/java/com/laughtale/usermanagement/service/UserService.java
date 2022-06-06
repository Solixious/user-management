package com.laughtale.usermanagement.service;

import com.laughtale.usermanagement.exception.BaseBusinessException;
import com.laughtale.usermanagement.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    /**
     * API to register new user. If the first user is being registered, s/he will have ADMIN role
     * @param userDto Initial data object to use for registering user
     * @throws BaseBusinessException exception
     * @return The user data object of the registered user
     */
    UserDto registerUser(final UserDto userDto) throws BaseBusinessException;

    /**
     * API to get list of users registered in the system
     * @param pageNo The page number to be retrieved
     * @return List of user data objects retrieved for the given page number
     */
    Page<UserDto> getUsers(final int pageNo);

    void updateLastLoginInfo(final String userName);

    UserDto updateUser(UserDto userDto) throws BaseBusinessException;
}
