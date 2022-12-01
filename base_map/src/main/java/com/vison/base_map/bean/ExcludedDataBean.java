package com.vison.base_map.bean;

/**
 * @author XiaoShu
 * @date 1/22/21.
 */
public class ExcludedDataBean {

    /**
     * message : Request must include a bounding rectangle under 20,000,000 square meters.
     * detail : {"name":"Ground Hazards","displayName":"Ground Hazards"}
     * errorReason : QueryAreaTooLarge
     */

    private String message;
    /**
     * name : Ground Hazards
     * displayName : Ground Hazards
     */

    private DetailBean detail;
    private String errorReason;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public static class DetailBean {
        private String name;
        private String displayName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }
}
