package com.rsm.sample.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rsm.common.vo.FileInfoVo;

@RestController
@RequestMapping("/file")
public class FileCtrl {

	public static final String DIRECTORY = "/Users/kimwonchul/atomikos";

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FileInfoVo> upload(HttpServletRequest request) {

		MultipartHttpServletRequest mpsr = (MultipartHttpServletRequest) request;

		FileInfoVo fileInfo = new FileInfoVo();

		List<MultipartFile> mf = mpsr.getFiles("demo");

		if (mf.size() == 1 && mf.get(0).getOriginalFilename().equals("")) {
			return new ResponseEntity<FileInfoVo>(HttpStatus.BAD_REQUEST);
		}

		for (int i = 0; i < mf.size(); i++) {

			File destinationFile = new File(DIRECTORY + File.separator + mf.get(i).getOriginalFilename());

			try {
				mf.get(i).transferTo(destinationFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				return new ResponseEntity<FileInfoVo>(HttpStatus.BAD_REQUEST);
			}

			fileInfo.setFileName(destinationFile.getPath());
			fileInfo.setFileSize(mf.get(i).getSize());
		}

		return new ResponseEntity<FileInfoVo>(fileInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @RequestParam("file") String fileName) {
		File file = new File(DIRECTORY + File.separator + fileName);

		if (file.exists()) {

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());

			InputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public ResponseEntity<FileInfoVo> delete(@RequestParam("file") String fileName) {

		File file = new File(DIRECTORY + File.separator + fileName);

		FileInfoVo fileInfo = new FileInfoVo();
		
		if (file.exists()) {
			file.delete();
		}
		
		return new ResponseEntity<FileInfoVo>(fileInfo, HttpStatus.OK);
	}

}
