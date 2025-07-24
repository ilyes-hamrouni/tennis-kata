#!/bin/bash

BASE_URL="http://localhost:8080/api/v1"

echo "🔹 Creating Player 1..."
PLAYER_A_ID=$(curl -s -X POST "$BASE_URL/players" \
  -H "Content-Type: application/json" \
  -d '{"name": "Player A", "rank": 1, "age": 25, "country": "France"}' | jq '.id')

echo "🔹 Creating Player 2..."
PLAYER_B_ID=$(curl -s -X POST "$BASE_URL/players" \
  -H "Content-Type: application/json" \
  -d '{"name": "Player B", "rank": 2, "age": 27, "country": "Spain"}' | jq '.id')

echo "✅ Player A ID = $PLAYER_A_ID, Player B ID = $PLAYER_B_ID"

echo "🎾 Creating Match..."
MATCH_ID=$(curl -s -X POST "$BASE_URL/matches" \
  -H "Content-Type: application/json" \
  -d "{
    \"teamA\": [$PLAYER_A_ID],
    \"teamB\": [$PLAYER_B_ID],
    \"stadium\": \"Roland Garros\"
  }" | jq '.id')

echo "✅ Match ID: $MATCH_ID"

# Loop to create 3 sets
for SET_NUM in {1..3}; do
  echo -e "\n🏁 Creating Set $SET_NUM..."
  SET_ID=$(curl -s -X POST "$BASE_URL/sets/match/$MATCH_ID" | jq '.id')
  echo "📘 Set ID: $SET_ID"

  # Create 6 games in the set
  for GAME_NUM in {1..6}; do
    GAME_ID=$(curl -s -X POST "$BASE_URL/sets/$SET_ID/games" | jq '.id')
    echo "🎮 Created Game $GAME_NUM in Set $SET_NUM with ID: $GAME_ID"

    # Send enough points for quick win by TEAM_B: 4 points in a row
    curl -s -X POST "$BASE_URL/games/$GAME_ID/multiple-points" \
      -H "Content-Type: application/json" \
      -d '[{"teamWinner":"TEAM_B"}, {"teamWinner":"TEAM_B"}, {"teamWinner":"TEAM_B"}, {"teamWinner":"TEAM_B"}]' > /dev/null
  done
done

echo -e "\n📊 Final Match Result:"
curl -s "$BASE_URL/matches/$MATCH_ID" | jq

