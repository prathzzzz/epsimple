package com.eps.module.seeder.repository.activity;

import com.eps.module.activity.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long> {
}
