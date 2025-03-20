import java.util.*;

public class BettingGenerator {
    public BettingDto generateBets(int matches, int gardering) {
        final List<String> results = List.of("1", "X", "2");
        Map<Integer, String> bets = new HashMap<>();
        Random random = new Random();

        for (int i = 0; i < matches; i++) {
            int result = random.nextInt(3);
            bets.put(i, results.get(result));
        }

        addGardering(bets, matches, gardering, results);

        return new BettingDto(bets);
    }

    private void addGardering(Map<Integer, String> bets, int matches, int gardering, List<String> results) {
        Random random = new Random();
        for (int i = 0; i < gardering; i++) {
            int matchIndex = random.nextInt(matches);
            String existingPrediction = bets.get(matchIndex);
            String newPrediction;
            do {
                int resultIndex = random.nextInt(3);
                newPrediction = results.get(resultIndex);
            } while (existingPrediction.contains(newPrediction));
            bets.put(matchIndex, existingPrediction + newPrediction);
        }
    }

    public List<BettingDto> generateMultipleCoupons(PixRandomCouponRequest pixCouponRequest) {
        List<BettingDto> gameCoupons = new ArrayList<>();
        final List<String> results = List.of("1", "X", "2"); // Define results list here

        for (int i = 0; i < pixCouponRequest.getCoupons(); i++) {
            BettingDto bettingDto = generateBets(pixCouponRequest.getGames(), pixCouponRequest.getHalfGardering());
            gameCoupons.add(sortPredictions(bettingDto, results)); // Pass results to sortPredictions
        }

        return gameCoupons;
    }

    private BettingDto sortPredictions(BettingDto bettingDto, List<String> results) {
        Map<Integer, String> sortedBets = new HashMap<>();
        for (Map.Entry<Integer, String> entry : bettingDto.getPredictions().entrySet()) {
            int matchIndex = entry.getKey();
            String prediction = entry.getValue();
            String[] predictions = prediction.split("");
            Arrays.sort(predictions, Comparator.comparingInt(result -> results.indexOf(result)));
            String sortedPrediction = String.join("", predictions);
            sortedBets.put(matchIndex, sortedPrediction);
        }
        return new BettingDto(sortedBets);
    }

    static class BettingDto {
        Map<Integer, String> predictions;

        public BettingDto(Map<Integer, String> predictions) {
            this.predictions = predictions;
        }

        public Map<Integer, String> getPredictions() {
            return predictions;
        }
    }

    static class PixRandomCouponRequest {
        private int games;
        private int halfGardering;
        private int coupons;

        public PixRandomCouponRequest(int games, int halfGardering, int coupons) {
            this.games = games;
            this.halfGardering = halfGardering;
            this.coupons = coupons;
        }

        public int getGames() {
            return games;
        }

        public int getHalfGardering() {
            return halfGardering;
        }

        public int getCoupons() {
            return coupons;
        }
    }

    public static void main(String[] args) {
        BettingGenerator generator = new BettingGenerator();
        PixRandomCouponRequest pixCouponRequest = new PixRandomCouponRequest(15, 4, 5);
        List<BettingDto> gameCoupons = generator.generateMultipleCoupons(pixCouponRequest);
        for (BettingDto bettingDto : gameCoupons) {
            System.out.println("Original predictions: " + bettingDto.predictions);
            System.out.println("Sorted predictions: " + bettingDto.predictions);
        }
    }
}
