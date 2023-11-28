package dev.ioliver.matchappbackend.configurations;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributesConfig extends DefaultErrorAttributes {
  private static final String STATUS_KEY = "status";

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest,
      ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
    errorAttributes.remove("timestamp");
    errorAttributes.remove("error");
    errorAttributes.remove("path");

    Integer statusCode = (Integer) errorAttributes.get(STATUS_KEY);

    errorAttributes.put("statusCode", statusCode);
    errorAttributes.put("statusName", HttpStatus.valueOf(statusCode));
    errorAttributes.remove(STATUS_KEY);

    return errorAttributes;
  }
}