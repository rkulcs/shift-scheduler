package shift.scheduler.backend.util.algorithm;

import java.util.*;

public abstract class GeneticAlgorithm<T, B> {

    public int POPULATION_SIZE = 3000;
    public int TOURNAMENT_SIZE = 50;

    public double CROSSOVER_RATE = 0.8;
    public double MUTATION_RATE = 0.3;

    public int MIN_ITERATIONS = 50;
    public int MAX_ITERATIONS = Integer.MAX_VALUE;

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

        // TODO: Change this depending on the number of daily schedules
        int numIterations = MIN_ITERATIONS;

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

    abstract List<T> generateInitialPopulation(List<List<B>> components);
    abstract long computeFitnessScore(T schedule);
    abstract List<T> crossover(T a, T b);
    abstract T mutate(List<List<B>> components, T schedule);
}
