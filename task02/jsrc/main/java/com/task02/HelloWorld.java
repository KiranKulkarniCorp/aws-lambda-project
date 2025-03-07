package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	public Map<String, Object> handleRequest(Map<String, Object> request, Context context) {
		System.out.println("Received request: " + request);

		// Extract HTTP method and path
		Map<String, Object> requestContext = (Map<String, Object>) request.get("requestContext");
		Map<String, Object> httpInfo = (Map<String, Object>) requestContext.get("http");

		String path = (String) request.getOrDefault("rawPath", "/");
		String method = (String) httpInfo.getOrDefault("method", "GET");

		Map<String, Object> response = new HashMap<>();
		response.put("headers", Map.of("Content-Type", "application/json"));

		if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
			response.put("statusCode", 200);
			response.put("body", "{\"message\": \"Hello from Lambda!\"}");
		} else {
			response.put("statusCode", 400);
			response.put("body", "{\"error\": \"Bad Request\", \"message\": \"Invalid request to " + path + " with method " + method + "\"}");
		}

		return response;
	}
}
