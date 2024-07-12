package com.rocketseat.planner.activities;

import com.rocketseat.planner.activities.dto.CreateActivityResponse;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository repository;

    public CreateActivityResponse registerActivity(String title, String occursAt, Trip trip) {
        Activity newActivity = new Activity(title, occursAt, trip);
        this.repository.save(newActivity);
        return new CreateActivityResponse(newActivity.getId());
    }
}
