package WebTech.TP.TimeSeriesManager.api.controller;

import WebTech.TP.TimeSeriesManager.dao.Event;
import WebTech.TP.TimeSeriesManager.dao.TimeSeries;
import WebTech.TP.TimeSeriesManager.dao.UserAccessType;
import WebTech.TP.TimeSeriesManager.dto.EventDTO;
import WebTech.TP.TimeSeriesManager.service.EventService;
import WebTech.TP.TimeSeriesManager.service.TimeSeriesService;
import WebTech.TP.TimeSeriesManager.util.StringUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/app/api/timeseries/{timeSeriesId}/events")
public class EventAPIController {
    private final EventService service;
    private final TimeSeriesService timeSeriesService;

    @Autowired
    public EventAPIController(EventService service, TimeSeriesService timeSeriesService) {
        this.service = service;
        this.timeSeriesService = timeSeriesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getAllEvents(@PathVariable String timeSeriesId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<TimeSeries> timeSeries = timeSeriesService.findById(timeSeriesId);
        if(timeSeries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Time series %s does not exist", timeSeriesId));
        }

        if(timeSeries.get().getUserAccessType(authentication.getName()) == UserAccessType.NOT_AUTHORIZED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("You are not authorized to view time series %s", timeSeriesId));
        }

        List<Event> list = service.findByTimeSeriesId(timeSeriesId);
        List<EventDTO> listDTO = new ArrayList<>();
        for(Event event : list) {
            listDTO.add(event.toDTO());
        }
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Object> getEvent(@PathVariable String timeSeriesId, @PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Event> event = service.findByTimeSeriesIdAndId(timeSeriesId, id);
        if(event.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(event.get().toDTO());
        }

        Optional<TimeSeries> timeSeries = timeSeriesService.findById(timeSeriesId);
        if(timeSeries.get().getUserAccessType(authentication.getName()) == UserAccessType.NOT_AUTHORIZED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("You are not authorized to view time series %s", timeSeriesId));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Event %s does not exist under TimeSeries %s", id, timeSeriesId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> postEvent(@PathVariable String timeSeriesId, @RequestBody EventDTO event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<TimeSeries> timeSeries = timeSeriesService.findById(timeSeriesId);
        if(timeSeries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Time series %s does not exist", timeSeriesId));
        }

        if(timeSeries.get().getUserAccessType(authentication.getName()) != UserAccessType.FULL) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("You are not authorized to edit time series %s", timeSeriesId));
        }

        if(!StringUtilities.isBlankOrNull(event.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The ID is automatically generated");
        }

        if(!StringUtilities.isBlankOrNull(event.getTimeSeriesId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("It is not allowed to send the time series ID in the request body");
        }
        event.setTimeSeriesId(timeSeriesId);

        Event savedEvent = event.toEntity();
        service.save(savedEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent.toDTO());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Object> putEvent(@PathVariable String timeSeriesId, @PathVariable String id, @RequestBody EventDTO event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!service.existsByTimeSeriesIdAndId(timeSeriesId, id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Event %s does not exist under TimeSeries %s", id, timeSeriesId));
        }

        Optional<TimeSeries> timeSeries = timeSeriesService.findById(timeSeriesId);
        if(timeSeries.get().getUserAccessType(authentication.getName()) != UserAccessType.FULL) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("You are not authorized to edit time series %s", timeSeriesId));
        }

        if(!StringUtilities.isBlankOrNull(event.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updating the ID is not allowed");
        }
        event.setId(id);

        if(!StringUtilities.isBlankOrNull(event.getTimeSeriesId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("It is not allowed to send the time series ID in the request body");
        }
        event.setTimeSeriesId(timeSeriesId);

        Event savedEvent = event.toEntity();
        service.save(savedEvent);
        return ResponseEntity.status(HttpStatus.OK).body(savedEvent.toDTO());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Object> deleteEvent(@PathVariable String timeSeriesId, @PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!service.existsByTimeSeriesIdAndId(timeSeriesId, id)) {
            return ResponseEntity.status(HttpStatus.OK).body(String.format("Event %s does not exist under TimeSeries %s", id, timeSeriesId));
        }

        Optional<TimeSeries> timeSeries = timeSeriesService.findById(timeSeriesId);
        if(timeSeries.get().getUserAccessType(authentication.getName()) != UserAccessType.FULL) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(String.format("You are not authorized to edit time series %s", timeSeriesId));
        }

        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Time series %s was deleted", id));
    }
}
