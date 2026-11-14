package com.pathfinder.controller;

import com.pathfinder.model.*;
import com.pathfinder.service.PathfindingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pathfinding")
public class PathfindingController {
    private final PathfindingService service;
    public PathfindingController(PathfindingService service) { this.service=service; }
    @PostMapping("/find-path")
    public PathResponse findPath(@Valid @RequestBody PathRequest request) { return PathResponse.from(service.find(request)); }
    @PostMapping("/compare")
    public CompareResponse compare(@Valid @RequestBody CompareRequest request) { return service.compare(request); }
}
