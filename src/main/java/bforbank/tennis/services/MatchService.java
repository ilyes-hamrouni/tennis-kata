package bforbank.tennis.services;

import bforbank.tennis.domains.dtos.MatchDTO;
import bforbank.tennis.domains.entities.MatchEntity;
import bforbank.tennis.domains.entities.PlayerEntity;
import bforbank.tennis.domains.enums.GameStatus;
import bforbank.tennis.domains.requests.MatchRequest;
import bforbank.tennis.exceptions.TennisException;
import bforbank.tennis.mappers.MatchMapper;
import bforbank.tennis.repositories.MatchRepository;
import bforbank.tennis.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static bforbank.tennis.exceptions.TennisErrorConstants.ERROR_AT_LEAST_ONE_PLAYER_PER_TEAM_REQUIRED;
import static bforbank.tennis.exceptions.TennisErrorConstants.ERROR_PLAYER_NOT_FOUND;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final MatchMapper matchMapper;

    public MatchService(MatchRepository matchRepository, PlayerRepository playerRepository, MatchMapper matchMapper ) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.matchMapper = matchMapper;
    }

    public MatchDTO createMatch(MatchRequest matchRequest) {
        if (matchRequest.teamA() == null || matchRequest.teamA().isEmpty() ||
                matchRequest.teamB() == null || matchRequest.teamB().isEmpty()) {
            throw new TennisException(400, ERROR_AT_LEAST_ONE_PLAYER_PER_TEAM_REQUIRED);
        }

        List<PlayerEntity> teamAEntities = playerRepository.findAllById(matchRequest.teamA());
        List<PlayerEntity> teamBEntities = playerRepository.findAllById(matchRequest.teamB());

        if (teamAEntities.size() != matchRequest.teamA().size() || teamBEntities.size() != matchRequest.teamB().size()) {
            throw new TennisException(404, ERROR_PLAYER_NOT_FOUND);
        }

        MatchEntity match = MatchEntity.builder()
                        .teamA(teamAEntities)
                        .teamB(teamBEntities)
                        .stadium(matchRequest.stadium())
                        .status(GameStatus.IN_PROGRESS)
                        .build();

        return matchMapper.toDTO(matchRepository.save(match));
    }



    public MatchDTO getMatch(Long id) {
        MatchEntity match = matchRepository.findById(id)
                .orElseThrow(() -> new TennisException(404, "Match not found"));
        return matchMapper.toDTO(match);
    }

    public List<MatchDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }
}
