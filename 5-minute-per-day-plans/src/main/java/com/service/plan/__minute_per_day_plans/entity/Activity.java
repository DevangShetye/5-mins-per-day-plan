package com.service.plan.__minute_per_day_plans.entity;


import lombok.*;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category")
    private String category;

    @Column(name = "activity_title")
    private String activityTitle;

    @Column(name = "frequency")
    private  String frequency;

    @Column(name = "time")
    private String time;

}
