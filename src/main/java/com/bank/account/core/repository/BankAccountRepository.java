package com.bank.account.core.repository;

import com.bank.account.core.entity.BankAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    Page<BankAccountEntity> findAll(Specification<BankAccountEntity> specification, Pageable pageable);

    BankAccountEntity findByAccountNumber(String accountNumber);
}
