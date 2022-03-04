package com.tilitili.admin.controller;

import com.tilitili.common.utils.Asserts;
import com.typoface.jwoff2.WOFF2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


@Slf4j
@Controller
@RequestMapping("/api/pub/font")
public class FontController extends BaseController{
	private final File cacheWoff2File = new File("/home/www/data/tilitili-admin/font.woff2");
	private final File cacheTTFFile = new File("/home/www/data/tilitili-admin/font.woff2");

	@PostMapping("/woff2Tottf")
	public void woff2Tottf(MultipartFile woff2File, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Asserts.notNull(woff2File, "获取文件失败");
		synchronized (cacheWoff2File) {
			if (cacheWoff2File.exists()) cacheWoff2File.delete();
			if (cacheTTFFile.exists()) cacheTTFFile.delete();
			woff2File.transferTo(cacheWoff2File);
			WOFF2.convertWOFF2ToTTF(cacheWoff2File.getCanonicalPath(), cacheTTFFile.getCanonicalPath());
			download(request, response, cacheTTFFile);
		}
	}
}
