package com.bank.account.controller;

import com.bank.account.core.common.responses.ExceptionResponse;
import com.bank.account.core.dto.request.UserCreationRequest;
import com.bank.account.core.dto.response.UsersPaginationResponse;
import com.bank.account.core.entity.UserEntity;
import com.bank.account.core.service.UserService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "1. User", description = "User registration and user details related end points")
@Order(1)
@Validated
public class UserController {

    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }


    @Operation(summary = "Create user", description = """
            Create a user with the required info
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UsersPaginationResponse.class)))
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
            @Valid @RequestBody UserCreationRequest request) {

        return ResponseEntity.ok(service.createUser(request));
    }

    @Operation(summary = "Get user List.", description = """
            Get user List(paginated list). You can filter them with username, phoneNo, email.
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request. For validation errors, errorList contains values", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "Unknown", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "phoneNo", required = false) String phoneNo,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "100") Integer limit) {

        return ResponseEntity.ok(service.getUsers(username, phoneNo, email, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @Operation(summary = "Get user details by id.", description = """
            Get user details by id
            """)
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserEntity.class)))
    @ApiResponse(responseCode = "404", description = "Not found.", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @ApiResponse(responseCode = "500", description = "Unknown", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ExceptionResponse.class)))
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getUseDetailsById(@Parameter(description = "Id of the user.")@PathVariable Long id) {

        return ResponseEntity.ok(service.getUseDetailsById(id));
    }
}
