package com.cydeo.client;

import com.cydeo.dto.Response.ProjectResponse;
import com.cydeo.exception.ManagerNotRetrievedException;
import com.cydeo.exception.ProjectCheckFailedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectFeignFacade {

    private final ProjectClient projectClient;

    public ProjectFeignFacade(ProjectClient projectClient) {
        this.projectClient = projectClient;
    }

    @CircuitBreaker(name = "project-service", fallbackMethod = "checkByProjectCodeFallback")
    public ResponseEntity<ProjectResponse> checkByProjectCode(String projectCode) {
        return projectClient.checkByProjectCode(projectCode);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<ProjectResponse> checkByProjectCodeFallback(String projectCode, Throwable t) {
        throw new ProjectCheckFailedException("Project check is failed.");
    }

    @CircuitBreaker(name = "project-service", fallbackMethod = "getManagerByProjectFallback")
    public ResponseEntity<ProjectResponse> getManagerByProject(String projectCode) {
        return projectClient.getManagerByProject(projectCode);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<ProjectResponse> getManagerByProjectFallback(String projectCode, Throwable t) {
        throw new ManagerNotRetrievedException("Manager can not be retrieved.");
    }

    @CircuitBreaker(name = "project-service", fallbackMethod = "startProjectFallback")
    public ResponseEntity<ProjectResponse> startProject(String projectCode) {
        return projectClient.startProject(projectCode);
    }

    @SuppressWarnings("unused")
    private ResponseEntity<ProjectResponse> startProjectFallback(String projectCode, Throwable t) {
        throw new ProjectCheckFailedException("Project start is failed.");
    }
}
