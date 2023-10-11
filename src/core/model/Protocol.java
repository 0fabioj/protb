package core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Protocol {
    private int id, status;
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

    public String getStatus() {
        String res;
        if(status == 1) { res = "Recebido"; }
        else if (status == 2) { res = "Remetido"; }
        else if (status == 3) { res = "Deferido"; }
        else if (status == 4) { res = "Indeferido"; }
        else { res = "Cancelado"; }

        return res;
    }
    public int getStatusInt() {
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
