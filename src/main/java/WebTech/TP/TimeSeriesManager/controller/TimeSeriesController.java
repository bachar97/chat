package WebTech.TP.TimeSeriesManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TimeSeriesController {
    @GetMapping("/app/timeseries/{id}")
    public ModelAndView getTimeSeries(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("timeseriesview");
        mav.addObject("name", id);
        return mav;
    }
}
