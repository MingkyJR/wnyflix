package com.wny.wnyflix;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WnyflixApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WnyflixApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

//	@Bean
//	public OpenTelemetry openTelemetry() {
//		Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "wnyflix").put(ResourceAttributes.SERVICE_VERSION, "0.1.0").build();
//
//		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
//				.addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
//				.setResource(resource)
//				.build();
//
//		SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//				.registerMetricReader(PeriodicMetricReader.builder(LoggingMetricExporter.create()).build())
//				.setResource(resource)
//				.build();
//
//		SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
//				.addLogRecordProcessor(BatchLogRecordProcessor.builder(SystemOutLogRecordExporter.create()).build())
//				.setResource(resource)
//				.build();
//
//		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
//				.setTracerProvider(sdkTracerProvider)
//				.setMeterProvider(sdkMeterProvider)
//				.setLoggerProvider(sdkLoggerProvider)
//				.setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
//				.buildAndRegisterGlobal();
//
//		return openTelemetry;
//	}

}
