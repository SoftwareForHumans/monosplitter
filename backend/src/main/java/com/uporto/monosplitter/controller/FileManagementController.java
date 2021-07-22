package com.uporto.monosplitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uporto.monosplitter.service.storage.StorageFileNotFoundException;
import com.uporto.monosplitter.service.storage.StorageService;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping(value = "/file-management")
public class FileManagementController {
	private final StorageService storageService;

	@Autowired
	public FileManagementController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping(value = "/upload")
	@ResponseBody
	public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		cleanDirectory();
		try {
			storageService.store(file);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(RuntimeException e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	
	private void cleanDirectory() {
		storageService.deleteAll();
		storageService.init();
	}
}
