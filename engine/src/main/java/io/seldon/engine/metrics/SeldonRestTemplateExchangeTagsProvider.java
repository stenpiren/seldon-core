package io.seldon.engine.metrics;

import java.util.Arrays;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import io.micrometer.core.instrument.Tag;
import io.micrometer.spring.web.client.RestTemplateExchangeTags;
import io.micrometer.spring.web.client.RestTemplateExchangeTagsProvider;
import io.seldon.engine.service.InternalPredictionService;

public class SeldonRestTemplateExchangeTagsProvider implements RestTemplateExchangeTagsProvider {

	@Override
	public Iterable<Tag> getTags(String urlTemplate, HttpRequest request, ClientHttpResponse response) 
	{
		Tag uriTag = StringUtils.hasText(urlTemplate)? RestTemplateExchangeTags.uri(urlTemplate): RestTemplateExchangeTags.uri(request);
		
		
	            
		return Arrays.asList(RestTemplateExchangeTags.method(request), uriTag,
				RestTemplateExchangeTags.status(response),
	            RestTemplateExchangeTags.clientName(request),
	            unitId(request));
	}
	
	private Tag unitId(HttpRequest request)
	{
		String unitId = request.getHeaders().getFirst(InternalPredictionService.UNIT_ID_HEADER);
		if (!StringUtils.hasText(unitId))
			unitId = "unknown";
		
		return Tag.of("unitId", unitId);
	}

}
