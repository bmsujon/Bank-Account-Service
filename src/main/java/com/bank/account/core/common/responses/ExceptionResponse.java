package com.bank.account.core.common.responses;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {
    private HttpStatus status;
    private String message;
    private Integer code;
    private List<String> errorList;

}
