package com.eps.module.seeder.repository.activity;

import com.eps.module.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitySeederRepository extends JpaRepository<Activity, Long> {
}
