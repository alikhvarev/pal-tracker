package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class JpaTimeEntryRepository implements TimeEntryRepository {

    private EntityManager entityManager;

    public JpaTimeEntryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        entityManager.persist(timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return entityManager.find(TimeEntry.class, timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return entityManager.createQuery("select t from TimeEntry t").getResultList();
    }

    @Override
    public void delete(long id) {
        Query query = entityManager.createQuery("delete from TimeEntry t where t.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public TimeEntry update(long l, TimeEntry timeEntry) {
        timeEntry.setId(l);
        return entityManager.merge(timeEntry);
    }
}
