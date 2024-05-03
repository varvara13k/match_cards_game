package com.example.match_cards_game.models

import com.example.match_cards_game.IMAGES

class Game(private val boardSize: Board) {


    val cards: List<Card>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = IMAGES.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { Card(it) }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card: Card = cards[position]
        //three cases:
        // 0 cards previosly flipped over  =>  restore cards + flip over the selected cars
        // 1 cards previously flipped over =>  flip over the selected card + check if the images match
        // 2 cards previously flipped over =>  restore cards + flip over the selected cars
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 card flipped over previously
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // 1 card previously flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch

    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card: Card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }

        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFacedUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}