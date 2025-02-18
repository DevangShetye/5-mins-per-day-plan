package com.service.plan.__minute_per_day_plans.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.plan.__minute_per_day_plans.entity.Activity;
import com.service.plan.__minute_per_day_plans.entity.ActivityProgress;
import com.service.plan.__minute_per_day_plans.repository.ActivityProgressRepository;
import com.service.plan.__minute_per_day_plans.repository.ActivityRepository;
import com.service.plan.__minute_per_day_plans.vo.SuggestedPlanVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ActivityProgressServiceTest {

    @Mock
    private ActivityProgressRepository activityProgressRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityProgressService activityProgressService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ActivityProgress activityProgress;
    private List<Activity> activityList;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        activityProgress = new ActivityProgress();
        activityProgress.setActivityDates("2025-02-18");
        activityProgress.setUserId(1);
        activityProgress.setSuggestedActivities(objectMapper.writeValueAsString(Arrays.asList(1, 2, 3)));
        activityProgress.setCompletedActivities(objectMapper.writeValueAsString(Collections.singletonList(1)));

        Activity activity1 = new Activity(1, "Yoga", "Health", "2x/Day", "30");
        Activity activity2 = new Activity(2, "Meditation", "Wellness", "1x/Day", "20");
        Activity activity3 = new Activity(3, "Reading", "Education", "Max", "40");
        activityList = Arrays.asList(activity1, activity2, activity3);
    }

    @Test
    void testFetchProgram_WhenUserHasPlan() throws JsonProcessingException {
        when(activityProgressRepository.findByActivityDatesAndUserId("2024-07-01", 1))
                .thenReturn(Optional.of(activityProgress));
        when(activityRepository.findAll()).thenReturn(activityList);

        Map<String, Object> response = activityProgressService.fetchProgram("2024-07-01", 1);

        assertNotNull(response);
        assertEquals("success", response.get("status"));
        List<SuggestedPlanVO> suggestedPlans = (List<SuggestedPlanVO>) ((Map<String, Object>) response.get("result")).get("list");
        assertEquals(3, suggestedPlans.size());
        assertTrue(suggestedPlans.stream().anyMatch(plan -> plan.getIsCompleted()));
    }

    @Test
    void testFetchProgram_WhenUserHasNoPlan() {
        when(activityProgressRepository.findByActivityDatesAndUserId("2024-07-01", 1))
                .thenReturn(Optional.empty());
        Map<String, Object> response = activityProgressService.fetchProgram("2024-07-01", 1);
        assertNotNull(response);
        assertEquals("failure", response.get("status"));
        verify(activityProgressRepository).findByActivityDatesAndUserId("2024-07-01", 1);
    }


    @Test
    void testConfirmActivity_Success() throws JsonProcessingException {
        when(activityProgressRepository.findByActivityDatesAndUserId("2024-07-01", 1))
                .thenReturn(Optional.of(activityProgress));

        Map<String, Object> response = activityProgressService.confirmActivity("2024-07-01", "2", 1);

        assertNotNull(response);
        assertEquals("success", response.get("status"));
        verify(activityProgressRepository, times(1)).save(any(ActivityProgress.class));
    }

    @Test
    void testConfirmActivity_WhenActivityNotInPlan() throws JsonProcessingException {
        when(activityProgressRepository.findByActivityDatesAndUserId("2024-07-01", 1))
                .thenReturn(Optional.of(activityProgress));

        Map<String, Object> response = activityProgressService.confirmActivity("2024-07-01", "5", 1);

        assertNotNull(response);
        assertEquals("failure", response.get("status"));
        assertEquals("Activity not in your Plan", ((Map<String, Object>) response.get("result")).get("result"));
    }
}
