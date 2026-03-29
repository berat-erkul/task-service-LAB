package com.cydeo.client;

import com.cydeo.dto.Response.UserResponse;
import com.cydeo.exception.EmployeeCheckFailedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFacade {

    private final UserClient userClient;

    public UserFeignFacade(UserClient userClient) {
        this.userClient = userClient;
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "checkByUsernameFallback")
    public ResponseEntity<UserResponse> checkByUsername(String username) {
        return userClient.checkByUsername(username);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<UserResponse> checkByUsernameFallback(String username, Throwable t) {
        throw new EmployeeCheckFailedException("Employee check is failed.");
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "checkEmployeeByUsernameFallback")
    public ResponseEntity<UserResponse> checkEmployeeByUsername(String username) {
        return userClient.checkEmployeeByUsername(username);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<UserResponse> checkEmployeeByUsernameFallback(String username, Throwable t) {
        throw new EmployeeCheckFailedException("Employee check is failed.");
    }
}
