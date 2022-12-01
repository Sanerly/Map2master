package com.vison.base_map.bean;

import java.util.List;

/**
 * @author XiaoShu
 * @date 1/22/21.
 */
public class PropertiesBean {
    private String hazardFactor;
    private String hazardFactorName;
    private String fillColor;
    private String strokeColor;
    private float fillOpacity;
    private int borderWidth;
    private float borderOpacity;
    private int strokeWidth;
    private float strokeOpacity;
    private String detailedCategory;
    private String iconUrl;
    private String name;
    private String category;
    private int radius;

    /**
     * category : Ground Hazard
     * detailedCategory : Aerodrome
     * title : 深圳宝安国际机场
     * sections : [{"iconUrl":"https://api.altitudeangel.com/v1/map/icon?icon=warning.png","title":"Summary","displayTitle":"Summary","text":"Yellow zones indicate regions where operation of your drone may raise security, privacy or safety concerns.","disclaimer":null},{"iconUrl":"https://api.altitudeangel.com/v1/map/icon?icon=aeroway_aerodrome.png","title":"Aerodrome hazard","displayTitle":"Aerodrome hazard","text":"Flying in this area is likely to be prohibited. There is an extreme risk of interference with manned aviation. In most territories worldwide, you can only operate within the boundary of an aerodrome (or it's Control Area) with permission. Refrain from flying in the vicinity of the surrounding area and exercise elevated caution.","disclaimer":null}]
     * actions : []
     */

    private DisplayBean display;
    /**
     * name : Ground Hazards
     * property : show
     * displayName : Ground Hazards
     * active : true
     */

    private List<FiltersBean> filters;

    public String getHazardFactor() {
        return hazardFactor;
    }

    public void setHazardFactor(String hazardFactor) {
        this.hazardFactor = hazardFactor;
    }

    public String getHazardFactorName() {
        return hazardFactorName;
    }

    public void setHazardFactorName(String hazardFactorName) {
        this.hazardFactorName = hazardFactorName;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(float fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getBorderOpacity() {
        return borderOpacity;
    }

    public void setBorderOpacity(float borderOpacity) {
        this.borderOpacity = borderOpacity;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getStrokeOpacity() {
        return strokeOpacity;
    }

    public void setStrokeOpacity(float strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public String getDetailedCategory() {
        return detailedCategory;
    }

    public void setDetailedCategory(String detailedCategory) {
        this.detailedCategory = detailedCategory;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DisplayBean getDisplay() {
        return display;
    }

    public void setDisplay(DisplayBean display) {
        this.display = display;
    }

    public List<FiltersBean> getFilters() {
        return filters;
    }

    public void setFilters(List<FiltersBean> filters) {
        this.filters = filters;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public static class DisplayBean {
        private String category;
        private String detailedCategory;
        private String title;
        /**
         * iconUrl : https://api.altitudeangel.com/v1/map/icon?icon=warning.png
         * title : Summary
         * displayTitle : Summary
         * text : Yellow zones indicate regions where operation of your drone may raise security, privacy or safety concerns.
         * disclaimer : null
         */

        private List<SectionsBean> sections;
        private List<?> actions;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDetailedCategory() {
            return detailedCategory;
        }

        public void setDetailedCategory(String detailedCategory) {
            this.detailedCategory = detailedCategory;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<SectionsBean> getSections() {
            return sections;
        }

        public void setSections(List<SectionsBean> sections) {
            this.sections = sections;
        }

        public List<?> getActions() {
            return actions;
        }

        public void setActions(List<?> actions) {
            this.actions = actions;
        }

        public static class SectionsBean {
            private String iconUrl;
            private String title;
            private String displayTitle;
            private String text;
            private Object disclaimer;

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDisplayTitle() {
                return displayTitle;
            }

            public void setDisplayTitle(String displayTitle) {
                this.displayTitle = displayTitle;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public Object getDisclaimer() {
                return disclaimer;
            }

            public void setDisclaimer(Object disclaimer) {
                this.disclaimer = disclaimer;
            }
        }
    }

    public static class FiltersBean {
        private String name;
        private String property;
        private String displayName;
        private boolean active;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
