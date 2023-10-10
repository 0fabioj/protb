package core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Protocol {
    private int id, newId, status;
    private LocalDate recorded, requested, receipted, forwarded, checked;
    private LocalDateTime created, altered;
    private String summary, path;
    private Person person;
    private ProtocolType protocolType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getRecorded() {
        return recorded;
    }

    public void setRecorded(LocalDate recorded) {
        this.recorded = recorded;
    }

    public LocalDate getRequested() {
        return requested;
    }

    public void setRequested(LocalDate requested) {
        this.requested = requested;
    }

    public LocalDate getReceipted() {
        return receipted;
    }

    public void setReceipted(LocalDate receipted) {
        this.receipted = receipted;
    }

    public LocalDate getForwarded() {
        return forwarded;
    }

    public void setForwarded(LocalDate forwarded) {
        this.forwarded = forwarded;
    }

    public LocalDate getChecked() {
        return checked;
    }

    public void setChecked(LocalDate checked) {
        this.checked = checked;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getAltered() {
        return altered;
    }

    public void setAltered(LocalDateTime altered) {
        this.altered = altered;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public Protocol() {    }
    public Protocol(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
