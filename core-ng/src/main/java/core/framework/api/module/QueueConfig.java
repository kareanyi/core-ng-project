package core.framework.api.module;

import core.framework.api.queue.MessagePublisher;
import core.framework.api.util.Exceptions;
import core.framework.api.util.Types;
import core.framework.impl.module.AWSQueueBuilder;
import core.framework.impl.module.ModuleContext;
import core.framework.impl.module.RabbitMQQueueBuilder;
import core.framework.impl.queue.MessageValidator;
import core.framework.impl.queue.MockMessagePublisher;
import core.framework.impl.queue.RabbitMQ;
import core.framework.impl.queue.RabbitMQEndpoint;

/**
 * @author neo
 */
public class QueueConfig {
    private final ModuleContext context;

    public QueueConfig(ModuleContext context) {
        this.context = context;
    }

    public RabbitMQConfig rabbitMQ() {
        if (context.queueManager.rabbitMQ == null) {
            context.queueManager.rabbitMQ = new RabbitMQ();
            if (!context.test) {
                context.shutdownHook.add(context.queueManager.rabbitMQ::shutdown);
            }
        }
        return new RabbitMQConfig(context);
    }

    public MessageHandlerConfig subscribe(String queueURI) {
        if (context.beanFactory.registered(MessageHandlerConfig.class, queueURI)) {
            return context.beanFactory.bean(MessageHandlerConfig.class, queueURI);
        } else {
            MessageHandlerConfig listener = listener(queueURI);
            context.beanFactory.bind(MessageHandlerConfig.class, queueURI, listener);
            return listener;
        }
    }

    public <T> void publish(String destination, Class<T> messageClass) {
        MessageValidator validator = context.queueManager.validator();
        validator.register(messageClass);
        MessagePublisher<T> publisher = publisher(destination, messageClass);
        context.beanFactory.bind(Types.generic(MessagePublisher.class, messageClass), null, publisher);
    }

    private MessageHandlerConfig listener(String queueURI) {
        if (queueURI.startsWith("https://sqs.")) {
            return new AWSQueueBuilder(context).listener(queueURI);
        } else if (queueURI.startsWith("rabbitmq://queue/")) {
            return new RabbitMQQueueBuilder(context).listener(new RabbitMQEndpoint(queueURI).routingKey);
        } else {
            throw Exceptions.error("unsupported protocol, queueURI={}", queueURI);
        }
    }

    private <T> MessagePublisher<T> publisher(String uri, Class<T> messageClass) {
        if (context.test || uri.startsWith("mock://")) {
            return new MockMessagePublisher<>(uri, context.queueManager.validator());
        } else if (uri.startsWith("arn:aws:sns:")) {
            return new AWSQueueBuilder(context).snsPublisher(uri, messageClass);
        } else if (uri.startsWith("https://sqs.")) {
            return new AWSQueueBuilder(context).sqsPublisher(uri, messageClass);
        } else if (uri.startsWith("rabbitmq://")) {
            return new RabbitMQQueueBuilder(context).publisher(new RabbitMQEndpoint(uri), messageClass, context.queueManager.validator());
        } else {
            throw Exceptions.error("unsupported protocol, uri={}", uri);
        }
    }
}