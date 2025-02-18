package com.service.plan.__minute_per_day_plans.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.plan.__minute_per_day_plans.entity.Activity;
import com.service.plan.__minute_per_day_plans.entity.ActivityProgress;
import com.service.plan.__minute_per_day_plans.repository.ActivityProgressRepository;
import com.service.plan.__minute_per_day_plans.repository.ActivityRepository;
import com.service.plan.__minute_per_day_plans.vo.SuggestedPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityProgressService {
    @Autowired
    private ActivityProgressRepository activityProgressRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public Map<String, Object> fetchProgram(String day,Integer userId)  {
        Optional<ActivityProgress> userProgram =  activityProgressRepository.findByActivityDatesAndUserId(day,userId);
        Map<String,Object> resultMap = new HashMap<>();
       if(!userProgram.isPresent()){
           log.warn("user does not have a plan on {}",day);
           Map<String,Object> result = new HashMap<>();
           result.put("result","Activity not in your Plan");
           return failureResponse(result);
       }
        List<Activity> activityList=activityRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> completedActivities =null;
        List<Integer> suggestedActivities = null;
        try {
            suggestedActivities = objectMapper.readValue(userProgram.get().getSuggestedActivities(), new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (userProgram.get().getCompletedActivities()==null){
            completedActivities = new ArrayList<>();
        }else {
            try {
                completedActivities = objectMapper.readValue(userProgram.get().getCompletedActivities(), new TypeReference<List<Integer>>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        Map<Integer, Activity> actMap =activityList.stream().collect(Collectors.toMap(Activity::getId, e ->e));

       List<SuggestedPlanVO> suggestedPlanVOS = new ArrayList<>();
        for(Integer id: suggestedActivities){
            SuggestedPlanVO suggestedPlanVO = new SuggestedPlanVO();
            suggestedPlanVO.setId(actMap.get(id).getId());
            suggestedPlanVO.setActivityTitle(actMap.get(id).getActivityTitle());
            suggestedPlanVO.setCategory(actMap.get(id).getCategory());
            suggestedPlanVO.setFrequency(actMap.get(id).getFrequency());
            suggestedPlanVO.setTime(actMap.get(id).getTime());
            if (completedActivities!=null && completedActivities.contains(actMap.get(id).getId())){
                suggestedPlanVO.setIsCompleted(true);
            }else{
                suggestedPlanVO.setIsCompleted(false);
            }
            suggestedPlanVOS.add(suggestedPlanVO);
        }
         resultMap.put("list",suggestedPlanVOS);
        return successResponse(resultMap);
    }

    public Map<String, Object> confirmActivity(String activityDate, String suggestedActivity, Integer userId) {
        Optional<ActivityProgress> userPlan =  activityProgressRepository.findByActivityDatesAndUserId(activityDate,userId);
        ActivityProgress activityProgress= userPlan.get();
        Set<Integer>completedActivity= null;
        try {
            if(activityProgress.getCompletedActivities()==null){
                completedActivity= new HashSet<>();
            }else
            {
                completedActivity = new ObjectMapper().readValue(activityProgress.getCompletedActivities(), new TypeReference<Set<Integer>>() {});
            }
            if(!activityProgress.getSuggestedActivities().contains(suggestedActivity)){
                Map<String,Object> result = new HashMap<>();
                result.put("result","Activity not in your Plan");
                return failureResponse(result);
            }

            completedActivity.add(Integer.valueOf(suggestedActivity));
            activityProgress.setCompletedActivities(completedActivity.toString());
            activityProgressRepository.save(activityProgress);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("result","updated successfully");
        return successResponse(result);
    }

    public static Map<String, Object> successResponse(Map<String, Object> jsonMap) {
        log.debug("response map {}", jsonMap);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", "success");
        responseMap.put("result", jsonMap);
        responseMap.put("response_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return responseMap;
    }

    public static Map<String, Object> failureResponse(Map<String, Object> jsonMap) {
        log.debug("response map {}", jsonMap);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", "failure");
        responseMap.put("result", jsonMap);
        responseMap.put("response_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return responseMap;
    }
}
