package com.service.plan.__minute_per_day_plans.repository;

import com.service.plan.__minute_per_day_plans.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
}
