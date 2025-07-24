package bforbank.tennis.domains.dtos;

import bforbank.tennis.exceptions.TennisErrorConstants;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.enums.TeamType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScoringStrategy {
    private static final Map<Integer, String> LABELS = Map.of(
            0, "0", 1, "15", 2, "30", 3, "40"
    );

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

        if (winner == TeamType.TEAM_A) {
            pointsA++;
        } else {
            pointsB++;
        }
        game.setTeamAPoints(pointsA);
        game.setTeamBPoints(pointsB);

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

    private void finish(GameDTO game, boolean aWon) {
        game.setStatus(GameStatus.FINISHED);
        game.setWinnerTeam(aWon ? TeamType.TEAM_A : TeamType.TEAM_B);
    }

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
