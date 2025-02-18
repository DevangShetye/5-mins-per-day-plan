package com.service.plan.__minute_per_day_plans.repository;

import com.service.plan.__minute_per_day_plans.entity.ActivityProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityProgressRepository extends JpaRepository<ActivityProgress,Integer> {
    Optional<ActivityProgress> findByActivityDatesAndUserId(String day, Integer userId);
}
