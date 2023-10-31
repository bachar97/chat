package WebTech.TP.TimeSeriesManager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import WebTech.TP.TimeSeriesManager.model.TimeSeries;

public interface TimeSeriesRepository extends JpaRepository<TimeSeries, String> {
    
}