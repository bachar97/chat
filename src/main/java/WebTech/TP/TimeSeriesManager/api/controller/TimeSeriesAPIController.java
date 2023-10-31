package WebTech.TP.TimeSeriesManager.api.controller;

import WebTech.TP.TimeSeriesManager.dao.TimeSeriesRepository;
import WebTech.TP.TimeSeriesManager.model.TimeSeries;

import WebTech.TP.TimeSeriesManager.util.StringUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeseries")
public class TimeSeriesAPIController {
    @Autowired
    private TimeSeriesRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public List<TimeSeries> getAllTimeSeries() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Object> getTimeSeries(@PathVariable String id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Time series %s does not exist", id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> postTimeSeries(@RequestBody TimeSeries timeseries) {
        repository.save(timeseries);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Time series %s was created", timeseries.getId()));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<String> putTimeSeries(@PathVariable String id, @RequestBody TimeSeries timeseries) {
        if(!StringUtilities.isBlankOrNull(timeseries.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Updating the ID is not allowed");
        }
        timeseries.setId(id);

        if(!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Time series %s does not exist", id));
        }

        repository.save(timeseries);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Time series %s was updated successfully", id));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<String> deleteTimeSeries(@PathVariable String id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(String.format("Time series %s does not exist or was already deleted", id));
        }
        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Time series %s was deleted", id));
    }
}
