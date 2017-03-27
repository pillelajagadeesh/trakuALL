package com.tresbu.trakeye.web.rest;

import com.tresbu.trakeye.config.TrakEyeProperties;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProfileInfoResource {

    @Inject
    Environment env;

    @Inject
    private TrakEyeProperties jHipsterProperties;

    @RequestMapping(value = "/profile-info",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get active user profile", notes = "User can get list of active user profiles. User with role SuperAdmin, User Admin and User is allowed. ", response = ProfileInfoResponse.class)
    public ProfileInfoResponse getActiveProfiles() {
        return new ProfileInfoResponse(env.getActiveProfiles(), getRibbonEnv());
    }

    private String getRibbonEnv() {
        String[] activeProfiles = env.getActiveProfiles();
        String[] displayOnActiveProfiles = jHipsterProperties.getRibbon().getDisplayOnActiveProfiles();

        if (displayOnActiveProfiles == null) {
            return null;
        }

        List<String> ribbonProfiles = new ArrayList<>(Arrays.asList(displayOnActiveProfiles));
        List<String> springBootProfiles = Arrays.asList(activeProfiles);
        ribbonProfiles.retainAll(springBootProfiles);

        if (ribbonProfiles.size() > 0) {
            return ribbonProfiles.get(0);
        }
        return null;
    }

    class ProfileInfoResponse {

        public String[] activeProfiles;
        public String ribbonEnv;

        ProfileInfoResponse(String[] activeProfiles,String ribbonEnv) {
            this.activeProfiles=activeProfiles;
            this.ribbonEnv=ribbonEnv;
        }
    }
}
