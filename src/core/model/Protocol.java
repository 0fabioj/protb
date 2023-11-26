package core.model;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

import java.time.LocalDate;

public class Protocol {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> recorded;
    private Person person;
    private ProtocolType protocolType;
    private final StringProperty summary;
    private final StringProperty path;
    private final ObjectProperty<LocalDate> requested;
    private final IntegerProperty status;
    private final ObjectProperty<LocalDate> receipted;
    private final ObjectProperty<LocalDate> forwarded;
    private final ObjectProperty<LocalDate> checked;

    public Protocol(int id, LocalDate recorded, Person person, ProtocolType protocolType,
                    String summary, String path, LocalDate requested, int status,
                    LocalDate receipted, LocalDate forwarded, LocalDate checked) {
        this.id = new SimpleIntegerProperty(id);
        this.recorded = new SimpleObjectProperty<>(recorded);
        this.person = person;
        this.protocolType = protocolType;
        this.summary = new SimpleStringProperty(summary);
        this.path = new SimpleStringProperty(path);
        this.requested = new SimpleObjectProperty<>(requested);
        this.status = new SimpleIntegerProperty(status);
        this.receipted = new SimpleObjectProperty<>(receipted);
        this.forwarded = new SimpleObjectProperty<>(forwarded);
        this.checked = new SimpleObjectProperty<>(checked);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Person person() {
        return person;
    }

    public Protocol setPerson(Person person) {
        this.person = person;
        return this;
    }

    public ProtocolType protocolType() {
        return protocolType;
    }

    public Protocol setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return this;
    }

    public LocalDate getRecorded() {
        return recorded.get();
    }

    public ObjectProperty<LocalDate> recordedProperty() {
        return recorded;
    }

    public void setRecorded(LocalDate recorded) {
        this.recorded.set(recorded);
    }

    public String getSummary() {
        return summary.get();
    }

    public StringProperty summaryProperty() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.set(summary);
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public LocalDate getRequested() {
        return requested.get();
    }

    public ObjectProperty<LocalDate> requestedProperty() {
        return requested;
    }

    public void setRequested(LocalDate requested) {
        this.requested.set(requested);
    }

    public String getStatus() {
        return switch (status.get()) {
            case 1 -> "Remetido";
            case 2 -> "Deferido";
            case 3 -> "Indeferido";
            case 4 -> "Cancelado";
            default -> "Recebido";
        };
    }
    public int getStatusInt() {
        return status.get();
    }

    public IntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public LocalDate getReceipted() {
        return receipted.get();
    }

    public ObjectProperty<LocalDate> receiptedProperty() {
        return receipted;
    }

    public void setReceipted(LocalDate receipted) {
        this.receipted.set(receipted);
    }

    public LocalDate getForwarded() {
        return forwarded.get();
    }

    public ObjectProperty<LocalDate> forwardedProperty() {
        return forwarded;
    }

    public void setForwarded(LocalDate forwarded) {
        this.forwarded.set(forwarded);
    }

    public LocalDate getChecked() {
        return checked.get();
    }

    public ObjectProperty<LocalDate> checkedProperty() {
        return checked;
    }

    public void setChecked(LocalDate checked) {
        this.checked.set(checked);
    }

    public static Callback<Protocol, Observable[]> extractor() {
        return (Protocol p) -> new Observable[] {
                p.idProperty(),
                p.recordedProperty(),
                p.summaryProperty(),
                p.requestedProperty(),
                p.statusProperty(),
                p.receiptedProperty(),
                p.forwardedProperty(),
                p.checkedProperty()
        };
    }
}
