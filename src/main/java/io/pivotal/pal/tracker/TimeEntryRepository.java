package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {
    TimeEntry create(TimeEntry timeEntry);

    TimeEntry find(long timeEntryId);

    List<TimeEntry> list();

    void delete(long id);

    TimeEntry update(long l, TimeEntry timeEntry);

    //boolean isEmpty();
}
