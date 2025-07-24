package bforbank.tennis.domains.requests;

import java.util.List;

public record MatchRequest(
        List<Long> teamA,
        List<Long> teamB,
        String stadium
) {}
