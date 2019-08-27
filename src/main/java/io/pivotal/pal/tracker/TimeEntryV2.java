package io.pivotal.pal.tracker;

import java.time.LocalDate;

public class TimeEntryV2 extends TimeEntry {

    private boolean smtNew;

    public TimeEntryV2(long projectId, long userId, LocalDate date, int hours) {
        super(projectId, userId, date, hours);
    }

    public TimeEntryV2(long id, long projectId, long userId, LocalDate date, int hours) {
        super(id, projectId, userId, date, hours);
    }

    public TimeEntryV2() {
    }

    public boolean isSmtNew() {
        return smtNew;
    }

    public void setSmtNew(boolean smtNew) {
        this.smtNew = smtNew;
    }
}
