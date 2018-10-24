package io.github.kigsmtua.milau.client;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.TestAction;
import io.github.kigsmtua.milau.TestActionNonAnnotated;
import io.github.kigsmtua.milau.TestUtils;
import java.io.IOException;

import redis.clients.jedis.Jedis;

/**
 * Created by john.kiragu on 19/08/2018.
 */
public class ClientTest {
    
    private Client client;
    
    private Jedis jedis;

    @Before
    public  void setUp() {
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        this.client = new Client(config);
        this.jedis = TestUtils.getJedisConnection(config);
    }
    
    @Test
    public void testAnnnotatedJobsAreStoredCorrectly() throws Exception {
       Map<String , Object> jopProperties = new HashMap<>();
       jopProperties.put("testActionID", 12333);
       jopProperties.put("someTestData", 23242);
       this.client.enqueue(null,  TestAction.class, jopProperties, 0);
       long currentTime = System.currentTimeMillis();
       Set enquedJobs = 
               this.jedis.zrangeByScore("test-queue", 0,
                       Double.valueOf(currentTime));
       Assert.assertEquals(1, enquedJobs.size());
       
       ObjectMapper mapper = new ObjectMapper();
      
       enquedJobs.forEach((job) -> {
           String taskPayload = jedis.hget("test-queuejob-queue", 
                   String.valueOf(job));
           try {
             Map<String, Object> payload = 
                      mapper.readValue(taskPayload, Map.class);
             Assert.assertEquals(TestAction.class.getSimpleName(),
                     payload.get("taskClassName"));
             Assert.assertEquals(jopProperties, payload.get("jobProperties"));
           } catch (IOException ex) {
                  
           }
       });
    }
    
    @Test
    public void testNonAnnotatedJobsExecuteCorrectly() throws Exception {
       Map<String , Object> jopProperties = new HashMap<>();
       jopProperties.put("testActionID", 12333);
       jopProperties.put("someTestData", 23242);
       this.client.enqueue("test-queue",  TestActionNonAnnotated.class, jopProperties, 0);
       long currentTime = System.currentTimeMillis();
       Set enquedJobs = 
               this.jedis.zrangeByScore("test-queue", 0,
                       Double.valueOf(currentTime));
       Assert.assertEquals(1, enquedJobs.size());
       
       ObjectMapper mapper = new ObjectMapper();
      
       enquedJobs.forEach((job) -> {
           String taskPayload = jedis.hget("test-queuejob-queue", 
                   String.valueOf(job));
           try {
             Map<String, Object> payload = 
                      mapper.readValue(taskPayload, Map.class);
             Assert.assertEquals(TestActionNonAnnotated.class.getSimpleName(),
                     payload.get("taskClassName"));
             Assert.assertEquals(jopProperties, payload.get("jobProperties"));
           } catch (IOException ex) {
                  
           }
       });
    
    }

    @After
    public  void tearDown() {
        this.jedis.flushAll();
    }
}
