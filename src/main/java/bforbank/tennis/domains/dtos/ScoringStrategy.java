package bforbank.tennis.domains.dtos;

import bforbank.tennis.exceptions.TennisErrorConstants;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * This service encapsulates the scoring logic for a tennis game.
 * It applies point updates based on standard tennis rules including:
 * - Normal point increments
 * - Deuce and advantage handling
 * - Win condition detection
 */
@Service
public class ScoringStrategy {

    /**
     * Mapping of point counters to tennis scoring labels.
     * 0 -> "0", 1 -> "15", 2 -> "30", 3 -> "40"
     */
    private static final Map<Integer, String> LABELS = Map.of(
            0, "0", 1, "15", 2, "30", 3, "40"
    );

    /**
     * Applies a point to the given game, updates game state,
     * handles deuce/advantage, and detects winner.
     *
     * @param game   the game to update
     * @param winner the team that won the current point
     * @throws TennisException if the game is already finished
     */
    public void applyPoint(GameDTO game, TeamType winner) {
        if (game.getStatus() == GameStatus.FINISHED) {
            throw new TennisException(
                    550,
                    TennisErrorConstants.MSG_GAME_ALREADY_FINISHED
            );
        }

        int pointsA = game.getTeamAPoints();
        int pointsB = game.getTeamBPoints();
        boolean advA = game.isTeamAHasAdvantage();
        boolean advB = game.isTeamBHasAdvantage();

        // Handle deuce and advantage logic
        if (pointsA >= 3 && pointsB >= 3) {
            if (advA && winner == TeamType.TEAM_A) {
                finish(game, true);
            } else if (advB && winner == TeamType.TEAM_B) {
                finish(game, false);
            } else if (advA && winner == TeamType.TEAM_B) {
                game.setTeamAHasAdvantage(false);
                game.setStatus(GameStatus.DEUCE);
            } else if (advB && winner == TeamType.TEAM_A) {
                game.setTeamBHasAdvantage(false);
                game.setStatus(GameStatus.DEUCE);
            } else {
                if (winner == TeamType.TEAM_A) {
                    game.setTeamAHasAdvantage(true);
                } else {
                    game.setTeamBHasAdvantage(true);
                }
                game.setStatus(GameStatus.ADVANTAGE);
            }
            return;
        }

        // Normal point increment
        if (winner == TeamType.TEAM_A) {
            pointsA++;
        } else {
            pointsB++;
        }
        game.setTeamAPoints(pointsA);
        game.setTeamBPoints(pointsB);

        // Check win condition
        if (pointsA >= 4 && pointsA - pointsB >= 2) {
            finish(game, true);
        } else if (pointsB >= 4 && pointsB - pointsA >= 2) {
            finish(game, false);
        } else if (pointsA == 3 && pointsB == 3) {
            game.setStatus(GameStatus.DEUCE);
        } else {
            game.setStatus(GameStatus.IN_PROGRESS);
        }
    }

    /**
     * Marks the game as finished and sets the winner.
     *
     * @param game the game to finish
     * @param aWon true if Team A won, false if Team B won
     */
    private void finish(GameDTO game, boolean aWon) {
        game.setStatus(GameStatus.FINISHED);
        game.setWinnerTeam(aWon ? TeamType.TEAM_A : TeamType.TEAM_B);
    }

    /**
     * Returns a human-readable display of the game score.
     *
     * @param game the game to evaluate
     * @return formatted score string
     */
    public String getScoreDisplay(GameDTO game) {
        if (game.getStatus() == GameStatus.FINISHED) {
            return game.getWinnerTeam().name() + " wins";
        }
        if (game.getStatus() == GameStatus.DEUCE) {
            return "Deuce";
        }
        if (game.getStatus() == GameStatus.ADVANTAGE) {
            return "Advantage " + (game.isTeamAHasAdvantage()
                    ? "Team A" : "Team B");
        }
        return String.format("Team A %s - Team B %s",
                LABELS.get(game.getTeamAPoints()),
                LABELS.get(game.getTeamBPoints())
        );
    }
}
