package com.mk.bettinggenerator.service;

import com.mk.bettinggenerator.dto.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BettingService {

    public BettingDto generateBets(int games, int hedging) {
        final List<String> results = List.of("1", "X", "2");
        List<Prediction> bets = new ArrayList<>();

        for (int i = 0; i < games; i++) {
            int result = new Random().nextInt(3);

            bets.add(new Prediction(i, results.get(result), null));
        }

        addHedging(bets, hedging, results);

        return new BettingDto(bets);
    }

    public List<BettingDto> generateMultipleCoupons(PixCouponRequest pixCouponRequest) {
        List<BettingDto> gameCoupons = new ArrayList<>();

        pixCouponRequest.getPixs()
                .forEach(pix -> gameCoupons.add(generateBets(pix.getGames(), pix.getHalfGardering())));

        return gameCoupons;
    }

    public List<BettingDto> generateMultipleCoupons(PixRandomCouponRequest pixCouponRequest) {
        List<BettingDto> gameCoupons = new ArrayList<>();

        for (int i = 0; i < pixCouponRequest.getCoupons(); i++) {
            BettingDto bettingDto = generateBets(pixCouponRequest.getGames(), pixCouponRequest.getHalfGardering());
            gameCoupons.add(bettingDto);
        }

        return gameCoupons;
    }

    public List<BettingDto> createBettingCoupons(CouponRequest couponRequest) {
        return couponRequest.getCoupons().stream().map(coupon -> new BettingDto(coupon.getPredictions())).collect(Collectors.toList());
    }

    public List<BetDto> createBettingCoupon(BetPredictionRequest betPredictionRequest) {
        //TODO validate coupon
        validateBetCoupon(betPredictionRequest);
        //TODO map to entity
        //TODO map to DTO

        //todo return DTO
        return betPredictionRequest.getBetDto().stream().map(bet ->
                new BetDto(bet.getGameIndex(), bet.getPredictions())).collect(Collectors.toList());
    }

    private void validateBetCoupon(BetPredictionRequest betPredictionRequest) {
        List<String> validPredictions = List.of("12", "1X", "X2", "1", "X", "2");

        for (BetDto betDto : betPredictionRequest.getBetDto()) {
            if (betDto.getPredictions().stream().noneMatch(validPredictions::contains)) {
                throw new IllegalArgumentException("Invalid predictions found in the bet coupon");
            }
        }

    }


    private void addHedging(List<Prediction> bets, int hedging, List<String> possiblePredictions) {

        //generate garderingar
        List<Integer> gamesToBeHedged = generateHedgingGames(bets.size(), hedging);
        // Adding extra predictions randomly based on the gardering
        for (int i = 0; i < gamesToBeHedged.size(); i++) {
            int matchIndex = gamesToBeHedged.get(i);
            Prediction existingPrediction = bets.get(matchIndex);

            existingPrediction.randomizeGardering(possiblePredictions);

            bets.set(matchIndex, existingPrediction);
        }
    }

    private List<Integer> generateHedgingGames(int games, int hedging) {
        List<Integer> matches = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < hedging; i++) {

            int matchNumber;

            do {
                matchNumber = random.nextInt(games);

            } while (matches.contains(matchNumber));
            matches.add(matchNumber);
        }

        return matches;
    }

    public List<BetCoupon> generateMultipleCustomPixCoupons(MultiplePixRequest request) {
        List<BetCoupon> betCoupons = new ArrayList<>();
        int couponAmount = request.getCouponAmount();

        for (int i = 0; i < couponAmount; i++) {
            List<PixPrediction> predictions = new ArrayList<>();
            List<Integer> hedgedMatches;

            //List<GamePredictionDto> gamePredictionsToBeModified = createCopy(request.getGamePredictions());
            hedgedMatches = matchesToBeHedged(request);


            for (GamePredictionDto game : request.getGamePredictions()) {
                PixPrediction pixPrediction = new PixPrediction(game.getGameIndex(), game.getGameName(), generateHedging(hedgedMatches, game));
                predictions.add(pixPrediction);
            }

            BetCoupon betCoupon = new BetCoupon("Coupon " + (i + 1), predictions);
            betCoupons.add(betCoupon);

        }

        return betCoupons;
    }

    private List<GamePredictionDto> createCopy(List<GamePredictionDto> originalList) {
        List<GamePredictionDto> deepCopy = new ArrayList<>();

        for (GamePredictionDto original : originalList) {
            GamePredictionDto copy = new GamePredictionDto(original.getGameIndex(),
                    original.getGameName(),
                    new PredictBet(original.getPredictions().getResults().stream()
                            .map(predictionResult -> new PredictionResult(new String(predictionResult.getResult().intern()), predictionResult.getPercentage()))
                            .toList()));
            deepCopy.add(copy);
        }

        return deepCopy;
    }

    private List<String> generateHedging(List<Integer> hedgeGameIndexes, GamePredictionDto gamePrediction) {

        if (hedgeGameIndexes.contains(gamePrediction.getGameIndex())) {
            return hedgeGame(gamePrediction.getPredictions().getResults());
        } else {
            return List.of(generatePrediction(gamePrediction.getPredictions()));
        }



 /*       if (!hedgeGameIndexes.contains(gamePrediction.getGameIndex()) && gamePrediction.getPredictions().getResults().size() > 1) {
            return List.of(generatePrediction(gamePrediction.getPredictions()));
        } else {
            if (gamePrediction.getPredictions().getResults().size() > 2) {

                removePrediction(gamePrediction.getPredictions());
                predictions = (gamePrediction.getPredictions().getResults().stream().map(PredictionResult::getResult).toList());

                return predictions;
            } else {
                return gamePrediction.getPredictions().getResults().stream().map(PredictionResult::getResult).toList();
            }
        }*/
    }

    private List<String> hedgeGame(List<PredictionResult> results) {
        SecureRandom random = new SecureRandom(); //TODO use secure random
        if (results.size() == 3) {
            int indexToExclude = random.nextInt(results.size());
            List<String> newResults = new ArrayList<>(results.stream().map(PredictionResult::getResult).toList());
            newResults.remove(indexToExclude);
            return newResults;
        } else {
            return results.stream().map(PredictionResult::getResult).toList();
        }
    }

    private void removePrediction(PredictBet predictBet) {
        //TODO add randomizer to 100 since the percentage weighing need to accounted for
        SecureRandom random = new SecureRandom();
        int indexToBeRemoved = random.nextInt(predictBet.getResults().size());
        predictBet.getResults().remove(indexToBeRemoved);
    }

    private String generatePrediction(PredictBet predictBet) {
        //TODO add weight of percentage for each result
        List<String> resultOutcomes = calculateBetPercentage(predictBet);

        Random random = new Random();

        //TODO should maybe shuffle the list here?
        return resultOutcomes.get(random.nextInt(resultOutcomes.size()));
    }

    private List<String> calculateBetPercentage(PredictBet predictBet) {
        int percentageAmount = predictBet.getResults().stream()
                .map(PredictionResult::getPercentage)
                .reduce(0, Integer::sum);

        var amount = 0;
        if (percentageAmount > 100 || (percentageAmount < 100 && percentageAmount > 0)) {
            throw new IllegalArgumentException(); //TODO change this later!
        } else if (percentageAmount == 0) {
            List<String> predictions = new ArrayList<>(predictBet.getResults().stream().map(PredictionResult::getResult).toList());
            amount = predictions.size() == 2 ? 50 : 33;
        } else {
            amount = -1;
        }

        return calculatePercentageResult(amount, predictBet);

    }

    private List<String> calculatePercentageResult(int n, PredictBet predictBet) {
        return predictBet.getResults().stream()
                .map(endResult -> Collections.nCopies(n == -1 ? endResult.getPercentage() : n, endResult.getResult()))
                .flatMap(List::stream)
                .toList();
    }

    private List<Integer> matchesToBeHedged(MultiplePixRequest request) {
        SecureRandom random = new SecureRandom(); //TODO maybe check out another random

        Set<Integer> matchesToStayHedged = new HashSet<>();
        while (matchesToStayHedged.size() < request.getGamesToHedge()) {
            int matchIndex = random.nextInt(request.getGamePredictions().size());
            if (!matchesToStayHedged.contains(matchIndex) && request.getGamePredictions().get(matchIndex).getPredictions().getResults().size() > 1) {
                matchesToStayHedged.add(matchIndex);
            }
        }
        return new ArrayList<>(matchesToStayHedged);
    }

    public FullHitDto createFullHitPrediction(FullHitPredictionRequest fullHitPrediction) {
        //TODO note that it's always 13 games
        //TODO generate a fullhit coupon
        //TODO create response object.

        //create the basic coupon
        int gameIndex = 0;

        //add extra rows

        List<FullHitGame> gamePredictions = fullHitPrediction.getPercentagesInGames().stream()
                        .map(game -> new FullHitGame(game.getGameIndex(), createBaseCoupon(fullHitPrediction))).toList();

        return new FullHitDto(gamePredictions);
    }



    private String createBaseCoupon(FullHitPredictionRequest fullHitPrediction) {
        //TODO random prediction based on the prediction percentages
        List<String> fullPercentageList = new ArrayList<>();
        for (int i = 0; i < fullHitPrediction.getPercentagesInGames().size(); i++) {
            fullPercentageList.addAll(calculateFullHitPercentage(fullHitPrediction.getPercentagesInGames().get(i)));
        }

        SecureRandom random = new SecureRandom();

        return fullPercentageList.get(random.nextInt(fullPercentageList.size()));
    }

    private List<String> calculateFullHitPercentage(FullHitGameDto predictBet) {
        int percentageAmount = predictBet.getGoalPercentages().stream()
                .map(FullHitGamePrediction::getPercentage)
                .reduce(0, Integer::sum);

        if (percentageAmount != 100) {
            throw new IllegalArgumentException(); //TODO change this later!
        }

        return calculateFullHitPercentageResult(predictBet.getGoalPercentages());

    }

    private List<String> calculateFullHitPercentageResult(List<FullHitGamePrediction> predictions) {
        return predictions.stream()
                .flatMap(prediction -> Collections.nCopies(prediction.getPercentage(), prediction.getResult()).stream())
                .collect(Collectors.toList());
    }
}
