package by.training.entity;

public abstract class BaseTextElement {

    protected long id;
    protected final long parentId;
    protected final String text;

    public BaseTextElement(long id, long parentId, String text) {
        this.id = id;
        this.parentId = parentId;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getText() {
        return text;
    }

}
