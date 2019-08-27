package io.pivotal.pal.tracker;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> backingMap = new HashMap<>();

    private long idIncrement = 0;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++idIncrement);
        backingMap.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return backingMap.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(backingMap.values());
    }

    @Override
    public void delete(long id) {
        backingMap.remove(id);
    }

    @Override
    public TimeEntry update(long l, TimeEntry timeEntry) {
        TimeEntry existingTimeEntry = backingMap.get(l);
        if (existingTimeEntry == null) {
            return null;
        }
        timeEntry.setId(l);
        backingMap.put(l, timeEntry);
        return timeEntry;
    }

}
