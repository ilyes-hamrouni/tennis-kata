package bforbank.tennis.exceptions;


public class TennisErrorConstants {

        public static final String ERROR_PLAYER_NOT_FOUND = "PLAYER_NOT_FOUND";
        public static final String ERROR_PLAYER_NAME_REQUIRED = "PLAYER_NAME_REQUIRED";
        public static final String ERROR_MSG_PLAYER_NOT_FOUND = "Player with ID %d not found.";
        public static final String ERROR_MSG_GAME_ALREADY_FINISHED = "Cannot update a finished game.";
        public static final String ERROR_SET_NOT_FOUND = "Cannot find set ";
        public static final String ERROR_MATCH_NOT_FOUND = "Cannot find match ";
        public static final String ERROR_CREATE_GAME_SET_IS_FINISHED = "Cannot create a game in a finished set";

        public static final String ERROR_AT_LEAST_ONE_PLAYER_PER_TEAM_REQUIRED = "At least one player per team allowed.";

        public static final String ERROR_GAME_NOT_FOUND = "GAME NOT FOUND";
        private TennisErrorConstants() {}

}
