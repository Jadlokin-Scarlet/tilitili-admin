package com.tilitili.admin;

import com.tilitili.common.entity.RecommendVideo;

public class MainTest {
    public static void main(String[] args) {
        RecommendVideo recommendVideo = new RecommendVideo();
        System.out.println(recommendVideo.getType() == 1);
    }
}
