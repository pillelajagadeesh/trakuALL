package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.security.AuthoritiesConstants;
import com.tresbu.trakeye.service.dto.GeofenceDTO;
import com.tresbu.trakeye.web.rest.vm.LoggerVM;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.swagger.annotations.ApiOperation;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/management/jhipster")
public class LogsResource {

    @RequestMapping(value = "/logs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    @ApiOperation(value = "Get list of logs", notes = "User can get list of all logs. User with role Super Admin is allowed.", response = LoggerVM.class)
    public List<LoggerVM> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList()
            .stream()
            .map(LoggerVM::new)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/logs",
        method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    @Secured({AuthoritiesConstants.SUPER_ADMIN})
    @ApiOperation(value = "Change log level", notes = "User can change log level of all logs. User with role Super Admin is allowed.", response = LoggerVM.class)
    public void changeLevel(@RequestBody LoggerVM jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }
}
