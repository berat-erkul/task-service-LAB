package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;

import java.util.List;
import java.util.Map;

public interface TaskService {

    TaskDTO create(TaskDTO taskDTO);

    TaskDTO readByTaskCode(String taskCode);
    List<TaskDTO> readAllTasksByProject(String projectCode);
    List<TaskDTO> readAllByStatus(Status status);
    List<TaskDTO> readAllByStatusIsNot(Status status);

    /** Oturumdaki çalışanın bu projede atanmış görevi var mı (silinmemiş kayıtlar). */
    boolean employeeHasAssignedTaskOnProject(String projectCode);
    Map<String, Integer> getCountsByProject(String projectCode);
    Integer countNonCompletedByAssignedEmployee(String assignedEmployee);

    TaskDTO update(String taskCode, TaskDTO taskDTO);
    TaskDTO updateStatus(String taskCode, Status status);

    /**
     * Yalnızca oturumdaki çalışanın kendisine atanmış ve durumu OPEN olan görevi IN_PROGRESS yapar.
     */
    TaskDTO employeeOpenToInProgress(String taskCode);
    void completeByProject(String projectCode);

    void delete(String taskCode);
    void deleteByProject(String projectCode);

}
