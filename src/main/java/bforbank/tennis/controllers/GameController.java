    package bforbank.tennis.controllers;

    import bforbank.tennis.domains.dtos.GameDTO;
    import bforbank.tennis.domains.requests.CreateGameRequest;
    import bforbank.tennis.domains.requests.PointRequest;
    import bforbank.tennis.services.GameService;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/v1/games")
    public class GameController {
        private final GameService gameService;

        public GameController(GameService gameService) {
            this.gameService = gameService;
        }

        @PostMapping
        public ResponseEntity<GameDTO> createGame(
                @RequestBody @Valid CreateGameRequest request
        ) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(gameService.createGame(request));
        }

        @PostMapping("/{id}/point")
        public ResponseEntity<GameDTO> recordPoint(
                @PathVariable Long id,
                @RequestBody @Valid PointRequest request
        ) {
            return ResponseEntity.ok(gameService.recordPoint(id, request));
        }

        @GetMapping("/{id}")
        public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
            return ResponseEntity.ok(gameService.getGame(id));
        }
    }
