package com.adhm.dawam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "task_completions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "completed_date"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "completed_date", nullable = false)
    private LocalDate completedDate;
}