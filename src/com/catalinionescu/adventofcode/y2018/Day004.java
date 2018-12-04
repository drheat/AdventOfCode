package com.catalinionescu.adventofcode.y2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.catalinionescu.adventofcode.common.Log;

public class Day004 {
    public static String INPUT_URL = "https://adventofcode.com/2018/day/4/input";
    public static String INPUT_FILE = "inputs/input004.txt";

    enum EventType {
        FALL_ASLEEP,
        WAKE_UP,
        BEGIN_SHIFT
    }

    class Event implements Comparable<Event> {
        private int day;
        private int month;

        private int hour;
        private int min;

        private int timestamp;

        private EventType eventType;
        // relevant only if eventType == BEGIN_SHIFT
        private int guardId;

        public Event(final String rawEvent) {
            String[] split = rawEvent.split(" ");
            // The 3 possible event types:
            // [1518-08-02 23:59] Guard #1657 begins shift
            // [1518-03-26 00:25] falls asleep
            // [1518-09-03 00:27] wakes up
            if (split[2].equals("Guard")) {
                eventType = EventType.BEGIN_SHIFT;
            } else if (split[2].equals("falls")) {
                eventType = EventType.FALL_ASLEEP;
            } else {
                eventType = EventType.WAKE_UP;
            }

            if (eventType == EventType.BEGIN_SHIFT) {
                guardId = Integer.parseInt(split[3].substring(1, split[3].length()));
            }

            String rawDate = split[0].substring(1, split[0].length());
            String[] dateParts = rawDate.split("-");
            day = Integer.parseInt(dateParts[2]);
            month = Integer.parseInt(dateParts[1]);

            String rawTime = split[1].substring(0, split[1].length() - 1);
            String[] timeParts = rawTime.split(":");
            min = Integer.parseInt(timeParts[1]);

            hour = Integer.parseInt(timeParts[0]);
            // if (hour == 23) {
            // min = 0;
            // }

            // Timestamp conversion in minutes
            timestamp = min + hour * 60 + day * 24 * 60 + month * 31 * 24 * 60;
        }

        public EventType getType() {
            return eventType;
        }

        public int getGuardId() {
            return guardId;
        }

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }

        public int getHour() {
            return hour;
        }

        public int getMin() {
            return min;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Event)) {
                return false;
            }

            Event other = (Event) o;
            // For a minor speed up, we deliberately ignore guardId since there is a single guard working each night shift
            return (other.getType() == getType()) && (other.getMonth() == getMonth()) && (other.getDay() == getDay()) && (other.getMin() == getMin());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getType().hashCode();
            result = prime * result + getMonth();
            result = prime * result + getDay();
            result = prime * result + getMin();
            return result;
        }

        @Override
        public int compareTo(Event o) {
            // The ordering is strictly based off the timestamp. This is pushing the comparison contract (the equals part), but due to the particular input data
            // we can get away with it.
            return timestamp - o.timestamp;
        }

        @Override
        public String toString() {
            String blurb = "";
            switch (getType()) {
                case BEGIN_SHIFT:
                    blurb = String.format("Guard #%d begins shift", getGuardId());
                    break;
                case FALL_ASLEEP:
                    blurb = "falls asleep";
                    break;
                case WAKE_UP:
                    blurb = "wakes up";
                    break;
            }
            return String.format("[1518-%02d-%02d %02d:%02d] %s", getMonth(), getDay(), getHour(), getMin(), blurb);
        }
    }

    class Guard {
        private int id;
        private int minutesAsleep;
        // This guard's one hour schedule broken down into minutes
        private int[] schedule = new int[60];

        public Guard(final int id) {
            this.id = id;
        }

        public void addAsleepTime(int minStart, int minEnd) {
            minutesAsleep += (minEnd - minStart);
            for (int min = minStart; min < minEnd; min++) {
                schedule[min]++;
            }
        }

        public int getId() {
            return id;
        }

        public int getMinutesAsleep() {
            return minutesAsleep;
        }

        public int getMinuteAsleepMost() {
            // TODO: For production use, this would be computed once in addAsleepTime and the cached value would be returned here
            int result = 0;
            int timesSlept = 0;
            for (int min = 0; min < 60; min++) {
                if (schedule[min] > timesSlept) {
                    result = min;
                    timesSlept = schedule[min];
                }
            }

            return result;
        }

        public int getTimesSleptMostSameMinute() {
            // TODO: For production use, this would be computed once in addAsleepTime and the cached value would be returned here
            int timesSlept = 0;
            for (int min = 0; min < 60; min++) {
                if (schedule[min] > timesSlept) {
                    timesSlept = schedule[min];
                }
            }

            return timesSlept;
        }
    }

    private void main() throws IOException {
        List<String> input = Files.readAllLines(Paths.get(INPUT_FILE));

        if (input == null || input.isEmpty()) {
            Log.logErrorMessage("Input data set is empty!");
            return;
        }

        Set<Event> events = new TreeSet<>();
        for (String line : input) {
            events.add(new Event(line));
        }

        Log.logInfoMessage("Parsed and sorted %d events.", events.size());

        // for (Event event : events) {
        // Log.logInfoMessage(event.toString());
        // }

        // <guard Id, guard> for fast lookup
        Map<Integer, Guard> guards = new HashMap<>();
        Guard currentGuard = null;
        boolean isAsleep = false;
        int sleepStart = 0, sleepEnd = 0;

        for (Event event : events) {
            switch (event.getType()) {
                case BEGIN_SHIFT:
                    guards.putIfAbsent(event.getGuardId(), new Guard(event.getGuardId()));
                    currentGuard = guards.get(event.getGuardId());
                    isAsleep = false;
                    break;

                case FALL_ASLEEP:
                    if (currentGuard == null) {
                        throw new IllegalStateException("A guard fell asleep but there's no guard on duty");
                    }

                    if (isAsleep) {
                        throw new IllegalStateException(String.format("Guard %d fell asleep but it was already sleeping", currentGuard.getId()));
                    }

                    isAsleep = true;
                    sleepStart = event.getMin();
                    break;

                case WAKE_UP:
                    if (currentGuard == null) {
                        throw new IllegalStateException("A guard woke up but there's no guard on duty");
                    }

                    if (!isAsleep) {
                        throw new IllegalStateException(String.format("Guard %d woke up but wasn't asleep", currentGuard.getId()));
                    }

                    isAsleep = false;
                    sleepEnd = event.getMin();
                    currentGuard.addAsleepTime(sleepStart, sleepEnd);
                    break;
            }
        }

        if (guards.isEmpty()) {
            Log.logErrorMessage("No guards found on duty :(");
            return;
        }

        // ********* Part 1
        Guard sleepy = guards.values().iterator().next();
        for (Guard guard : guards.values()) {
            if (guard.getMinutesAsleep() > sleepy.getMinutesAsleep()) {
                sleepy = guard;
            }
        }
        Log.logInfoMessage("Part 1 - Guard %d slept %d minutes total, with minute %d asleep most. Response: %d", sleepy.getId(), sleepy.getMinutesAsleep(), sleepy.getMinuteAsleepMost(), sleepy.getId() *
                sleepy.getMinuteAsleepMost());

        // ********* Part 2
        sleepy = guards.values().iterator().next();
        for (Guard guard : guards.values()) {
            if (guard.getTimesSleptMostSameMinute() > sleepy.getTimesSleptMostSameMinute()) {
                sleepy = guard;
            }
        }
        Log.logInfoMessage("Part 2 - Guard %d slept %d minutes total, with minute %d asleep most, %d times. Response: %d", sleepy.getId(), sleepy.getMinutesAsleep(), sleepy.getMinuteAsleepMost(),
            sleepy.getTimesSleptMostSameMinute(), sleepy.getId() * sleepy.getMinuteAsleepMost());
    }

    public static void main(String[] args) throws IOException {
        new Day004().main();
    }
}
