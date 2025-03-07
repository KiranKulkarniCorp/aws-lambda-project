package com.task01;

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
		System.out.println("Request received: " + request);

		// Extract path and HTTP method
		String path = (String) request.get("rawPath");
		String method = (String) request.get("requestContext.http.method");

		Map<String, Object> response = new HashMap<>();

		if ("/hello".equals(path) && "GET".equals(method)) {
			response.put("statusCode", 200);
			response.put("message", "Hello from Lambda");
		} else {
			response.put("statusCode", 400);
			response.put("error", "Bad Request: " + method + " " + path + " is not a valid endpoint.");
		}
		return response;
	}
}
