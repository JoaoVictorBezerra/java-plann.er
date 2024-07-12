package com.rocketseat.planner.activities;

import com.rocketseat.planner.activities.dto.ActivityResponse;
import com.rocketseat.planner.activities.dto.CreateActivityResponse;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository repository;

    public CreateActivityResponse registerActivity(String title, String occursAt, Trip trip) {
        Activity newActivity = new Activity(title, occursAt, trip);
        this.repository.save(newActivity);
        return new CreateActivityResponse(newActivity.getId());
    }

    public List<ActivityResponse> getAllActivitiesByTripId(UUID id) {
        List<Activity> activities = this.repository.findByTripId(id);
        return activities.stream().map(activity -> new ActivityResponse(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
