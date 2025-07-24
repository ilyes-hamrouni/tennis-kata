#!/bin/bash

BASE_URL="http://localhost:8080/api/v1"
HEADERS=(-H "Content-Type: application/json")

# Helpers
create_player() {
  curl -s "${HEADERS[@]}" -X POST "$BASE_URL/players" -d "{
    \"name\": \"$1\",
    \"rank\": $2,
    \"age\": $3,
    \"country\": \"$4\"
  }"
}

create_game() {
  local SET_ID=$1
  GAME_ID=$(curl -s -X POST "$BASE_URL/sets/$SET_ID/games" | jq -r '.id')
  echo "$GAME_ID"
}

record_game_win() {
  local GAME_ID=$1
  local WINNER=$2
  curl -s "${HEADERS[@]}" -X POST "$BASE_URL/games/$GAME_ID/multiple-points" -d "[
    {\"teamWinner\": \"$WINNER\"},
    {\"teamWinner\": \"$WINNER\"},
    {\"teamWinner\": \"$WINNER\"},
    {\"teamWinner\": \"$WINNER\"}
  ]" > /dev/null
}

# 1–2: Create Players
PLAYER_A=$(create_player "Player A" 1 25 "France")
PLAYER_B=$(create_player "Player B" 2 27 "Spain")
ID_A=$(echo "$PLAYER_A" | jq -r '.id')
ID_B=$(echo "$PLAYER_B" | jq -r '.id')

# 3: Create Match
MATCH=$(curl -s "${HEADERS[@]}" -X POST "$BASE_URL/matches" -d "{
  \"teamA\": [$ID_A],
  \"teamB\": [$ID_B],
  \"stadium\": \"Arthur Ashe Stadium\"
}")
MATCH_ID=$(echo "$MATCH" | jq -r '.id')

# 4: Create Set 1 (Tie-break)
SET1=$(curl -s -X POST "$BASE_URL/sets/match/$MATCH_ID")
SET1_ID=$(echo "$SET1" | jq -r '.id')

# Alternate 6 games each: A-B-A-B-A-B
# Game 1 – Deuce (TEAM_A wins)

GID=$(create_game $SET1_ID)

curl -s -X POST "$BASE_URL/games/$GID/multiple-points" \
  -H "Content-Type: application/json" \
  -d @- <<EOF
[
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_B"},
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_B"},
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_B"},
  {"teamWinner": "TEAM_B"},
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_B"},
  {"teamWinner": "TEAM_A"},
  {"teamWinner": "TEAM_A"}
]
EOF



# Remaining 5 games for A and 6 for B (alternating)
for i in {1..5}; do
  GID=$(create_game $SET1_ID)
  record_game_win $GID "TEAM_A"
  GID=$(create_game $SET1_ID)
  record_game_win $GID "TEAM_B"
done

# Tie-break game (7th): TEAM_B wins it
GID=$(create_game $SET1_ID)
record_game_win $GID "TEAM_B"

# 5: Create Set 2 (A wins 6–4)
SET2=$(curl -s -X POST "$BASE_URL/sets/match/$MATCH_ID")
SET2_ID=$(echo "$SET2" | jq -r '.id')
for i in {1..6}; do GID=$(create_game $SET2_ID); record_game_win $GID "TEAM_A"; done
for i in {1..4}; do GID=$(create_game $SET2_ID); record_game_win $GID "TEAM_B"; done

# 6: Create Set 3 (B wins 6–4)
SET3=$(curl -s -X POST "$BASE_URL/sets/match/$MATCH_ID")
SET3_ID=$(echo "$SET3" | jq -r '.id')
for i in {1..6}; do GID=$(create_game $SET3_ID); record_game_win $GID "TEAM_B"; done
for i in {1..4}; do GID=$(create_game $SET3_ID); record_game_win $GID "TEAM_A"; done

# Final Result
echo -e "\n🎾 Final Match Summary:"
curl -s "$BASE_URL/matches/$MATCH_ID" | jq

