package br.com.qwasolucoes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.cloud.documentai.v1.RawDocument;
import com.google.protobuf.ByteString;

public class ProcessDocument {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
		processDocument();

	}

	public static void processDocument()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		String projectId = "915639844858";
		String location = "us"; // Format is "us" or "eu".
		String processerId = "9e42edc9d235fb7c";
		String filePath = "D:\\CNH - RONAN.pdf";
		processDocument(projectId, location, processerId, filePath);
	}

	public static void processDocument(String projectId, String location, String processorId, String filePath)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create()) {
			final String name = String.format("projects/%s/locations/%s/processors/%s", projectId, location, processorId);

			byte[] imageFileData = Files.readAllBytes(Paths.get(filePath));
			
			ByteString content = ByteString.copyFrom(imageFileData);

			RawDocument document = RawDocument.newBuilder().setContent(content).setMimeType("application/pdf").build();

			ProcessRequest request = ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();
			
			ProcessResponse result = client.processDocument(request);
			Document documentResponse = result.getDocument();

			String text = documentResponse.getText();
			
			System.out.println(text);
		}
	}

}
