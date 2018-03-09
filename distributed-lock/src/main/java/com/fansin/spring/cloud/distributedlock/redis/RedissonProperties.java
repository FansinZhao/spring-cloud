package com.fansin.spring.cloud.distributedlock.redis;

import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * The type Redisson properties.
 *
 * @author fansin
 * @version 1.0
 * @date 18-3-9 上午8:04
 */
@ConfigurationProperties(prefix = "redisson")
@Configuration
public class RedissonProperties implements BeanClassLoaderAware, InitializingBean {

    private String address;
    private int connectionMinimumIdleSize = 10;
    private int idleConnectionTimeout = 10000;
    private int pingTimeout = 1000;
    private int connectTimeout = 10000;
    private int timeout = 3000;
    private int retryAttempts = 3;
    private int retryInterval = 1500;
    private int reconnectionTimeout = 3000;
    private int failedAttempts = 3;
    private String password = null;
    private int subscriptionsPerConnection = 5;
    private String clientName = null;
    private int subscriptionConnectionMinimumIdleSize = 1;
    private int subscriptionConnectionPoolSize = 50;
    private int connectionPoolSize = 64;
    private int database = 0;
    private boolean dnsMonitoring = false;
    private int dnsMonitoringInterval = 5000;
    /**
     * 当前处理核数量 * 2
     */
    private int thread;
    private ClassLoader classLoader;
    private String codec = "org.redisson.codec.JsonJacksonCodec";

    /**
     * Getter for property 'address'.
     *
     * @return Value for property 'address'.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter for property 'address'.
     *
     * @param address Value to set for property 'address'.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter for property 'connectionMinimumIdleSize'.
     *
     * @return Value for property 'connectionMinimumIdleSize'.
     */
    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    /**
     * Setter for property 'connectionMinimumIdleSize'.
     *
     * @param connectionMinimumIdleSize Value to set for property 'connectionMinimumIdleSize'.
     */
    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    /**
     * Getter for property 'idleConnectionTimeout'.
     *
     * @return Value for property 'idleConnectionTimeout'.
     */
    public int getIdleConnectionTimeout() {
        return idleConnectionTimeout;
    }

    /**
     * Setter for property 'idleConnectionTimeout'.
     *
     * @param idleConnectionTimeout Value to set for property 'idleConnectionTimeout'.
     */
    public void setIdleConnectionTimeout(int idleConnectionTimeout) {
        this.idleConnectionTimeout = idleConnectionTimeout;
    }

    /**
     * Getter for property 'pingTimeout'.
     *
     * @return Value for property 'pingTimeout'.
     */
    public int getPingTimeout() {
        return pingTimeout;
    }

    /**
     * Setter for property 'pingTimeout'.
     *
     * @param pingTimeout Value to set for property 'pingTimeout'.
     */
    public void setPingTimeout(int pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    /**
     * Getter for property 'connectTimeout'.
     *
     * @return Value for property 'connectTimeout'.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Setter for property 'connectTimeout'.
     *
     * @param connectTimeout Value to set for property 'connectTimeout'.
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Getter for property 'timeout'.
     *
     * @return Value for property 'timeout'.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Setter for property 'timeout'.
     *
     * @param timeout Value to set for property 'timeout'.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Getter for property 'retryAttempts'.
     *
     * @return Value for property 'retryAttempts'.
     */
    public int getRetryAttempts() {
        return retryAttempts;
    }

    /**
     * Setter for property 'retryAttempts'.
     *
     * @param retryAttempts Value to set for property 'retryAttempts'.
     */
    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    /**
     * Getter for property 'retryInterval'.
     *
     * @return Value for property 'retryInterval'.
     */
    public int getRetryInterval() {
        return retryInterval;
    }

    /**
     * Setter for property 'retryInterval'.
     *
     * @param retryInterval Value to set for property 'retryInterval'.
     */
    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * Getter for property 'reconnectionTimeout'.
     *
     * @return Value for property 'reconnectionTimeout'.
     */
    public int getReconnectionTimeout() {
        return reconnectionTimeout;
    }

    /**
     * Setter for property 'reconnectionTimeout'.
     *
     * @param reconnectionTimeout Value to set for property 'reconnectionTimeout'.
     */
    public void setReconnectionTimeout(int reconnectionTimeout) {
        this.reconnectionTimeout = reconnectionTimeout;
    }

    /**
     * Getter for property 'failedAttempts'.
     *
     * @return Value for property 'failedAttempts'.
     */
    public int getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * Setter for property 'failedAttempts'.
     *
     * @param failedAttempts Value to set for property 'failedAttempts'.
     */
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    /**
     * Getter for property 'password'.
     *
     * @return Value for property 'password'.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for property 'password'.
     *
     * @param password Value to set for property 'password'.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for property 'subscriptionsPerConnection'.
     *
     * @return Value for property 'subscriptionsPerConnection'.
     */
    public int getSubscriptionsPerConnection() {
        return subscriptionsPerConnection;
    }

    /**
     * Setter for property 'subscriptionsPerConnection'.
     *
     * @param subscriptionsPerConnection Value to set for property 'subscriptionsPerConnection'.
     */
    public void setSubscriptionsPerConnection(int subscriptionsPerConnection) {
        this.subscriptionsPerConnection = subscriptionsPerConnection;
    }

    /**
     * Getter for property 'clientName'.
     *
     * @return Value for property 'clientName'.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Setter for property 'clientName'.
     *
     * @param clientName Value to set for property 'clientName'.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Getter for property 'subscriptionConnectionMinimumIdleSize'.
     *
     * @return Value for property 'subscriptionConnectionMinimumIdleSize'.
     */
    public int getSubscriptionConnectionMinimumIdleSize() {
        return subscriptionConnectionMinimumIdleSize;
    }

    /**
     * Setter for property 'subscriptionConnectionMinimumIdleSize'.
     *
     * @param subscriptionConnectionMinimumIdleSize Value to set for property 'subscriptionConnectionMinimumIdleSize'.
     */
    public void setSubscriptionConnectionMinimumIdleSize(int subscriptionConnectionMinimumIdleSize) {
        this.subscriptionConnectionMinimumIdleSize = subscriptionConnectionMinimumIdleSize;
    }

    /**
     * Getter for property 'subscriptionConnectionPoolSize'.
     *
     * @return Value for property 'subscriptionConnectionPoolSize'.
     */
    public int getSubscriptionConnectionPoolSize() {
        return subscriptionConnectionPoolSize;
    }

    /**
     * Setter for property 'subscriptionConnectionPoolSize'.
     *
     * @param subscriptionConnectionPoolSize Value to set for property 'subscriptionConnectionPoolSize'.
     */
    public void setSubscriptionConnectionPoolSize(int subscriptionConnectionPoolSize) {
        this.subscriptionConnectionPoolSize = subscriptionConnectionPoolSize;
    }

    /**
     * Getter for property 'connectionPoolSize'.
     *
     * @return Value for property 'connectionPoolSize'.
     */
    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    /**
     * Setter for property 'connectionPoolSize'.
     *
     * @param connectionPoolSize Value to set for property 'connectionPoolSize'.
     */
    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    /**
     * Getter for property 'database'.
     *
     * @return Value for property 'database'.
     */
    public int getDatabase() {
        return database;
    }

    /**
     * Setter for property 'database'.
     *
     * @param database Value to set for property 'database'.
     */
    public void setDatabase(int database) {
        this.database = database;
    }

    /**
     * Getter for property 'dnsMonitoring'.
     *
     * @return Value for property 'dnsMonitoring'.
     */
    public boolean isDnsMonitoring() {
        return dnsMonitoring;
    }

    /**
     * Setter for property 'dnsMonitoring'.
     *
     * @param dnsMonitoring Value to set for property 'dnsMonitoring'.
     */
    public void setDnsMonitoring(boolean dnsMonitoring) {
        this.dnsMonitoring = dnsMonitoring;
    }

    /**
     * Getter for property 'dnsMonitoringInterval'.
     *
     * @return Value for property 'dnsMonitoringInterval'.
     */
    public int getDnsMonitoringInterval() {
        return dnsMonitoringInterval;
    }

    /**
     * Setter for property 'dnsMonitoringInterval'.
     *
     * @param dnsMonitoringInterval Value to set for property 'dnsMonitoringInterval'.
     */
    public void setDnsMonitoringInterval(int dnsMonitoringInterval) {
        this.dnsMonitoringInterval = dnsMonitoringInterval;
    }

    /**
     * Getter for property 'thread'.
     *
     * @return Value for property 'thread'.
     */
    public int getThread() {
        return thread;
    }

    /**
     * Setter for property 'thread'.
     *
     * @param thread Value to set for property 'thread'.
     */
    public void setThread(int thread) {
        this.thread = thread;
    }

    /**
     * Getter for property 'codec'.
     *
     * @return Value for property 'codec'.
     */
    public String getCodec() {
        return codec;
    }

    /**
     * Setter for property 'codec'.
     *
     * @param codec Value to set for property 'codec'.
     */
    public void setCodec(String codec) {
        this.codec = codec;
    }

    /**
     * Getter for property 'classLoader'.
     *
     * @return Value for property 'classLoader'.
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Setter for property 'classLoader'.
     *
     * @param classLoader Value to set for property 'classLoader'.
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void afterPropertiesSet() {
        //
    }

    /**
     * TODO 这里还需要优化一下,参考springboot 其他自动配置文件
     *
     * @return config
     */
    public Config build() {
        System.out.println("!!!请完善配置!!!");
        Config config = new Config();
        //单机模式,集群模式和哨兵模式类似, TODO 下次使用补上
        config.useSingleServer().setAddress(address)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectionPoolSize(connectionPoolSize)
                .setDatabase(database)
                .setDnsMonitoring(dnsMonitoring)
                .setDnsMonitoringInterval(dnsMonitoringInterval)
                .setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
                .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setFailedAttempts(failedAttempts)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setReconnectionTimeout(reconnectionTimeout)
                .setTimeout(timeout)
                .setConnectTimeout(connectTimeout)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout)
                .setPassword(password);
        Codec codec = null;
        try {
            codec = (Codec) ClassUtils.forName(getCodec(), ClassUtils.getDefaultClassLoader()).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        config.setCodec(codec);
        config.setThreads(thread);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return config;
    }
}