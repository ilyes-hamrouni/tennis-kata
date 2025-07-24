package bforbank.tennis.controllers;

import bforbank.tennis.domains.dtos.GameDTO;
import bforbank.tennis.domains.requests.CreateGameRequest;
import bforbank.tennis.domains.requests.PointRequest;
import bforbank.tennis.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Game API", description = "Endpoints for creating and managing tennis games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new game",
            description = "Creates a new tennis game between two teams of players"
    )
    public ResponseEntity<GameDTO> createGame(@RequestBody @Valid CreateGameRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gameService.createGame(request));
    }

    @PostMapping("/{id}/point")
    @Operation(
            summary = "Record a point for a game",
            description = "Updates the score by adding a point to the specified team in the game"
    )
    public ResponseEntity<GameDTO> recordPoint(
            @PathVariable Long id,
            @RequestBody @Valid PointRequest request
    ) {
        return ResponseEntity.ok(gameService.recordPoint(id, request));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a game by ID",
            description = "Retrieves the full game state including score and point history"
    )
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }
}
