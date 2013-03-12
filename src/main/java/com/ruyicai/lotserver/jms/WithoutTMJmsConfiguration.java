/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruyicai.lotserver.jms;

import static org.apache.camel.component.jms.JmsMessageHelper.normalizeDestinationName;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.jms.DefaultJmsKeyFormatStrategy;
import org.apache.camel.component.jms.DestinationEndpoint;
import org.apache.camel.component.jms.EndpointMessageListener;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.component.jms.JmsKeyFormatStrategy;
import org.apache.camel.component.jms.JmsMessageListenerContainer;
import org.apache.camel.component.jms.JmsMessageType;
import org.apache.camel.component.jms.JmsProviderMetadata;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @version 
 */
public class WithoutTMJmsConfiguration extends JmsConfiguration {

    public static final String QUEUE_PREFIX = "queue:";
    public static final String TOPIC_PREFIX = "topic:";
    public static final String TEMP_QUEUE_PREFIX = "temp:queue:";
    public static final String TEMP_TOPIC_PREFIX = "temp:topic:";

    private static final transient Logger LOG = LoggerFactory.getLogger(WithoutTMJmsConfiguration.class);
    private JmsOperations jmsOperations;
    private DestinationResolver destinationResolver;
    private ConnectionFactory connectionFactory;
    private ConnectionFactory templateConnectionFactory;
    private ConnectionFactory listenerConnectionFactory;
    private int acknowledgementMode = -1;
    private String acknowledgementModeName;
    // Used to configure the spring Container
    private ExceptionListener exceptionListener;
    private boolean autoStartup = true;
    private boolean acceptMessagesWhileStopping;
    private String clientId;
    private String durableSubscriptionName;
    private boolean subscriptionDurable;
    private boolean exposeListenerSession = true;
    private TaskExecutor taskExecutor;
    private boolean pubSubNoLocal;
    private int concurrentConsumers = 1;
    private int maxMessagesPerTask = -1;
    private int cacheLevel = -1;
    private String cacheLevelName;
    private long recoveryInterval = -1;
    private long receiveTimeout = -1;
    private long requestTimeout = 20000L;
    private int idleTaskExecutionLimit = 1;
    private int maxConcurrentConsumers;
    // JmsTemplate only
    private Boolean explicitQosEnabled;
    private boolean deliveryPersistent = true;
    private boolean replyToDeliveryPersistent = true;
    private long timeToLive = -1;
    private MessageConverter messageConverter;
    private boolean mapJmsMessage = true;
    private boolean messageIdEnabled = true;
    private boolean messageTimestampEnabled = true;
    private int priority = -1;
    // Transaction related configuration
    private boolean transacted;
    private boolean transactedInOut;
    private boolean lazyCreateTransactionManager = true;
    private PlatformTransactionManager transactionManager;
    private String transactionName;
    private int transactionTimeout = -1;
    private boolean preserveMessageQos;
    private boolean disableReplyTo;
    private boolean eagerLoadingOfProperties;
    // Always make a JMS message copy when it's passed to Producer
    private boolean alwaysCopyMessage;
    private boolean useMessageIDAsCorrelationID;
    private JmsProviderMetadata providerMetadata = new JmsProviderMetadata();
    private JmsOperations metadataJmsOperations;
    private String replyToDestination;
    private String replyToDestinationSelectorName;
    private JmsMessageType jmsMessageType;
    private JmsKeyFormatStrategy jmsKeyFormatStrategy;
    private boolean transferExchange;
    private boolean transferException;
    private boolean testConnectionOnStartup;
    // if the message is a JmsMessage and mapJmsMessage=false, force the 
    // producer to send the javax.jms.Message body to the next JMS destination    
    private boolean forceSendOriginalMessage;
    // to force disabling time to live (works in both in-only or in-out mode)
    private boolean disableTimeToLive;

    public WithoutTMJmsConfiguration() {
    	LOG.info("use WithoutTMJmsConfiguration");
    }

    public WithoutTMJmsConfiguration(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Returns a copy of this configuration
     */
    public JmsConfiguration copy() {
        try {
            return (JmsConfiguration) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }

    /**
     * Creates a {@link JmsOperations} object used for request/response using a request timeout value
     */
    public JmsOperations createInOutTemplate(JmsEndpoint endpoint, boolean pubSubDomain, String destination, long requestTimeout) {
        JmsOperations answer = createInOnlyTemplate(endpoint, pubSubDomain, destination);
        if (answer instanceof JmsTemplate && requestTimeout > 0) {
            JmsTemplate jmsTemplate = (JmsTemplate) answer;
            jmsTemplate.setExplicitQosEnabled(true);

            // prefer to use timeToLive over requestTimeout if both specified
            long ttl = timeToLive > 0 ? timeToLive : requestTimeout;
            if (ttl > 0 && !isDisableTimeToLive()) {
                // only use TTL if not disabled
                jmsTemplate.setTimeToLive(ttl);
            }

            jmsTemplate.setSessionTransacted(isTransactedInOut());
            if (isTransactedInOut()) {
                jmsTemplate.setSessionAcknowledgeMode(Session.SESSION_TRANSACTED);
            } else {
                if (acknowledgementMode >= 0) {
                    jmsTemplate.setSessionAcknowledgeMode(acknowledgementMode);
                } else if (acknowledgementModeName != null) {
                    jmsTemplate.setSessionAcknowledgeModeName(acknowledgementModeName);
                } else {
                    // default to AUTO
                    jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
                }
            }
        }
        return answer;
    }

    /**
     * Creates a {@link JmsOperations} object used for one way messaging
     */
    public JmsOperations createInOnlyTemplate(JmsEndpoint endpoint, boolean pubSubDomain, String destination) {
        if (jmsOperations != null) {
            return jmsOperations;
        }

        ConnectionFactory factory = getTemplateConnectionFactory();
        JmsTemplate template = new CamelJmsTemplate(this, factory);

        template.setPubSubDomain(pubSubDomain);
        if (destinationResolver != null) {
            template.setDestinationResolver(destinationResolver);
            if (endpoint instanceof DestinationEndpoint) {
                LOG.debug("You are overloading the destinationResolver property on a DestinationEndpoint; are you sure you want to do that?");
            }
        } else if (endpoint instanceof DestinationEndpoint) {
            DestinationEndpoint destinationEndpoint = (DestinationEndpoint) endpoint;
            template.setDestinationResolver(createDestinationResolver(destinationEndpoint));
        }
        template.setDefaultDestinationName(destination);

        template.setExplicitQosEnabled(isExplicitQosEnabled());
        template.setDeliveryPersistent(deliveryPersistent);
        if (messageConverter != null) {
            template.setMessageConverter(messageConverter);
        }
        template.setMessageIdEnabled(messageIdEnabled);
        template.setMessageTimestampEnabled(messageTimestampEnabled);
        if (priority >= 0) {
            template.setPriority(priority);
        }
        template.setPubSubNoLocal(pubSubNoLocal);
        if (receiveTimeout >= 0) {
            template.setReceiveTimeout(receiveTimeout);
        }
        // only set TTL if we have a positive value and it has not been disabled
        if (timeToLive >= 0 && !isDisableTimeToLive()) {
            template.setTimeToLive(timeToLive);
        }

        template.setSessionTransacted(transacted);
        if (transacted) {
            template.setSessionAcknowledgeMode(Session.SESSION_TRANSACTED);
        } else {
            // This is here for completeness, but the template should not get
            // used for receiving messages.
            if (acknowledgementMode >= 0) {
                template.setSessionAcknowledgeMode(acknowledgementMode);
            } else if (acknowledgementModeName != null) {
                template.setSessionAcknowledgeModeName(acknowledgementModeName);
            }
        }
        return template;
    }

    public DefaultMessageListenerContainer createMessageListenerContainer(JmsEndpoint endpoint) throws Exception {
        DefaultMessageListenerContainer container = new JmsMessageListenerContainer(endpoint);
        configureMessageListenerContainer(container, endpoint);
        return container;
    }


    // Properties
    // -------------------------------------------------------------------------

    public ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            connectionFactory = createConnectionFactory();
        }
        return connectionFactory;
    }

    /**
     * Sets the default connection factory to be used if a connection factory is
     * not specified for either
     * {@link #setTemplateConnectionFactory(ConnectionFactory)} or
     * {@link #setListenerConnectionFactory(ConnectionFactory)}
     *
     * @param connectionFactory the default connection factory to use
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ConnectionFactory getListenerConnectionFactory() {
        if (listenerConnectionFactory == null) {
            listenerConnectionFactory = createListenerConnectionFactory();
        }
        return listenerConnectionFactory;
    }

    /**
     * Sets the connection factory to be used for consuming messages
     *
     * @param listenerConnectionFactory the connection factory to use for
     *                                  consuming messages
     */
    public void setListenerConnectionFactory(ConnectionFactory listenerConnectionFactory) {
        this.listenerConnectionFactory = listenerConnectionFactory;
    }

    public ConnectionFactory getTemplateConnectionFactory() {
        if (templateConnectionFactory == null) {
            templateConnectionFactory = createTemplateConnectionFactory();
        }
        return templateConnectionFactory;
    }

    /**
     * Sets the connection factory to be used for sending messages via the
     * {@link JmsTemplate} via {@link #createInOnlyTemplate(JmsEndpoint,boolean, String)}
     *
     * @param templateConnectionFactory the connection factory for sending messages
     */
    public void setTemplateConnectionFactory(ConnectionFactory templateConnectionFactory) {
        this.templateConnectionFactory = templateConnectionFactory;
    }

    public boolean isAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public boolean isAcceptMessagesWhileStopping() {
        return acceptMessagesWhileStopping;
    }

    public void setAcceptMessagesWhileStopping(boolean acceptMessagesWhileStopping) {
        this.acceptMessagesWhileStopping = acceptMessagesWhileStopping;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String consumerClientId) {
        this.clientId = consumerClientId;
    }

    public String getDurableSubscriptionName() {
        return durableSubscriptionName;
    }

    public void setDurableSubscriptionName(String durableSubscriptionName) {
        this.durableSubscriptionName = durableSubscriptionName;
    }

    public ExceptionListener getExceptionListener() {
        return exceptionListener;
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    @Deprecated
    public boolean isSubscriptionDurable() {
        return subscriptionDurable;
    }

    @Deprecated
    public void setSubscriptionDurable(boolean subscriptionDurable) {
        this.subscriptionDurable = subscriptionDurable;
    }

    public String getAcknowledgementModeName() {
        return acknowledgementModeName;
    }

    public void setAcknowledgementModeName(String consumerAcknowledgementMode) {
        this.acknowledgementModeName = consumerAcknowledgementMode;
        this.acknowledgementMode = -1;
    }

    public boolean isExposeListenerSession() {
        return exposeListenerSession;
    }

    public void setExposeListenerSession(boolean exposeListenerSession) {
        this.exposeListenerSession = exposeListenerSession;
    }

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public boolean isPubSubNoLocal() {
        return pubSubNoLocal;
    }

    public void setPubSubNoLocal(boolean pubSubNoLocal) {
        this.pubSubNoLocal = pubSubNoLocal;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public int getMaxMessagesPerTask() {
        return maxMessagesPerTask;
    }

    public void setMaxMessagesPerTask(int maxMessagesPerTask) {
        this.maxMessagesPerTask = maxMessagesPerTask;
    }

    public int getCacheLevel() {
        return cacheLevel;
    }

    public void setCacheLevel(int cacheLevel) {
        this.cacheLevel = cacheLevel;
    }

    public String getCacheLevelName() {
        return cacheLevelName;
    }

    public void setCacheLevelName(String cacheName) {
        this.cacheLevelName = cacheName;
    }

    public long getRecoveryInterval() {
        return recoveryInterval;
    }

    public void setRecoveryInterval(long recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null && isTransacted() && isLazyCreateTransactionManager()) {
            transactionManager = createTransactionManager();
        }
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public int getTransactionTimeout() {
        return transactionTimeout;
    }

    public void setTransactionTimeout(int transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    public int getIdleTaskExecutionLimit() {
        return idleTaskExecutionLimit;
    }

    public void setIdleTaskExecutionLimit(int idleTaskExecutionLimit) {
        this.idleTaskExecutionLimit = idleTaskExecutionLimit;
    }

    public int getMaxConcurrentConsumers() {
        return maxConcurrentConsumers;
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
        this.maxConcurrentConsumers = maxConcurrentConsumers;
    }

    public boolean isExplicitQosEnabled() {
        return explicitQosEnabled != null ? explicitQosEnabled : false;
    }

    public void setExplicitQosEnabled(boolean explicitQosEnabled) {
        this.explicitQosEnabled = explicitQosEnabled;
    }

    public boolean isDeliveryPersistent() {
        return deliveryPersistent;
    }

    public void setDeliveryPersistent(boolean deliveryPersistent) {
        this.deliveryPersistent = deliveryPersistent;
        configuredQoS();
    }

    public boolean isReplyToDeliveryPersistent() {
        return replyToDeliveryPersistent;
    }

    public void setReplyToDeliveryPersistent(boolean replyToDeliveryPersistent) {
        this.replyToDeliveryPersistent = replyToDeliveryPersistent;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
        configuredQoS();
    }

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public boolean isMapJmsMessage() {
        return mapJmsMessage;
    }

    public void setMapJmsMessage(boolean mapJmsMessage) {
        this.mapJmsMessage = mapJmsMessage;
    }

    public boolean isMessageIdEnabled() {
        return messageIdEnabled;
    }

    public void setMessageIdEnabled(boolean messageIdEnabled) {
        this.messageIdEnabled = messageIdEnabled;
    }

    public boolean isMessageTimestampEnabled() {
        return messageTimestampEnabled;
    }

    public void setMessageTimestampEnabled(boolean messageTimestampEnabled) {
        this.messageTimestampEnabled = messageTimestampEnabled;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
        configuredQoS();
    }

    public int getAcknowledgementMode() {
        return acknowledgementMode;
    }

    public void setAcknowledgementMode(int consumerAcknowledgementMode) {
        this.acknowledgementMode = consumerAcknowledgementMode;
        this.acknowledgementModeName = null;
    }

    public boolean isTransacted() {
        return transacted;
    }

    public void setTransacted(boolean consumerTransacted) {
        this.transacted = consumerTransacted;
    }

    /**
     * Should InOut operations (request reply) default to using transacted mode?
     * <p/>
     * By default this is false as you need to commit the outgoing request before you can consume the input
     */
    @Deprecated
    public boolean isTransactedInOut() {
        return transactedInOut;
    }

    @Deprecated
    public void setTransactedInOut(boolean transactedInOut) {
        this.transactedInOut = transactedInOut;
    }

    public boolean isLazyCreateTransactionManager() {
        return lazyCreateTransactionManager;
    }

    public void setLazyCreateTransactionManager(boolean lazyCreating) {
        this.lazyCreateTransactionManager = lazyCreating;
    }

    public boolean isEagerLoadingOfProperties() {
        return eagerLoadingOfProperties;
    }

    /**
     * Enables eager loading of JMS properties as soon as a message is loaded
     * which generally is inefficient as the JMS properties may not be required
     * but sometimes can catch early any issues with the underlying JMS provider
     * and the use of JMS properties
     *
     * @param eagerLoadingOfProperties whether or not to enable eager loading of
     *                                 JMS properties on inbound messages
     */
    public void setEagerLoadingOfProperties(boolean eagerLoadingOfProperties) {
        this.eagerLoadingOfProperties = eagerLoadingOfProperties;
    }

    public boolean isDisableReplyTo() {
        return disableReplyTo;
    }

    /**
     * Disables the use of the JMSReplyTo header for consumers so that inbound
     * messages are treated as InOnly rather than InOut requests.
     *
     * @param disableReplyTo whether or not to disable the use of JMSReplyTo
     *                       header indicating an InOut
     */
    public void setDisableReplyTo(boolean disableReplyTo) {
        this.disableReplyTo = disableReplyTo;
    }

    /**
     * Set to true if you want to send message using the QoS settings specified
     * on the message. Normally the QoS settings used are the one configured on
     * this Object.
     */
    public void setPreserveMessageQos(boolean preserveMessageQos) {
        this.preserveMessageQos = preserveMessageQos;
    }

    public JmsOperations getJmsOperations() {
        return jmsOperations;
    }

    public void setJmsOperations(JmsOperations jmsOperations) {
        this.jmsOperations = jmsOperations;
    }

    public DestinationResolver getDestinationResolver() {
        return destinationResolver;
    }

    public void setDestinationResolver(DestinationResolver destinationResolver) {
        this.destinationResolver = destinationResolver;
    }

    public JmsProviderMetadata getProviderMetadata() {
        return providerMetadata;
    }

    /**
     * Allows the provider metadata to be explicitly configured. Typically this is not required
     * and Camel will auto-detect the provider metadata from the underlying provider.
     */
    public void setProviderMetadata(JmsProviderMetadata providerMetadata) {
        this.providerMetadata = providerMetadata;
    }

    public JmsOperations getMetadataJmsOperations(JmsEndpoint endpoint) {
        if (metadataJmsOperations == null) {
            metadataJmsOperations = getJmsOperations();
            if (metadataJmsOperations == null) {
                metadataJmsOperations = createInOnlyTemplate(endpoint, false, null);
            }
        }
        return metadataJmsOperations;
    }

    /**
     * Sets the {@link JmsOperations} used to deduce the {@link JmsProviderMetadata} details which if none
     * is customized one is lazily created on demand
     */
    public void setMetadataJmsOperations(JmsOperations metadataJmsOperations) {
        this.metadataJmsOperations = metadataJmsOperations;
    }


    // Implementation methods
    // -------------------------------------------------------------------------

    public static DestinationResolver createDestinationResolver(final DestinationEndpoint destinationEndpoint) {
        return new DestinationResolver() {
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
                return destinationEndpoint.getJmsDestination(session);
            }
        };
    }


    protected void configureMessageListenerContainer(DefaultMessageListenerContainer container,
                                                     JmsEndpoint endpoint) throws Exception {
        container.setConnectionFactory(getListenerConnectionFactory());
        if (endpoint instanceof DestinationEndpoint) {
            container.setDestinationResolver(createDestinationResolver((DestinationEndpoint) endpoint));
        } else if (destinationResolver != null) {
            container.setDestinationResolver(destinationResolver);
        }
        container.setAutoStartup(autoStartup);

        if (durableSubscriptionName != null) {
            container.setDurableSubscriptionName(durableSubscriptionName);
            container.setSubscriptionDurable(true);
        }
        if (clientId != null) {
            container.setClientId(clientId);
        }

        if (exceptionListener != null) {
            container.setExceptionListener(exceptionListener);
        }

        container.setAcceptMessagesWhileStopping(acceptMessagesWhileStopping);
        container.setExposeListenerSession(exposeListenerSession);
        container.setSessionTransacted(transacted);

        if (endpoint.getSelector() != null && endpoint.getSelector().length() != 0) {
            container.setMessageSelector(endpoint.getSelector());
        }

        if (concurrentConsumers >= 0) {
            container.setConcurrentConsumers(concurrentConsumers);
        }

        if (cacheLevel >= 0) {
            container.setCacheLevel(cacheLevel);
        } else if (cacheLevelName != null) {
            container.setCacheLevelName(cacheLevelName);
        } else {
            container.setCacheLevel(defaultCacheLevel(endpoint));
        }

        if (idleTaskExecutionLimit >= 0) {
            container.setIdleTaskExecutionLimit(idleTaskExecutionLimit);
        }
        if (maxConcurrentConsumers > 0) {
            if (maxConcurrentConsumers < concurrentConsumers) {
                throw new IllegalArgumentException("Property maxConcurrentConsumers: " + maxConcurrentConsumers
                        + " must be higher than concurrentConsumers: " + concurrentConsumers);
            }
            container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        }
        if (maxMessagesPerTask >= 0) {
            container.setMaxMessagesPerTask(maxMessagesPerTask);
        }
        container.setPubSubNoLocal(pubSubNoLocal);
        if (receiveTimeout >= 0) {
            container.setReceiveTimeout(receiveTimeout);
        }
        if (recoveryInterval >= 0) {
            container.setRecoveryInterval(recoveryInterval);
        }
        if (taskExecutor != null) {
            container.setTaskExecutor(taskExecutor);
        }
        if (transactionName != null) {
            container.setTransactionName(transactionName);
        }
        if (transactionTimeout >= 0) {
            container.setTransactionTimeout(transactionTimeout);
        }
    }

    public void configure(EndpointMessageListener listener) {
        if (isDisableReplyTo()) {
            listener.setDisableReplyTo(true);
        }
        if (isEagerLoadingOfProperties()) {
            listener.setEagerLoadingOfProperties(true);
        }
        if (getReplyTo() != null) {
            listener.setReplyToDestination(getReplyTo());
        }

        // TODO: REVISIT: We really ought to change the model and let JmsProducer
        // and JmsConsumer have their own JmsConfiguration instance
        // This way producer's and consumer's QoS can differ and be
        // independently configured
        JmsOperations operations = listener.getTemplate();
        if (operations instanceof JmsTemplate) {
            JmsTemplate template = (JmsTemplate) operations;
            template.setDeliveryPersistent(isReplyToDeliveryPersistent());
        }
    }

    /**
     * Defaults the JMS cache level if none is explicitly specified.
     * <p/>
     * Will return <tt>CACHE_AUTO</tt> which will pickup and use <tt>CACHE_NONE</tt>
     * if transacted has been enabled, otherwise it will use <tt>CACHE_CONSUMER</tt>
     * which is the most efficient.
     *
     * @param endpoint the endpoint
     * @return the cache level
     */
    protected int defaultCacheLevel(JmsEndpoint endpoint) {
        return DefaultMessageListenerContainer.CACHE_AUTO;
    }

    /**
     * Factory method which allows derived classes to customize the lazy
     * creation
     */
    protected ConnectionFactory createConnectionFactory() {
        ObjectHelper.notNull(connectionFactory, "connectionFactory");
        return null;
    }

    /**
     * Factory method which allows derived classes to customize the lazy
     * creation
     */
    protected ConnectionFactory createListenerConnectionFactory() {
        return getConnectionFactory();
    }

    /**
     * Factory method which allows derived classes to customize the lazy
     * creation
     */
    protected ConnectionFactory createTemplateConnectionFactory() {
        return getConnectionFactory();
    }

    /**
     * Factory method which which allows derived classes to customize the lazy
     * transcationManager creation
     */
    protected PlatformTransactionManager createTransactionManager() {
        JmsTransactionManager answer = new JmsTransactionManager();
        answer.setConnectionFactory(getConnectionFactory());
        return answer;
    }

    public boolean isPreserveMessageQos() {
        return preserveMessageQos;
    }

    /**
     * When one of the QoS properties are configured such as {@link #setDeliveryPersistent(boolean)},
     * {@link #setPriority(int)} or {@link #setTimeToLive(long)} then we should auto default the
     * setting of {@link #setExplicitQosEnabled(boolean)} if its not been configured yet
     */
    protected void configuredQoS() {
        if (explicitQosEnabled == null) {
            explicitQosEnabled = true;
        }
    }


    public boolean isAlwaysCopyMessage() {
        return alwaysCopyMessage;
    }

    public void setAlwaysCopyMessage(boolean alwaysCopyMessage) {
        this.alwaysCopyMessage = alwaysCopyMessage;
    }

    public boolean isUseMessageIDAsCorrelationID() {
        return useMessageIDAsCorrelationID;
    }

    public void setUseMessageIDAsCorrelationID(boolean useMessageIDAsCorrelationID) {
        this.useMessageIDAsCorrelationID = useMessageIDAsCorrelationID;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * Sets the timeout in milliseconds which requests should timeout after
     */
    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getReplyTo() {
        return replyToDestination;
    }

    public void setReplyTo(String replyToDestination) {
        this.replyToDestination = normalizeDestinationName(replyToDestination);
    }

    public String getReplyToDestinationSelectorName() {
        return replyToDestinationSelectorName;
    }

    public void setReplyToDestinationSelectorName(String replyToDestinationSelectorName) {
        this.replyToDestinationSelectorName = replyToDestinationSelectorName;
        // in case of consumer -> producer and a named replyTo correlation selector
        // message pass through is impossible as we need to set the value of selector into
        // outgoing message, which would be read-only if pass through were to remain enabled
        if (replyToDestinationSelectorName != null) {
            setAlwaysCopyMessage(true);
        }
    }

    public JmsMessageType getJmsMessageType() {
        return jmsMessageType;
    }

    public void setJmsMessageType(JmsMessageType jmsMessageType) {
        if (jmsMessageType == JmsMessageType.Blob && !supportBlobMessage()) {
            throw new IllegalArgumentException("BlobMessage is not supported by this implementation");
        }
        this.jmsMessageType = jmsMessageType;
    }

    /**
     * Should get overridden by implementations which support BlobMessages
     *
     * @return false
     */
    protected boolean supportBlobMessage() {
        return false;
    }

    public JmsKeyFormatStrategy getJmsKeyFormatStrategy() {
        if (jmsKeyFormatStrategy == null) {
            jmsKeyFormatStrategy = new DefaultJmsKeyFormatStrategy();
        }
        return jmsKeyFormatStrategy;
    }

    public void setJmsKeyFormatStrategy(JmsKeyFormatStrategy jmsKeyFormatStrategy) {
        this.jmsKeyFormatStrategy = jmsKeyFormatStrategy;
    }

    public boolean isTransferExchange() {
        return transferExchange;
    }

    public void setTransferExchange(boolean transferExchange) {
        this.transferExchange = transferExchange;
    }

    public boolean isTransferException() {
        return transferException;
    }

    public void setTransferException(boolean transferException) {
        this.transferException = transferException;
    }

    public boolean isTestConnectionOnStartup() {
        return testConnectionOnStartup;
    }

    public void setTestConnectionOnStartup(boolean testConnectionOnStartup) {
        this.testConnectionOnStartup = testConnectionOnStartup;
    }

    public void setForceSendOriginalMessage(boolean forceSendOriginalMessage) {
        this.forceSendOriginalMessage = forceSendOriginalMessage;
    }

    public boolean isForceSendOriginalMessage() {
        return forceSendOriginalMessage;
    }

    public boolean isDisableTimeToLive() {
        return disableTimeToLive;
    }

    public void setDisableTimeToLive(boolean disableTimeToLive) {
        this.disableTimeToLive = disableTimeToLive;
    }
}
