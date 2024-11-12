package com.bank.account.core.dto.response;

import com.bank.account.core.common.responses.PaginationResponse;
import com.bank.account.core.entity.BankAccountEntity;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountPaginationResponse extends PaginationResponse {
    private List<BankAccountEntity> accounts;

    public BankAccountPaginationResponse(Page<BankAccountEntity> entityPage) {
        this.setAccounts(entityPage.getContent());
        super.setPagResponse(entityPage);
    }
}
