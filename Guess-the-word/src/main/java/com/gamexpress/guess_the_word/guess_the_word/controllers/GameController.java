package com.gamexpress.guess_the_word.guess_the_word.controllers;

import com.gamexpress.guess_the_word.guess_the_word.service.GameService;
import com.gamexpress.guess_the_word.guess_the_word.utils.GameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameUtils gameUtils;

    @GetMapping("/")
    public String redirectToGameHome() {
        return "redirect:/game-home";
    }

    // Game Home Page
    @GetMapping("/game-home")
    public String showGameHomePage(@RequestParam(value = "guessedChar", required = false) String guessedChar, Model model) {

        System.out.println("Captured guessed word is: " + guessedChar);

        String[] randomWords = gameService.getRandomWords();
        model.addAttribute("randomWords", randomWords);

        String randomWord = gameService.toString();

        // Initialize the error message to null
        String errorMessage = null;

        if (guessedChar != null) {
            // Trim the input to avoid leading/trailing spaces
            guessedChar = guessedChar.trim();

            // Use GameService's validateInput to check the input
            errorMessage = gameService.validateInput(guessedChar);

            if (errorMessage == null) {
                // If the input is valid, process the guess
                boolean isGuessCorrect = gameService.addGuess(Character.toLowerCase(guessedChar.charAt(0)));
                randomWord = gameService.toString();

                if (!isGuessCorrect) {
                    gameUtils.reduceTry();
                }

                // Check if the word is completely guessed
                if (randomWord.replace(" ", "").equals(gameService.getRandomlyChoosenWord())) {
                    model.addAttribute("gameStatus", "You Won!");
                    model.addAttribute("isGameWon", true); // For visual representation
                }
            }
        }

        // Check if the user has lost the game
        if (gameUtils.getTriesRemaining() <= 0) {
            model.addAttribute("gameStatus", "You Lost!");
            model.addAttribute("correctWord", gameService.getRandomlyChoosenWord()); // Pass the correct word
            model.addAttribute("isGameOver", true); // To check in the frontend
        }

        // Update Model Attributes
        System.out.println("Number of tries remaining: " + gameUtils.getTriesRemaining());
        model.addAttribute("wordToDisplay", randomWord);
        model.addAttribute("triesLeft", gameUtils.getTriesRemaining());
        model.addAttribute("errorMessage", errorMessage); // Pass error message if present

        return "game-home-page";
    }

    // Reload Game and Reset State
    @GetMapping("/reload")
    public String reloadGame() {
        gameService = gameUtils.reload(); // Reset the game state
        gameUtils.resetTries(); // Reset tries count
        return "redirect:/game-home";
    }
}
