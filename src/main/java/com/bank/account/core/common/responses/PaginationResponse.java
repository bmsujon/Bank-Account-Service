package com.bank.account.core.common.responses;

import lombok.*;
import org.springframework.data.domain.Page;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private Boolean first;
    private Boolean last;
    private Integer totalPages;
    private Long totalElements;
    private Integer number;
    private Integer numberOfElements;
    private Integer size;

    public void setPagResponse(Page<?> entityPage) {
        this.setFirst(entityPage.isFirst());
        this.setLast(entityPage.isLast());
        this.setNumber(entityPage.getNumber());
        this.setNumberOfElements(entityPage.getNumberOfElements());
        this.setTotalPages(entityPage.getTotalPages());
        this.setTotalElements(entityPage.getTotalElements());
        this.setSize(entityPage.getSize());
    }
}
