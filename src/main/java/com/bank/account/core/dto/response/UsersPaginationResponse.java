package com.bank.account.core.dto.response;

import com.bank.account.core.common.responses.PaginationResponse;
import com.bank.account.core.entity.UserEntity;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersPaginationResponse extends PaginationResponse {
    private List<UserEntity> users;

    public UsersPaginationResponse(Page<UserEntity> entityPage) {
        this.setUsers(entityPage.getContent());
        super.setPagResponse(entityPage);
    }
}
