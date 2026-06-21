package com.adhm.dawam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recurrence_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurrenceRule {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recurrence_rule_days", joinColumns = @JoinColumn(name = "recurrence_rule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private List<DayOfWeek> daysOfWeek;

    public enum Frequency {
        DAILY,
        SPECIFIC_DAYS
    }

    public enum DayOfWeek {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}