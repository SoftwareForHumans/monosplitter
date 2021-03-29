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
		File dir = new File(projectRootLocation.toString().replace("'\'", "/").concat("/external-libs"));
		File projectJAR = new File(projectRootLocation.toString().replace("'\'", "/").concat("/upload-dir")).listFiles()[0];
		
		try {
			Process process = Runtime.getRuntime().exec("java -jar javacg-0.1-SNAPSHOT-static.jar ".concat(projectJAR.getPath().toString()), null, dir);

			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			File staticAnalysisResult = new File(projectRootLocation.toString().replace("'\'", "\\").concat("\\upload-dir\\staticAnalysisResult.txt"));
			FileWriter fw = new FileWriter(staticAnalysisResult);
			
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
