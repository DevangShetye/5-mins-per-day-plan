package com.service.plan.__minute_per_day_plans.vo;


import lombok.Data;

@Data
public class SuggestedPlanVO {
    private Integer id;
    private String category;
    private String activityTitle;
    private String frequency;
    private String time;
    private Boolean isCompleted;
}
