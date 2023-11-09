package WebTech.TP.TimeSeriesManager.api.controller;

import WebTech.TP.TimeSeriesManager.dao.User;
import WebTech.TP.TimeSeriesManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/api/user")
public class UserAPIController {

    private final UserService service;

    @Autowired
    public UserAPIController(UserService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> postUser(@RequestBody User user) {
        service.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
