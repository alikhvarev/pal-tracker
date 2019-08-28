package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntry);
    }

    @PostMapping(consumes = {"application/vnd.example.v2+json"})
    public ResponseEntity createV2(@RequestBody TimeEntryV2 timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(timeEntry);
    }

    @GetMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry != null) {
            actionCounter.increment();
            return ResponseEntity.ok(timeEntry);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity update(@PathVariable("timeEntryId") long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
        if (timeEntry != null) {
            actionCounter.increment();
            return ResponseEntity.ok(timeEntry);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{timeEntryId}")
    public ResponseEntity delete(@PathVariable("timeEntryId") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);

        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        return ResponseEntity.noContent().build();
    }
}
