package com.tradair.mock.model.sender;

public abstract class Sender<T> {

    protected long id;
    protected String externalId;
    protected T payload;

    public abstract <U> boolean send(U msg);

    public Sender(String externalId, T payload) {
        this.id = System.currentTimeMillis();
        this.externalId = externalId;
        this.payload = payload;
    }

    public long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Sender{");
        sb.append("id=").append(id);
        sb.append(", externalId='").append(externalId).append('\'');
        sb.append(", payload=").append(payload);
        sb.append('}');
        return sb.toString();
    }
}
