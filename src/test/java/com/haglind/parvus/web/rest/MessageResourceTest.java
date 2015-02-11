package com.haglind.parvus.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import com.haglind.parvus.Application;
import com.haglind.parvus.domain.Message;
import com.haglind.parvus.repository.MessageRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_CREATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_STR = dateTimeFormatter.print(DEFAULT_CREATED);
    private static final String DEFAULT_PAYLOAD = "SAMPLE_TEXT";
    private static final String UPDATED_PAYLOAD = "UPDATED_TEXT";

    @Inject
    private MessageRepository messageRepository;

    private MockMvc restMessageMockMvc;

    private Message message;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessageResource messageResource = new MessageResource();
        ReflectionTestUtils.setField(messageResource, "messageRepository", messageRepository);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource).build();
    }

    @Before
    public void initTest() {
        message = new Message();
        message.setCreated(DEFAULT_CREATED);
        message.setPayload(DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        // Validate the database is empty
        assertThat(messageRepository.findAll()).hasSize(0);

        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(1);
        Message testMessage = messages.iterator().next();
        assertThat(testMessage.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED);
        assertThat(testMessage.getPayload()).isEqualTo(DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messages
        restMessageMockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(message.getId().intValue()))
                .andExpect(jsonPath("$.[0].created").value(DEFAULT_CREATED_STR))
                .andExpect(jsonPath("$.[0].payload").value(DEFAULT_PAYLOAD.toString()));
    }

    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED_STR))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Update the message
        message.setCreated(UPDATED_CREATED);
        message.setPayload(UPDATED_PAYLOAD);
        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(1);
        Message testMessage = messages.iterator().next();
        assertThat(testMessage.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED);
        assertThat(testMessage.getPayload()).isEqualTo(UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(0);
    }
}
