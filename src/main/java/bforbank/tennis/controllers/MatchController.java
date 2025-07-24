package bforbank.tennis.controllers;


import bforbank.tennis.domains.dtos.MatchDTO;
import bforbank.tennis.domains.requests.MatchRequest;
import bforbank.tennis.services.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Match API", description = "Manage tennis matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    @Operation(summary = "Create a new match")
    public ResponseEntity<MatchDTO> createMatch(@RequestBody MatchRequest matchRequest) {
        return ResponseEntity.ok(matchService.createMatch(matchRequest));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a match by ID")
    public ResponseEntity<MatchDTO> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatch(id));
    }

    @GetMapping
    @Operation(summary = "Get all matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }
}
