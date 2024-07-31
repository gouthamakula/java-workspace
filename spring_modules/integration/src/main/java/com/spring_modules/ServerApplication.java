package com.spring_modules;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.splitter.DefaultMessageSplitter;

@SpringBootApplication
@EnableIntegrationManagement
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	public IntegrationFlow flow(RabbitTemplate rabbitTemplate) {
		return IntegrationFlow.from(Http.inboundGateway("/receiveGateway")
						.requestMapping(m -> m.methods(HttpMethod.POST))
						.requestPayloadType(String.class))
				.split(commaSplitter())
				.<String, String>transform(p -> p + "from the other side")
//				.channel(MessageChannels.executor(executor()))
//				.<String, String>transform(String::toUpperCase)
				.handle(Amqp.outboundGateway(rabbitTemplate).routingKey("foo"))
				.aggregate()
				.transform(Object::toString)
				.get();
	}

//	public Executor executor() {
//		ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
//		threadPoolExecutor.setCorePoolSize(10);
//		threadPoolExecutor.initialize();
//		return threadPoolExecutor;
//	}

	@Bean
	public IntegrationFlow amqpInboundFlow(ConnectionFactory connectionFactory) {
		return IntegrationFlow.from(Amqp.inboundGateway(connectionFactory, "foo"))
				.route("payload.substring(0, 3)", r -> r
						.resolutionRequired(false)
						.subFlowMapping("foo", s -> s.<String, String>transform(String::toUpperCase))
						.subFlowMapping("bar", s -> s.<String, String>transform(p -> p + p))
				).get();
	}

	@Bean
	public DefaultMessageSplitter commaSplitter() {
		DefaultMessageSplitter splitter = new DefaultMessageSplitter();
		splitter.setDelimiters(",");
		return splitter;
	}
}
