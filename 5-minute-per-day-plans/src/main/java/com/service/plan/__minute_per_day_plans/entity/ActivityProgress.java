package com.service.plan.__minute_per_day_plans.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table(name = "activity_progress")
public class ActivityProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "activity_dates")
    private String activityDates;

    @Column(name = "suggested_activities")
    private String suggestedActivities;

    @Column(name = "completed_activities")
    private String completedActivities;
}
