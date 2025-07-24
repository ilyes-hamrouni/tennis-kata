package bforbank.tennis.controllers;

import bforbank.tennis.domains.dtos.PlayerDTO;
import bforbank.tennis.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@Validated
@Tag(name = "Player API", description = "Manage tennis players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new player",
            description = "Adds a new tennis player to the system"
    )
    public ResponseEntity<PlayerDTO> create(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO created = playerService.create(playerDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    @Operation(
            summary = "List all players",
            description = "Returns a list of all registered players"
    )
    public ResponseEntity<List<PlayerDTO>> getAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get player by ID",
            description = "Returns the player with the given ID"
    )
    public ResponseEntity<PlayerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a player",
            description = "Deletes the player with the specified ID"
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
