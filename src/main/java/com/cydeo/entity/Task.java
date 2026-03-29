package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")
public class Task extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String taskCode;

    private String taskSubject;
    private String taskDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status taskStatus;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate assignedDate;

    @Column(nullable = false)
    private String projectCode;

    // private User assignedEmployee;    User-> "String" username (key/unique) so we use String
    @Column(nullable = false)
    private String assignedEmployee;

    // private User assignedManager;    User-> "String" username (key/unique) so we use String
    @Column(nullable = false)
    private String assignedManager;

}
