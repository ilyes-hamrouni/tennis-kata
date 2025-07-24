package bforbank.tennis.controllers;

import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.dtos.SetDTO;
import bforbank.tennis.domains.enums.TeamType;
import bforbank.tennis.services.SetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sets")
@Tag(name = "Set API", description = "Manage tennis sets")
public class SetController {

    private final SetService setService;

    public SetController(SetService setService) {
        this.setService = setService;
    }

    @PostMapping("/match/{matchId}")
    @Operation(summary = "Create a new set in a match")
    public ResponseEntity<SetDTO> createSet(@PathVariable Long matchId) {
        return ResponseEntity.ok(setService.createSet(matchId));
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "List all sets of a match")
    public ResponseEntity<List<SetDTO>> getSets(@PathVariable Long matchId) {
        return ResponseEntity.ok(setService.getSetsByMatch(matchId));
    }

    @PostMapping("/{setId}/record-win/{team}")
    @Operation(summary = "Record a game win for a team in the set")
    public ResponseEntity<Void> recordWin(@PathVariable Long setId, @PathVariable TeamType team) {
        setService.recordGameWin(setId, team);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{setId}/games")
    public ResponseEntity<GameDTO> createGameInSet(@PathVariable Long setId) {
        return ResponseEntity.ok(setService.createGameInSet(setId));
    }

    @GetMapping("/{setId}/games/latest")
    public ResponseEntity<GameDTO> getLatestGameInSet(@PathVariable Long setId) {
        return ResponseEntity.ok(setService.getLatestGameInSet(setId));
    }
}