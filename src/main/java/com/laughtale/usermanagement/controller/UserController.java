package com.laughtale.usermanagement.controller;

import com.laughtale.usermanagement.config.TokenUtil;
import com.laughtale.usermanagement.constants.ErrorCode;
import com.laughtale.usermanagement.constants.UrlMapping;
import com.laughtale.usermanagement.exception.BaseBusinessException;
import com.laughtale.usermanagement.model.ErrorDto;
import com.laughtale.usermanagement.model.TokenDto;
import com.laughtale.usermanagement.model.UserDto;
import com.laughtale.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMapping.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(UrlMapping.REGISTER)
    public Object register(@RequestBody final UserDto userDto) {
        try {
            return userService.registerUser(userDto);
        } catch (BaseBusinessException e) {
            return new ErrorDto(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            return new ErrorDto(ErrorCode.GENERAL_ERROR.name(), ErrorCode.GENERAL_ERROR.getErrorMessage());
        }
    }

    @PostMapping(UrlMapping.TOKEN)
    public Object getToken(@RequestBody final UserDto userDto) {
        try {
            authenticate(userDto.getUserName(), userDto.getPassword());
            UserDetails userDetails = userService.loadUserByUsername(userDto.getUserName());
            String token = tokenUtil.generateToken(userDetails);
            return new TokenDto(token);
        } catch (BaseBusinessException e) {
            return new ErrorDto(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            return new ErrorDto(ErrorCode.GENERAL_ERROR.name(), ErrorCode.GENERAL_ERROR.getErrorMessage());
        }
    }

    @PostMapping(UrlMapping.UPDATE)
    public Object update(@RequestBody final UserDto userDto) {
        try {
            return userService.updateUser(userDto);
        } catch (BaseBusinessException e) {
            return new ErrorDto(e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            return new ErrorDto(ErrorCode.GENERAL_ERROR.name(), ErrorCode.GENERAL_ERROR.getErrorMessage());
        }
    }

    private void authenticate(final String username, final String password) throws BaseBusinessException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new BaseBusinessException(ErrorCode.USER_DISABLED);
        } catch (BadCredentialsException e) {
            throw new BaseBusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }
}
