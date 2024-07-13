package com.rocketseat.planner.link;

import com.rocketseat.planner.link.dto.CreateLinkResponse;
import com.rocketseat.planner.link.dto.LinkResponse;
import com.rocketseat.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;


    public CreateLinkResponse registerLink(String title, String url, Trip trip) {
        Link link = new Link(title, url, trip);

        this.repository.save(link);

        return new CreateLinkResponse(link.getId());
    }

    public List<LinkResponse> getAllLinksByTripId(UUID id) {
        return this.repository.findByTripId(id)
              .stream()
              .map(
                    link -> new LinkResponse(link.getId(), link.getTitle(), link.getUrl())
              ).toList();
    }
}
