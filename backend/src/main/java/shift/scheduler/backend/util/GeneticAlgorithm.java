package shift.scheduler.backend.util;

import shift.scheduler.backend.model.ScheduleForWeek;

import java.util.*;

public interface GeneticAlgorithm<T, B> {

    static final int POPULATION_SIZE = 3000;
    static final int TOURNAMENT_SIZE = 50;

    static final double CROSSOVER_RATE = 0.8;
    static final double MUTATION_RATE = 0.3;

    static final int MIN_ITERATIONS = 50;
    static final int MAX_ITERATIONS = Integer.MAX_VALUE;

    static final int MAX_NUM_GENERATED_SCHEDULES = 3;

    static final Random random = new Random();

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

    default Collection<T> generateSchedules(List<List<B>> components) {

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

                if (resultA.getScore() == 0)
                    generatedSchedules.add(a);

                if (resultA.getScore() < closestSuboptimalSolution.getScore())
                    closestSuboptimalSolution = resultA;

                TournamentResult<T> resultB = performTournament(population);
                T b = resultB.getParticipant();

                if (a == b)
                    continue;

                if (resultB.getScore() == 0)
                    generatedSchedules.add(b);

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

    default TournamentResult<T> performTournament(List<T> schedules) {

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

//    public Collection<T> generateSchedules(List<List<B>> components);
    List<T> generateInitialPopulation(List<List<B>> components);
    long computeFitnessScore(T schedule);
    List<T> crossover(T a, T b);
    T mutate(List<List<B>> components, T schedule);
}
