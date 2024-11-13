package com.bank.account.core.service;


import com.amazonaws.services.personalizeevents.model.InvalidInputException;
import com.bank.account.core.common.utls.AppUtils;
import com.bank.account.core.dto.request.BankAccountCreationRequest;
import com.bank.account.core.dto.response.BankAccountPaginationResponse;
import com.bank.account.core.entity.BankAccountEntity;
import com.bank.account.core.entity.UserEntity;
import com.bank.account.core.enums.AccountTypeEnum;
import com.bank.account.core.enums.CurrencyEnum;
import com.bank.account.core.repository.BankAccountRepository;

import com.bank.account.core.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankAccountService {
    private final BankAccountRepository repository;
    private final UserRepository userRepository;
    public BankAccountService(BankAccountRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    public BankAccountEntity createAccount(BankAccountCreationRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(request.getUserId());
        if(userEntityOptional.isEmpty()) {
            throw new NotFoundException("user not found with userId: " + request.getUserId());
        }

        validateAccountType(request.getAccountType().name());
        validateCurrencyType(request.getCurrencyList());

        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setUser(userEntityOptional.get());
        bankAccountEntity.setAccountNumber(AppUtils.generateUid().toString());
        bankAccountEntity.setAccountType(request.getAccountType());
        bankAccountEntity.setCurrencyList(request.getCurrencyList());
        bankAccountEntity.setAccountHolderName(userEntityOptional.get().getFullName());
        bankAccountEntity.setBalance(request.getBalance() == null ? BigDecimal.ZERO : request.getBalance());

        return repository.save(bankAccountEntity);
    }

    private List<String> getAccountTypeStringsFromEnum() {
        List<String> types = EnumSet.allOf(AccountTypeEnum.class).stream().map(AccountTypeEnum::name).collect(Collectors.toList());
        log.info("accountTypes: {}", types);
        return types;
    }

    private void validateAccountType(String type) {
        if (!getAccountTypeStringsFromEnum().contains(type)) {
            throw new InvalidInputException("AccountType must be either " + AccountTypeEnum.SAVINGS.name() + " or " + AccountTypeEnum.CURRENT.name());
        }
    }

    private List<String> getCurrencyStringsFromEnum() {
        List<String> types = EnumSet.allOf(CurrencyEnum.class).stream().map(CurrencyEnum::name).collect(Collectors.toList());
        log.info("currencyTypes: {}", types);
        return types;
    }

    private void validateCurrencyType(List<String> types) {
        for (String type : types) {
            if (!getCurrencyStringsFromEnum().contains(type)) {
                throw new InvalidInputException("Currency must be either " + CurrencyEnum.BDT.name() + " or " + CurrencyEnum.USD.name());
            }
        }
    }

    public BankAccountPaginationResponse getAccounts(String accountNumber, String username, Pageable pageable) {

        Specification<BankAccountEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.isNotBlank(username)) {
                UserEntity userEntity = userRepository.findByUserName(username);
                if(userEntity != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("user"), userEntity));
                }
            }

            if (StringUtils.isNotBlank(accountNumber)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("accountNumber"), accountNumber));
            }

            return predicate;
        };
        Page<BankAccountEntity> entityPage = repository.findAll(specification, pageable);
        return new BankAccountPaginationResponse(entityPage);
    }

    public BankAccountEntity getBankAccountDetails(String accountNumber) {
        BankAccountEntity bankAccountEntity = repository.findByAccountNumber(accountNumber);
        if(bankAccountEntity == null)
            throw new NotFoundException("Bank account not found with accountNumber: " + accountNumber);
        return bankAccountEntity;
    }
}
