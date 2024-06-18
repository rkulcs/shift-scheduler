package shift.scheduler.backend.util.algorithm;

import java.util.*;

public abstract class GeneticAlgorithm<T, B> {

    public int POPULATION_SIZE = 3000;
    public int TOURNAMENT_SIZE = 50;

    public double CROSSOVER_RATE = 0.8;
    public double MUTATION_RATE = 0.3;

    public int MIN_ITERATIONS = 25;
    public int MAX_ITERATIONS = 500;

    public int MAX_NUM_GENERATED_SCHEDULES = 3;

    public Random random = new Random();

    public class TournamentResult<T> {
        private T participant;
        private long score;

        public TournamentResult(T participant, long score) {
            this.participant = participant;
            this.score = score;
        }

        public T getParticipant() {
            return participant;
        }

        public void setParticipant(T participant) {
            this.participant = participant;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }
    }

    public Collection<T> generateSchedules(List<List<B>> components) {

        Collection<T> generatedSchedules = new ArrayList<>();
        TournamentResult<T> closestSuboptimalSolution = new TournamentResult<>(null, Long.MAX_VALUE);

        List<T> population = generateInitialPopulation(components);

        int numIterations = computeNumIterations(components);

        for (int i = 0; i < numIterations; i++) {
            List<T> nextGeneration = new ArrayList<>();

            while (nextGeneration.size() != POPULATION_SIZE) {
                TournamentResult<T> resultA = performTournament(population);
                T a = resultA.getParticipant();

                if (resultA.getScore() == 0) {
                    generatedSchedules.add(a);

                    if (generatedSchedules.size() == MAX_NUM_GENERATED_SCHEDULES)
                        return generatedSchedules;
                }

                if (resultA.getScore() < closestSuboptimalSolution.getScore())
                    closestSuboptimalSolution = resultA;

                TournamentResult<T> resultB = performTournament(population);
                T b = resultB.getParticipant();

                if (a == b)
                    continue;

                if (resultB.getScore() == 0) {
                    generatedSchedules.add(b);

                    if (generatedSchedules.size() == MAX_NUM_GENERATED_SCHEDULES)
                        return generatedSchedules;
                }

                if (resultB.getScore() < closestSuboptimalSolution.getScore())
                    closestSuboptimalSolution = resultB;

                if (random.nextDouble() <= CROSSOVER_RATE) {
                    List<T> offspring = crossover(a, b);

                    offspring.forEach(o -> {
                        if (random.nextDouble() <= MUTATION_RATE)
                            o = mutate(components, o);

                        nextGeneration.add(o);
                    });
                }
            }

            population = nextGeneration;
        }

        if (generatedSchedules.isEmpty()) {
            generatedSchedules.add(closestSuboptimalSolution.getParticipant());
        }

        return generatedSchedules;
    }

    public TournamentResult<T> performTournament(List<T> schedules) {

        Set<T> participants = new HashSet<>();

        while (participants.size() != TOURNAMENT_SIZE) {
            int j = random.nextInt(schedules.size());
            T schedule = schedules.get(j);

            if (!participants.contains(schedule))
                participants.add(schedule);
        }

        TournamentResult<T> result = new TournamentResult<>(null, Long.MAX_VALUE);

        participants.forEach(participant -> {
            long score = computeFitnessScore(participant);

            if (score < result.getScore()) {
                result.setParticipant(participant);
                result.setScore(score);
            }
        });

        return result;
    }

    private int computeNumIterations(List<List<B>> components) {

        // Compute the number of possible combinations of components
        long numCombinations = 1;

        for (var component : components) {
            numCombinations *= component.size();

            // Detect overflows
            if (numCombinations < 0)
                return MAX_ITERATIONS;
        }

        if (numCombinations < POPULATION_SIZE*MIN_ITERATIONS)
            return MIN_ITERATIONS;
        else if (numCombinations >= POPULATION_SIZE*MAX_ITERATIONS)
            return MAX_ITERATIONS;

        return (int) (numCombinations/POPULATION_SIZE + 3);
    }

    abstract List<T> generateInitialPopulation(List<List<B>> components);
    abstract long computeFitnessScore(T schedule);
    abstract List<T> crossover(T a, T b);
    abstract T mutate(List<List<B>> components, T schedule);
}
