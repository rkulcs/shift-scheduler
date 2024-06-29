package shift.scheduler.backend.util.algorithm;

import shift.scheduler.backend.model.schedule.Schedule;

import java.util.*;

public abstract class GeneticAlgorithm<B> {

    public int POPULATION_SIZE = 3000;
    public int TOURNAMENT_SIZE = 50;

    public double CROSSOVER_RATE = 0.8;
    public double MUTATION_RATE = 0.3;

    public int MIN_ITERATIONS = 25;
    public int MAX_ITERATIONS = 500;

    public int MAX_NUM_GENERATED_SCHEDULES = 3;

    public Random random = new Random();

    public class TournamentResult {
        private Schedule participant;
        private long score;

        public TournamentResult(Schedule participant, long score) {
            this.participant = participant;
            this.score = score;
        }

        public Schedule getParticipant() {
            return participant;
        }

        public void setParticipant(Schedule participant) {
            this.participant = participant;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }
    }

    public Collection<Schedule> generateSchedules(List<List<B>> components) {

        Collection<Schedule> generatedSchedules = new ArrayList<>();
        TournamentResult closestSuboptimalSolution = new TournamentResult(null, Long.MAX_VALUE);

        List<Schedule> population = generateInitialPopulation(components);

        int numIterations = computeNumIterations(components);

        for (int i = 0; i < numIterations; i++) {
            List<Schedule> nextGeneration = new ArrayList<>();

            while (nextGeneration.size() != POPULATION_SIZE) {
                TournamentResult resultA = performTournament(population);
                Schedule a = resultA.getParticipant();

                if (resultA.getScore() == 0) {
                    generatedSchedules.add(a);

                    if (generatedSchedules.size() == MAX_NUM_GENERATED_SCHEDULES)
                        return generatedSchedules;
                }

                if (resultA.getScore() < closestSuboptimalSolution.getScore())
                    closestSuboptimalSolution = resultA;

                TournamentResult resultB = performTournament(population);
                Schedule b = resultB.getParticipant();

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
                    List<Schedule> offspring = crossover(a, b);

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

    public TournamentResult performTournament(List<Schedule> schedules) {

        Set<Schedule> participants = new HashSet<>();

        while (participants.size() != TOURNAMENT_SIZE) {
            int j = random.nextInt(schedules.size());
            Schedule schedule = schedules.get(j);

            if (!participants.contains(schedule))
                participants.add(schedule);
        }

        TournamentResult result = new TournamentResult(null, Long.MAX_VALUE);

        participants.forEach(participant -> {
            long score = computeFitnessScore(participant);

            if (score < result.getScore()) {
                result.setParticipant(participant);
                result.setScore(score);
            }
        });

        return result;
    }

    private long computeFitnessScore(Schedule schedule) {

        boolean isValid = schedule.validate();

        long score = 0;

        if (isValid)
            return score;

        score = schedule.getConstraintViolations()
                .stream()
                .reduce(0, (subtotal, constraint) -> subtotal + Math.abs(constraint.getDifference()), Integer::sum);

        return score;
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

    abstract List<Schedule> generateInitialPopulation(List<List<B>> components);
    abstract List<Schedule> crossover(Schedule schedA, Schedule schedB);
    abstract Schedule mutate(List<List<B>> components, Schedule schedule);
}
