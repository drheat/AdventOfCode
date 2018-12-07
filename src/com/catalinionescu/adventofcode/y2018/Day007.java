package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.catalinionescu.adventofcode.common.Log;

public class Day007 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/7/input";
    public static String INPUT_FILE = "inputs/input007.txt";

    private static int WORKFORCE_SIZE = 5;

    class Step implements Comparable<Step> {
        private final char name;
        private Set<Step> requirements = new TreeSet<>();
        private boolean completed = false;
        private boolean undergoing = false;

        public Step(final char name) {
            this.name = name;
        }

        /**
         * Returns the name of this step.
         * 
         * @return
         */
        public char getName() {
            return name;
        }

        /**
         * Adds a new required step to this one.
         * 
         * @param step Step to add
         */
        public void addRequirement(Step step) {
            requirements.add(step);
        }

        /**
         * Returns true if this step has no requirements, or all it's required steps have been already completed.
         * 
         * @return
         */
        public boolean isAvailable() {
            if (requirements.isEmpty()) {
                return true;
            }

            for (Step step : requirements) {
                if (!step.isCompleted()) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Returns true if this step has been completed.
         * 
         * @return
         */
        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted() {
            completed = true;
            undergoing = false;
        }

        public void resetCompleted() {
            completed = false;
        }

        public void setUndergoing() {
            undergoing = true;
        }

        /**
         * Returns true if this step is currently being assigned to a worker.
         * 
         * @return
         */
        public boolean isUndergoing() {
            return undergoing;
        }

        /**
         * Returns the next step that needs to be completed, in alphabetical order.
         * 
         * @return Step that needs to be completed
         */
        public Step getNextStep() {
            for (Step step : requirements) {
                if (!step.isAvailable()) {
                    return step;
                }
            }

            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Step)) {
                return false;
            }

            return ((Step) o).name == name;
        }

        @Override
        public int hashCode() {
            return name;
        }

        @Override
        public int compareTo(Step o) {
            return name - o.name;
        }
    }

    interface Callback {
        void onWorkDone();
    }

    /**
     * A single lonely worker Elf.
     * 
     * @author Catalin Ionescu
     *
     */
    class Worker {
        private int workRemaining = 0;
        private Step step;
        private Callback callback;

        /**
         * Assigns a work unit to this worker.
         * 
         * @param step Step to be worked on
         * @param callback Callback to be called when the work is done. Can be null.
         */
        public void setWorking(Step step, Callback callback) {
            workRemaining = step.getName() - 'A' + 1 + 60;
            this.step = step;
            this.callback = callback;
            step.setUndergoing();
        }

        /**
         * Advances time by 1 second.
         */
        public void timeTick() {
            if (workRemaining > 0) {
                workRemaining--;
            }
            if (workRemaining == 0) {
                if (step != null) {
                    step.setCompleted();
                }
                if (callback != null) {
                    callback.onWorkDone();
                    callback = null;
                }
            }
        }

        /**
         * Returns true if this worker is available, false otherwise.
         * 
         * @return
         */
        public boolean isAvailable() {
            return workRemaining == 0;
        }

        /**
         * Returns the work this Elf is currently performing, or '.' if it's idle.
         * 
         * @return
         */
        public char getWork() {
            return workRemaining > 0 ? step.getName() : '.';
        }
    }

    /**
     * Manager for a pool of workers.
     * 
     * @author Catalin Ionescu
     *
     */
    class Dispatcher {
        private Collection<Worker> workers = new ArrayList<>();
        private Set<Step> workToDo = new HashSet<>();

        /**
         * Creates a dispatcher that distributes work to available workers.
         * 
         * @param numWorkers Number of workers in the pool
         */
        public Dispatcher(final int numWorkers) {
            for (int i = 0; i < numWorkers; i++) {
                workers.add(new Worker());
            }
        }

        /**
         * Queues up some work for processing. This doesn't actually begin any work, just adds it to the queue.
         * 
         * @param step Step to be added to the queue
         */
        public void addWork(Step step) {
            step.setUndergoing();
            workToDo.add(step);
        }

        /**
         * Returns true if at least one worker is available to take on work.
         * 
         * @return
         */
        public boolean isWorkerAvailable() {
            return getAvailableWorker() != null;
        }

        /**
         * Returns the first available worker, if there's one. If no workers are available, returns null.
         * 
         * @return
         */
        public Worker getAvailableWorker() {
            for (Worker worker : workers) {
                if (worker.isAvailable()) {
                    return worker;
                }
            }

            return null;
        }

        /**
         * Processes the queued up work, assigning it to any available worker.
         */
        public void processQueuedWork() {
            if (!workToDo.isEmpty()) {
                Iterator<Step> iter = workToDo.iterator();
                Worker worker = getAvailableWorker();
                while (worker != null && iter.hasNext()) {
                    Step step = iter.next();
                    worker.setWorking(step, null);
                    iter.remove();
                    worker = getAvailableWorker();
                }
            }
        }

        /**
         * Advances time by 1 second for all workers in the managed pool.
         */
        public void timeTick() {
            for (Worker worker : workers) {
                worker.timeTick();
            }
        }

        /**
         * Returns true if all assigned work has been performed and all workers are currently idle.
         * 
         * @return
         */
        public boolean isAllWorkDone() {
            if (!workToDo.isEmpty()) {
                return false;
            }

            for (Worker worker : workers) {
                if (!worker.isAvailable()) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Worker worker : workers) {
                if (first) {
                    first = false;
                }
                sb.append("  ");
                sb.append(worker.getWork());
            }

            return sb.toString();
        }
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        Map<Character, Step> steps = new TreeMap<>();
        for (String line : input) {
            // Step G must be finished before step W can begin.
            String[] parts = line.split(" ");
            char req = parts[1].charAt(0);
            char next = parts[7].charAt(0);

            steps.putIfAbsent(req, new Step(req));
            steps.putIfAbsent(next, new Step(next));

            steps.get(next).addRequirement(steps.get(req));
        }

        // ********* Part 1
        final StringBuilder sb = new StringBuilder();
        boolean allDone = false;
        while (!allDone) {
            allDone = true;
            for (Step step : steps.values()) {
                if (step.isCompleted()) {
                    continue;
                }

                if (step.isAvailable()) {
                    sb.append(step.getName());
                    step.setCompleted();
                    allDone = false;
                    break;
                }
            }
        }

        Log.logInfoMessage("Order of steps is: %s", sb.toString());

        // ********* Part 2
        for (Step step : steps.values()) {
            step.resetCompleted();
        }

        Dispatcher dispatcher = new Dispatcher(WORKFORCE_SIZE);
        int timeElapsed = 0;
        allDone = false;

        sb.setLength(0);

        while (!allDone || !dispatcher.isAllWorkDone()) {
            allDone = true;

            // Add all available steps to the dispatcher (producer side)
            for (final Step step : steps.values()) {
                if (step.isCompleted()) {
                    continue;
                }

                if (step.isAvailable() && !step.isUndergoing()) {
                    allDone = false;
                    dispatcher.addWork(step);
                }
            }

            // Work on the added steps (consumer side)
            if (!dispatcher.isAllWorkDone()) {
                dispatcher.processQueuedWork();
                // Log.logInfoMessage("%02d %s %s", timeElapsed, dispatcher.toString(), sb.toString());
                timeElapsed++;
                dispatcher.timeTick();
            }

            // More work to do in future?
            if (allDone) {
                for (Step step : steps.values()) {
                    if (!step.isCompleted()) {
                        allDone = false;
                        break;
                    }
                }
            }
        }

        // Log.logInfoMessage("%02d %s %s", timeElapsed, dispatcher.toString(), sb.toString());
        Log.logInfoMessage("Time required to complete all steps: %d", timeElapsed);
    }

    public static void main(String[] args) throws IOException {
        new Day007().main();
    }
}
