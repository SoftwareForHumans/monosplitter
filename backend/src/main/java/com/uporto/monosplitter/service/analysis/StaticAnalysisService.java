package com.uporto.monosplitter.service.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaticAnalysisService implements AnalysisService {
	private final Path projectRootLocation;

	@Autowired
	public StaticAnalysisService() {
		this.projectRootLocation = Paths.get("").toAbsolutePath();
	}
	
	@Override
	public void execute() {
		String osOperation = System.getProperty("os.name").toLowerCase().startsWith("windows") ? "cmd.exe" : "sh";
		File executionDir = new File(projectRootLocation.toString().replace("'\'", "/").concat("/external-libs"));
		File projectJAR = new File(projectRootLocation.toString().replace("'\'", "/").concat("/upload-dir")).listFiles()[0];
		
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(osOperation, "/c", "java -jar javacg-0.1-SNAPSHOT-static.jar ".concat(projectJAR.getPath().toString()));
			builder.directory(executionDir);
			
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String fileName = projectJAR.getName().toString().split("\\.")[0];
			File result = new File(projectRootLocation.toString().replace("'\'", "\\").
												concat("\\upload-dir\\").
												concat(fileName).
												concat("_STATIC_ANALYSIS.txt"));
			
			FileWriter fw = new FileWriter(result);
			
			String line;
			while ((line = reader.readLine()) != null) {
				fw.write(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				fw.close();
			} 

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
