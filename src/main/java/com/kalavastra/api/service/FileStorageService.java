package com.kalavastra.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Service
public class FileStorageService {

	@Value("${app.fe.images-path}")
	private String imagesPath;

	@Value("${app.fe.images-url}")
	private String imagesUrl;

	/**
	 * Save the uploaded file under imagesPath, preserving its original filename,
	 * and return the public URL (imagesUrl + "/" + filename).
	 */
	public String saveProductImage(MultipartFile file) throws IOException {
		// clean up the incoming filename
		String original = StringUtils.cleanPath(file.getOriginalFilename());
		if (original.contains("..")) {
			throw new IOException("Invalid file name: " + original);
		}

		// ensure the target directory exists
		Path targetDir = Paths.get(imagesPath).toAbsolutePath().normalize();
		Files.createDirectories(targetDir);

		// copy to {imagesPath}/{originalFilename}, replacing if already there
		Path target = targetDir.resolve(original);
		try (InputStream in = file.getInputStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}

		// return the URL clients will use to fetch it
		return imagesUrl + "/" + original;
	}
}
