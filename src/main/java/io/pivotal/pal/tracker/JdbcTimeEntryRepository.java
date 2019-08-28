package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql =
                "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                        "VALUES (?, ?, ?, ?)";

        long millis = TimeUnit.DAYS.toMillis(timeEntry.getDate().toEpochDay());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, new Date(millis));
            ps.setInt(4, timeEntry.getHours());

            return ps;
        }, keyHolder);

        BigInteger id = (BigInteger) keyHolder.getKey();
        timeEntry.setId(id.longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        try {
            Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", timeEntryId);
            return toTimeEntry(foundEntry);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private TimeEntry toTimeEntry(Map<String, Object> foundEntry) {
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId((Long) foundEntry.get("id"));
        timeEntry.setProjectId((Long) foundEntry.get("project_id"));
        timeEntry.setUserId((Long) foundEntry.get("user_id"));
        timeEntry.setDate(((Date) foundEntry.get("date")).toLocalDate());
        timeEntry.setHours((Integer) foundEntry.get("hours"));
        return timeEntry;
    }

    @Override
    public List<TimeEntry> list() {
        List<Map<String, Object>> foundEntries = jdbcTemplate.queryForList("Select * from time_entries");
        return foundEntries.stream().map(foundEntry -> toTimeEntry(foundEntry)).collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        String sql =
                "DELETE FROM time_entries " +
                        " WHERE id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql);
            ps.setLong(1, id);

            return ps;
        });
    }

    @Override
    public TimeEntry update(long l, TimeEntry timeEntry) {
        String sql =
                "UPDATE time_entries " +
                        " SET project_id = ?, user_id = ?, date = ?, hours =? WHERE id = ?";

        long millis = TimeUnit.DAYS.toMillis(timeEntry.getDate().toEpochDay());

        int rowsUpdated = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, new Date(millis));
            ps.setInt(4, timeEntry.getHours());
            ps.setLong(5, l);

            return ps;
        });

        if (rowsUpdated > 0) {
            timeEntry.setId(l);
            return timeEntry;
        }

        return null;
    }
}
