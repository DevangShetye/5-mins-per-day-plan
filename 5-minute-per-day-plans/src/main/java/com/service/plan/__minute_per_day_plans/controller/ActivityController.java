package com.service.plan.__minute_per_day_plans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.plan.__minute_per_day_plans.service.ActivityProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api")
@RestController
public class ActivityController {

    @Autowired
    private ActivityProgressService activityProgressService;


    @GetMapping("/program")
    ResponseEntity<Map<String,Object>> getPlans(@RequestParam("day")String day, @RequestHeader("user_id")Integer userId)  {
        Map<String,Object> responseMap=activityProgressService.fetchProgram(day,userId);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/comfirm")
    ResponseEntity<Map<String,Object>> confirmActivity(@RequestParam("activity_date")String activityDate,@RequestParam("suggested_activity")String suggestedActivity , @RequestHeader("user_id")Integer userId) throws JsonProcessingException {
        Map<String,Object> responseMap=activityProgressService.confirmActivity(activityDate,suggestedActivity,userId);
        return ResponseEntity.ok(responseMap);
    }
}
