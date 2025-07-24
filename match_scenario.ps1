$baseUrl = "http://localhost:8080/api/v1"

Write-Host "🔹 Creating Player A..."
$playerAResponse = Invoke-RestMethod -Uri "$baseUrl/players" -Method Post -ContentType "application/json" -Body (@{
    name = "Player A"
    rank = 1
    age = 25
    country = "France"
} | ConvertTo-Json -Depth 3)
$playerAId = $playerAResponse.id

Write-Host "🔹 Creating Player B..."
$playerBResponse = Invoke-RestMethod -Uri "$baseUrl/players" -Method Post -ContentType "application/json" -Body (@{
    name = "Player B"
    rank = 2
    age = 27
    country = "Spain"
} | ConvertTo-Json -Depth 3)
$playerBId = $playerBResponse.id

Write-Host "✅ Player A ID: $playerAId, Player B ID: $playerBId"

Write-Host "🎾 Creating Match..."
$matchResponse = Invoke-RestMethod -Uri "$baseUrl/matches" -Method Post -ContentType "application/json" -Body (@{
    teamA = @($playerAId)
    teamB = @($playerBId)
    stadium = "Roland Garros"
} | ConvertTo-Json -Depth 3)
$matchId = $matchResponse.id

Write-Host "✅ Match ID: $matchId"

for ($setNum = 1; $setNum -le 3; $setNum++) {
    Write-Host "`n🏁 Creating Set $setNum..."
    $setResponse = Invoke-RestMethod -Uri "$baseUrl/sets/match/$matchId" -Method Post
    $setId = $setResponse.id
    Write-Host "📘 Set ID: $setId"

    for ($gameNum = 1; $gameNum -le 6; $gameNum++) {
        $gameResponse = Invoke-RestMethod -Uri "$baseUrl/sets/$setId/games" -Method Post
        $gameId = $gameResponse.id
        Write-Host "🎮 Game $gameNum in Set $setNum created with ID: $gameId"

        $points = @(
            @{ teamWinner = "TEAM_B" },
            @{ teamWinner = "TEAM_B" },
            @{ teamWinner = "TEAM_B" },
            @{ teamWinner = "TEAM_B" }
        )

        Invoke-RestMethod -Uri "$baseUrl/games/$gameId/multiple-points" `
            -Method Post -ContentType "application/json" `
            -Body ($points | ConvertTo-Json -Depth 3) | Out-Null
    }
}

Write-Host "`n📊 Final Match Result:"
Invoke-RestMethod -Uri "$baseUrl/matches/$matchId" -Method Get | ConvertTo-Json -Depth 5
