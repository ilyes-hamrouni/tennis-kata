package bforbank.tennis.controllers;

import bforbank.tennis.services.PlayerService;
import bforbank.tennis.domains.dtos.PlayerDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@Validated
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> create(@Valid @RequestBody PlayerDTO playerDTO) {
        PlayerDTO created = playerService.create(playerDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
