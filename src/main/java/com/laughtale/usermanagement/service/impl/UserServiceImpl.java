package com.laughtale.usermanagement.service.impl;

import com.laughtale.usermanagement.constants.ErrorCode;
import com.laughtale.usermanagement.constants.Role;
import com.laughtale.usermanagement.exception.BaseBusinessException;
import com.laughtale.usermanagement.model.UserDto;
import com.laughtale.usermanagement.model.entity.RoleEntity;
import com.laughtale.usermanagement.model.entity.UserEntity;
import com.laughtale.usermanagement.repository.RoleRepository;
import com.laughtale.usermanagement.repository.UserRepository;
import com.laughtale.usermanagement.service.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    public static final int PAGE_SIZE = 20;
    public static final int MINIMUM_PASSWORD_LENGTH = 7;
    public static final int MINIMUM_USERNAME_LENGTH = 4;
    public static final int MAXIMUM_USERNAME_LENGTH = 24;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Mapper mapper;

    @Autowired
    private Pattern usernamePattern;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User name '" + userName + "' not found."));
        return new User(userEntity.getUserName(), userEntity.getPassword(), getGrantedAuthorities(userEntity));
    }

    @Override
    public UserDto registerUser(final UserDto userDto) throws BaseBusinessException {
        validateNewUser(userDto);
        long userCount = userRepository.count();
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleRepository.findByName(((userCount > 0) ? Role.USER : Role.ADMIN).name()).orElseThrow());
        userEntity.setRoles(roleEntities);
        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public Page<UserDto> getUsers(final int pageNo) {
        return userRepository.findAll(Pageable.ofSize(PAGE_SIZE).withPage(pageNo)).map(e -> mapper.map(e, UserDto.class));
    }

    @Override
    public void updateLastLoginInfo(final String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow();
        userEntity.setLastActive(new Date());
        userRepository.save(userEntity);
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws BaseBusinessException {
        validateUpdateUser(userDto);
        UserEntity userEntity = userRepository.findByUserName(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).orElseThrow();
        Optional.ofNullable(userDto.getUserName()).ifPresent(userEntity::setUserName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(userEntity::setEmail);
        Optional.ofNullable(userDto.getProfilePicture()).ifPresent(userEntity::setProfilePicture);
        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(final UserEntity userEntity) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (RoleEntity authority : userEntity.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    private void validateNewUser(final UserDto userDto) throws BaseBusinessException {

        // Password Length
        if (userDto.getPassword() == null || userDto.getPassword().length() < MINIMUM_PASSWORD_LENGTH) {
            throw new BaseBusinessException(ErrorCode.PASSWORD_LENGTH);
        }

        // Username Length
        if (userDto.getUserName() == null || userDto.getUserName().length() < MINIMUM_USERNAME_LENGTH) {
            throw new BaseBusinessException(ErrorCode.USERNAME_TOO_SHORT_LENGTH);
        } else if (userDto.getUserName().length() > MAXIMUM_USERNAME_LENGTH) {
            throw new BaseBusinessException(ErrorCode.USERNAME_TOO_LONG_LENGTH);
        }

        // Username Pattern
        if (!usernamePattern.matcher(userDto.getUserName()).find()) {
            throw new BaseBusinessException(ErrorCode.INVALID_USERNAME);
        }

        // Username Exists
        if (userRepository.findByUserName(userDto.getUserName()).isPresent()) {
            throw new BaseBusinessException(ErrorCode.USERNAME_EXISTS);
        }

        // Email Exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BaseBusinessException(ErrorCode.EMAIL_EXISTS);
        }
    }

    private void validateUpdateUser(final UserDto userDto) throws BaseBusinessException {
        // Update User Needed
        if (userDto.getUserName() != null) {

            // Username Length
            if (userDto.getUserName().length() < MINIMUM_USERNAME_LENGTH) {
                throw new BaseBusinessException(ErrorCode.USERNAME_TOO_SHORT_LENGTH);
            } else if (userDto.getUserName().length() > MAXIMUM_USERNAME_LENGTH) {
                throw new BaseBusinessException(ErrorCode.USERNAME_TOO_LONG_LENGTH);
            }

            // Username Pattern
            if (!usernamePattern.matcher(userDto.getUserName()).find()) {
                throw new BaseBusinessException(ErrorCode.INVALID_USERNAME);
            }

            // Username Exists
            if (userRepository.findByUserName(userDto.getUserName()).isPresent()) {
                throw new BaseBusinessException(ErrorCode.USERNAME_EXISTS);
            }
        }

        // Email Exists
        if (userDto.getEmail() != null && userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BaseBusinessException(ErrorCode.EMAIL_EXISTS);
        }
    }
}