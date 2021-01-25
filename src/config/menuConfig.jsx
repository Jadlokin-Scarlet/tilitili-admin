import VideoInfoManager from "../pages/rank/VideoInfoManager";
import VideoDataManager from "../pages/rank/VideoDataManager";
import SpiderVideoManager from "../pages/rank/SpiderVideoManager";
import BatchSpiderVideoManager from "../pages/rank/BatchSpiderVideoManager";
import VideoDataFileCheck from "../pages/rank/VideoDataFileCheck";
import TagManager from "../pages/tilitili/TagManager";
import VideoTagManager from "../pages/tilitili/VideoTagManager";
import VideoResourceManager from "../pages/rank/VideoResourceManager";
import OwnerManager from "../pages/tilitili/OwnerManager";

export const menuList = [
    {key: "/rank", title: "排行榜后台", icon: "customer-service", children: [
        { title: "视频信息管理", key: "/rank/videoInfoManager", icon: "video-camera", component: VideoInfoManager},
        { title: "视频排行管理", key: "/rank/videoDataManager", icon: "database", component: VideoDataManager},
        { title: "视频排行文件检查", key: "/rank/videoDataFileCheck", icon: "database", component: VideoDataFileCheck},
        { title: "自定义爬取", key: "/rank/spiderVideoManager", icon: "bug", component: SpiderVideoManager},
        { title: "批量爬取", key: "/rank/batchSpiderVideoManager", icon: "bug", component: BatchSpiderVideoManager},
        { title: "视频资源下发管理", key: "/rank/videoResourceManager", icon: "arrow-down", component: VideoResourceManager},
    ]},
    {key: "/tilitili", title: "视频站后台", icon: "customer-service", children: [
        { title: "作者管理", key: "/tilitili/ownerManager", icon: "user", component: OwnerManager},
        { title: "标签管理", key: "/tilitili/tagManager", icon: "tag", component: TagManager},
        { title: "视频标签管理", key: "/tilitili/videoTagManager", icon: "tag", component: VideoTagManager},
    ]}
]
