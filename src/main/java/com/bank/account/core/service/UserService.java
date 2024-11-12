package com.bank.account.core.service;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.bank.account.core.dto.request.UserCreationRequest;
import com.bank.account.core.dto.response.UsersPaginationResponse;
import com.bank.account.core.entity.UserEntity;
import com.bank.account.core.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserEntity createUser(UserCreationRequest request) {
        if(repository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException("User already exists with username: " + request.getUsername());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setAddress(request.getAddress());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(request.getPassword());
        userEntity.setUsername(request.getUsername());
        userEntity.setFullName(request.getFullName());
        userEntity.setDateOfBirth(request.getDateOfBirth());
        userEntity.setPhoneNo(request.getPhoneNo());

        return repository.save(userEntity);
    }

    public UsersPaginationResponse getUsers(String username, String phoneNo, String email, Pageable pageable) {
        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(username)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("username"), username));
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
}
