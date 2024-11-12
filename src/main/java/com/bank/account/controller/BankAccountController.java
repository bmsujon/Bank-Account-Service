package com.bank.account.controller;

import com.bank.account.core.common.responses.ExceptionResponse;
import com.bank.account.core.dto.request.BankAccountCreationRequest;
import com.bank.account.core.dto.response.BankAccountPaginationResponse;
import com.bank.account.core.entity.BankAccountEntity;
import com.bank.account.core.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Slf4j
@Tag(name = "2. Account", description = "User bank account related end points")
@Order(2)
public class BankAccountController {
    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @Operation(summary = "Create a bank account", description = """
            Create a bank account with the required info
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BankAccountEntity.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request. For validation errors, errorList contains values", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "Unknown", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody BankAccountCreationRequest request) {

        return ResponseEntity.ok(service.createAccount(request));
    }

    @Operation(summary = "Get Bank Account List.", description = """
            Get Bank Account List(paginated list) of users. You can filter them with username and bankAccountNo.
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BankAccountPaginationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request. For validation errors, errorList contains values", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "Unknown", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "accountNumber", required = false) String accountNumber,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(service.getAccounts(accountNumber, username, pageable));
    }

    @Operation(summary = "Get bank account details by accountNumber.", description = """
            Get bank account details by accountNumber
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BankAccountEntity.class)))
    @ApiResponse(responseCode = "404", description = "Not found.", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "Unknown", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping("/detail/{accountNumber}")
    public ResponseEntity<?> getUseDetails(@Parameter(description = "accountNumber of the customer.")@PathVariable String accountNumber) {

        return ResponseEntity.ok(service.getBankAccountDetails(accountNumber));
    }
}
