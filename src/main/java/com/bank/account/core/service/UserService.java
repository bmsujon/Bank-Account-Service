package com.bank.account.core.service;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.bank.account.core.dto.request.UserCreationRequest;
import com.bank.account.core.dto.request.UserLoginRequest;
import com.bank.account.core.dto.response.LoginResponse;
import com.bank.account.core.dto.response.UsersPaginationResponse;
import com.bank.account.core.entity.UserEntity;
import com.bank.account.core.repository.UserRepository;
import com.bank.account.security.service.JwtService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserEntity createUser(UserCreationRequest request) {
        if(repository.existsByUserName(request.getUserName())) {
            throw new AlreadyExistsException("User already exists with username: " + request.getUserName());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setAddress(request.getAddress());
        userEntity.setEmail(request.getEmail());
        userEntity.setUserName(request.getUserName());
        userEntity.setFullName(request.getFullName());
        userEntity.setDateOfBirth(request.getDateOfBirth());
        userEntity.setPhoneNo(request.getPhoneNo());
        userEntity.setPassword(bCryptPasswordEncoder
                .encode(request.getPassword()));
        return repository.save(userEntity);
    }

    public UsersPaginationResponse getUsers(String userName, String phoneNo, String email, Pageable pageable) {
        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(userName)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userName"), userName));
            }

            if (StringUtils.isNotBlank(phoneNo)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("phoneNo"), phoneNo));
            }

            if (!ObjectUtils.isEmpty(email)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("email"), email));
            }
            return predicate;
        };
        Page<UserEntity> entityPage = repository.findAll(specification, pageable);
        return new UsersPaginationResponse(entityPage);
    }

    public UserEntity getUseDetailsById(Long id) {
        Optional<UserEntity> userEntityOptional = repository.findById(id);
        if(userEntityOptional.isEmpty())
            throw new NotFoundException("User not found with id: " + id);
        return userEntityOptional.get();
    }

    private String verify(String userName, String password) {
        log.info("user came to verify method with userName: {} and pass : {}", userName, password);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userName, password
                )
        );
        log.info("authenticate: {}" , authentication);
        //var u = userRepository.findByUserName(user.getUserName());
        if(authentication.isAuthenticated())
            return jwtService.generateToken(userName);
        else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    public LoginResponse login(UserLoginRequest request) {
        UserEntity userEntity = repository.findByUserName(request.getUserName());
        if(userEntity == null)
            throw new NotFoundException("User not found with userName: " + request.getUserName());

//        String token = "Bearer " + jwtService.generateToken(userEntity);
        String token = "Bearer " + verify(request.getUserName(), request.getPassword());

        LoginResponse response = new LoginResponse(token);
        return response;
    }
}
