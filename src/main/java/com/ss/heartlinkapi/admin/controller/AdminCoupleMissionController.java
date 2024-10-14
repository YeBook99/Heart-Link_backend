package com.ss.heartlinkapi.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminCoupleMissionController {

    @PostMapping("/missionLink")
    public ResponseEntity<?> addMissionTag(){

    }
}
