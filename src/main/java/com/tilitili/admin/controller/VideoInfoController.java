package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.VideoInfoCountRequest;
import com.tilitili.admin.entity.count.VideoInfoCountResponse;
import com.tilitili.admin.service.VideoInfoService;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.VideoInfoMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/video/info")
@Validated
@Slf4j
public class VideoInfoController extends BaseController {

    private final VideoInfoService videoInfoService;
    private final VideoInfoMapper videoInfoMapper;

    @Autowired
    public VideoInfoController(VideoInfoService videoInfoService, VideoInfoMapper videoInfoMapper) {
        this.videoInfoService = videoInfoService;
        this.videoInfoMapper = videoInfoMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<VideoInfo>> getVideoInfoByCondition(VideoInfoQuery query) {
        Asserts.notNull(query, "参数异常");

        QueryUtil.suppleQuery(query);

        int count = videoInfoMapper.countForVideoInfoTable(query);
        List<VideoInfo> videoInfoList = videoInfoMapper.listForVideoInfoTable(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoInfoList);
    }

    @GetMapping("/count")
    @ResponseBody
    public BaseModel<VideoInfoCountResponse> getVideoInfoCount(VideoInfoCountRequest request) {
        Asserts.notNull(request, "参数异常");
        Asserts.notNull(request.getTime(), "查询区间未获取到");
        VideoInfoCountResponse videoInfoCountResponse = new VideoInfoCountResponse();
        videoInfoCountResponse.setNewVideoCountList(videoInfoService.getNewVideoCount(request));
        return BaseModel.success(videoInfoCountResponse);
    }

    @DeleteMapping("/{av}/isDelete/true")
    @ResponseBody
    public BaseModel<?> deleteVideo(@PathVariable Long av) {
        videoInfoService.delete(av);
        return new BaseModel<>("成功删除", true);
    }

    @PatchMapping("/{av}/isDelete/false")
    @ResponseBody
    public BaseModel<?> recoveryVideo(@PathVariable Long av) {
        videoInfoService.recovery(av);
        return new BaseModel<>("成功恢复", true);
    }

    @PatchMapping("/startTime")
    @ResponseBody
    public BaseModel<?> updateStartTime(@RequestBody VideoInfo videoInfo) {
        Asserts.notNull(videoInfo, "参数异常");
        Asserts.notNull(videoInfo.getAv(), "参数异常");
        Asserts.notNull(videoInfo.getStartTime(), "参数异常");
        videoInfoService.updateStartTime(videoInfo.getAv(), videoInfo.getStartTime());
        return new BaseModel<>("更新成功", true);
    }

    @PatchMapping("/externalOwner")
    @ResponseBody
    public BaseModel<?> updateExternalOwner(@RequestBody VideoInfo videoInfo) {
        Asserts.notNull(videoInfo, "参数异常");
        Asserts.notNull(videoInfo.getAv(), "参数异常");
        if (videoInfo.getExternalOwner() == null) videoInfo.setExternalOwner("");
        videoInfoMapper.updateExternalOwner(videoInfo.getAv(), videoInfo.getExternalOwner());
        return new BaseModel<>("更新成功", true);
    }

    @PatchMapping("/isCopyWarning")
    @ResponseBody
    public BaseModel<?> updateIsCopyWarning(@RequestBody VideoInfo videoInfo) {
        Asserts.notNull(videoInfo, "参数异常");
        Asserts.notNull(videoInfo.getAv(), "参数异常");
        Asserts.notNull(videoInfo.getIsCopyWarning(), "参数异常");
        VideoInfo oldVideoInfo = videoInfoMapper.getVideoInfoByAv(videoInfo.getAv());
        Asserts.notNull(oldVideoInfo.getCopyright(), "参数异常");

        if (oldVideoInfo.getCopyright()) {
            return new BaseModel<>("已经是搬运视频了，不用疑似");
        }
        videoInfoMapper.updateIsCopyWarning(videoInfo.getAv(), videoInfo.getIsCopyWarning());
        return new BaseModel<>("更新成功", true);
    }

}
