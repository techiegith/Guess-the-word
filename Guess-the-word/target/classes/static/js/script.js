window.onload = function checkIfGameOver() {
    var numberOfTriesRemaining = parseInt(document.getElementById('triesLeftElement').textContent);
    var gameStatus = document.querySelector('.win-message'); // Check if win message is present
    var correctWord = document.querySelector('.correct-word span'); // Fetch the correct word

    // Check if correctWord is found and extract text
    if (correctWord) {
        correctWord = correctWord.textContent.trim(); // Get the correct word
    }

    // If the game is over due to no tries left
    if (numberOfTriesRemaining <= 0 && !gameStatus) {
        // Show the "You Lost" message with the correct word in the alert box
//        alert("Game Over");
         // Show the "You Lost" message with the correct word in the "lose-message" div
                document.querySelector('.lose-message').style.display = 'block';

        // Disable the input and submit buttons when the game is over
        document.getElementById('guessedChar').disabled = true;
        document.getElementById('try').disabled = true;
    }

    // Disable the input and submit buttons if the game is won
    if (gameStatus) {
        document.getElementById('guessedChar').disabled = true;
        document.getElementById('try').disabled = true;
    }
}

function reloadGame() {
    // Reload the game by navigating to the reload URL
    window.location.href = "/reload";  // Modify this based on your actual reload endpoint
}