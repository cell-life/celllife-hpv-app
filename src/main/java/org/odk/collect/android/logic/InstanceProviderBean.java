package org.odk.collect.android.logic;

/**
 * A Java bean that is used in conjunction with the TwoItemChoiceAdapter to show instance data in a list format.
 */
public class InstanceProviderBean {

    private long id;
    private String title;
    private String subtext;
    private String reference;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InstanceProviderClass [id=" + id + ", title=" + title + ", subtext=" + subtext + ", reference="
                + reference + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InstanceProviderBean other = (InstanceProviderBean) obj;
        if (id != other.id)
            return false;
        return true;
    }

}