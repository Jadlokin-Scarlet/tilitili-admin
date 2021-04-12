package com.tilitili.admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DispatchResourcesView {
    private Integer isStaffShow1;
    private Integer isStaffShow2;
    private String musicName;
    private String musicOwner;
    private String musicCard;
    private String musicImage;
    private String musicSource;
    private String markTime;
    private String v;
    private String countTime;
    private String tips;

    public DispatchResourcesView setIsStaffShow1(String isStaffShow1) {
        this.isStaffShow1 = Integer.valueOf(isStaffShow1);
        return this;
    }

    public DispatchResourcesView setIsStaffShow2(String isStaffShow2) {
        this.isStaffShow2 = Integer.valueOf(isStaffShow2);
        return this;
    }

    public String toString() {
        return "isStaffShow1\n" + isStaffShow1 + "\n"
                + "isStaffShow2\n" + isStaffShow2 + "\n"
                + "musicName\n" + musicName + "\n"
                + "musicOwner\n" + musicOwner + "\n"
                + "musicCard\n" + musicCard + "\n"
                + "musicImage\n" + musicImage + "\n"
                + "musicSource\n" + musicSource + "\n";
    }
}
