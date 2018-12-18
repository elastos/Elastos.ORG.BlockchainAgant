package org.elastos.POJO;

public class DidEntity {

    public static final String DID_TAG = "DID Property";

    public enum DidStatus{
        Normal,
        Deprecated
    }

    public class DidProperty {
        String key;
        String value;
        DidStatus status = DidStatus.Normal;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public DidStatus getStatus() {
            return status;
        }

        public void setStatus(DidStatus status) {
            this.status = status;
        }
    }

    String tag = DidEntity.DID_TAG;
    String did;
    DidStatus didStatus = DidStatus.Normal;
    String version = "1.0";
    DidProperty property;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getTag() {
        return tag;
    }

    public DidStatus getDidStatus() {
        return didStatus;
    }

    public void setDidStatus(DidStatus didStatus) {
        this.didStatus = didStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DidProperty getProperty() {
        return property;
    }

    public void setProperty(DidProperty property) {
        this.property = property;
    }

}
