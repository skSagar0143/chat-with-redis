package com.chatwithredis.config;
import com.chatwithredis.service.RedisChatSubscriber;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.*;
@Configuration
public class RedisPubSubConfig {
 @Bean ChannelTopic chatTopic(){return new ChannelTopic("chat:messages");}
 @Bean RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory,RedisChatSubscriber subscriber,ChannelTopic chatTopic){
  RedisMessageListenerContainer c=new RedisMessageListenerContainer();c.setConnectionFactory(factory);c.addMessageListener(subscriber,chatTopic);return c;
 }
}