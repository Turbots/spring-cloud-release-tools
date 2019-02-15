/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZipkinApplication {

	private static Logger log = LoggerFactory.getLogger(ZipkinApplication.class);

	// Use this for debugging (or if there is no Zipkin server running on port 9411)
	/*
	 * @Bean
	 *
	 * @ConditionalOnProperty(value="sample.zipkin.enabled", havingValue="false") public
	 * ZipkinSpanReporter spanCollector(Reporter<zipkin2.Span> reporter, EndpointLocator
	 * endpointLocator, Environment environment, List<SpanAdjuster> spanAdjusters) {
	 * return new ZipkinSpanReporter(reporter, endpointLocator, environment,
	 * spanAdjusters) {
	 *
	 * @Override public void report(Span span) { log.info("Reporting span [{}]", span); }
	 * }; }
	 */

	public static void main(String[] args) {
		SpringApplication.run(ZipkinApplication.class, args);
	}

}
