package com.bank.account.core.dto.request;

import com.bank.account.core.enums.AccountTypeEnum;
import com.bank.account.core.enums.CurrencyEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class BankAccountCreationRequest {
    @NotNull(message = "userId required.")
    private Long userId;
    private BigDecimal balance = BigDecimal.ZERO;
    @NotNull(message = "accountType required")
    private AccountTypeEnum accountType = AccountTypeEnum.CURRENT;
    @NotEmpty(message = "currency required")
    private List<String> currencyList;
}
