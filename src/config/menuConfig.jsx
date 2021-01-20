import VideoInfoManager from "../pages/VideoInfoManager";
import VideoDataManager from "../pages/VideoDataManager";
import SpiderVideoManager from "../pages/SpiderVideoManager";
import BatchSpiderVideoManager from "../pages/BatchSpiderVideoManager";
import VideoDataFileCheck from "../pages/VideoDataFileCheck";
import TagManager from "../pages/TagManager";
import VideoTagManager from "../pages/VideoTagManager";
import VideoResourceManager from "../pages/VideoResourceManager";
import OwnerManager from "../pages/OwnerManager";

export const menuList = [
    { title: "视频信息管理", key: "/videoInfoManager", icon: "video-camera", component: VideoInfoManager},
    { title: "视频排行管理", key: "/videoDataManager", icon: "database", component: VideoDataManager},
    { title: "视频排行文件检查", key: "/videoDataFileCheck", icon: "database", component: VideoDataFileCheck},
    { title: "自定义爬取", key: "/spiderVideoManager", icon: "bug", component: SpiderVideoManager},
    { title: "批量爬取", key: "/batchSpiderVideoManager", icon: "bug", component: BatchSpiderVideoManager},
    { title: "视频标签管理", key: "/videoTagManager", icon: "tag", component: VideoTagManager},
    { title: "标签管理", key: "/tagManager", icon: "tag", component: TagManager},
    { title: "视频资源下发管理", key: "/videoResourceManager", icon: "arrow-down", component: VideoResourceManager},
    { title: "作者管理", key: "/ownerManager", icon: "user", component: OwnerManager},
]
