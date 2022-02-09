package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataAdminFileItem;
import com.tilitili.admin.utils.MathUtil;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.dto.VideoDTO;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.mapper.rank.VideoDataMapper;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tilitili.admin.utils.StringUtil.bigNumberFormat;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class VideoDataServiceTest extends TestCase {
    @Resource
    private VideoDataService videoDataService;
    @Resource
    private VideoDataMapper videoDataMapper;
    @Resource
    private VideoDataFileService videoDataFileService;
    @Test
    void reRank() {
        VideoDTO hisData = videoDataMapper.getByAvAndIssue(715997051L, 44);
    }
    @Test
    void test() {
        List<VideoDataAdminFileItem> list = videoDataFileService.listForDataFile(new VideoDataQuery().setIssue(videoDataMapper.getNewIssue()));
        VideoDataAdminFileItem a = null;
        VideoDataAdminFileItem b = null;
        for (VideoDataAdminFileItem item : list) {
            if (item.getAv().equals(507491334L)){
                a=item;
            } else if (item.getAv().equals(807479224L)) {
                b=item;
            }
        }
        System.out.println();
    }

}