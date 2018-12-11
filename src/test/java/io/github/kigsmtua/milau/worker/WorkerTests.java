package io.github.kigsmtua.milau.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.TestActionNonAnnotated;
import io.github.kigsmtua.milau.TestUtils;
import io.github.kigsmtua.milau.client.Client;
import redis.clients.jedis.Jedis;

/**
 * Created by john.kiragu on 19/08/2018.
 */
public class WorkerTests {
   
    private Client client;
    
    private Jedis jedis;
    

    @Before
    public  void setUp() {
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        this.client = new Client(config);
        this.jedis = TestUtils.getJedisConnection(config);
    }
    
    @Test
    public void testGetReadyTasksGetsOnlyReadyTasks() throws Exception {
        
        String queue = "test-queue";
        Map<String , Object> jopProperties = new HashMap<>();
        jopProperties.put("testActionID", 12333);
        jopProperties.put("someTestData", 23242);
        
        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 0);
        

        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 86400000);
        
 
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        
        
        Worker worker = new Worker(config, queue);

        Set jobSet = worker.getReadyTasks();
     
        Assert.assertEquals(1, jobSet.size());
        
    }
    
    @Test
    public void testJobsAreAddedToAckQueue() throws Exception {
 
        String queue = "test-queue";
        Map<String , Object> jopProperties = new HashMap<>();
        jopProperties.put("testActionID", 12333);
        jopProperties.put("someTestData", 23242);
        
        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 0);
        
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        
      
        Worker worker = new Worker(config, queue);
        
        Set jobSet = worker.getReadyTasks();

        Map tasks = worker.getTaskPayloads(jobSet);
        
        Assert.assertEquals(1, tasks.size());
    
        String ackQueue = queue + "ack-queue";
        
        long currentTime = System.currentTimeMillis();
        Set ackJobs = jedis.zrangeByScore(ackQueue, 0, 
                Double.valueOf(currentTime));
        
        Assert.assertEquals(1, ackJobs.size());    
    }
    

    @Test
    public void testjobsAreAckedAfterExecution() throws Exception {
        String queue = "test-queue";
        Map<String , Object> jopProperties = new HashMap<>();
        jopProperties.put("testActionID", 12333);
        jopProperties.put("someTestData", 23242);
        
        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 0);
        
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        
      
        Worker worker = new Worker(config, queue);
        
        Set jobSet = worker.getReadyTasks();

        Map tasks = worker.getTaskPayloads(jobSet);
        
        Assert.assertEquals(1, tasks.size());

        worker.processTasks(tasks);
        
        long currentTime = System.currentTimeMillis();
        
        String ackQueue = queue + "ack-queue";
        
        Set acSet = jedis.zrangeByScore(ackQueue, 0,
                Double.valueOf(currentTime));
         Assert.assertEquals(0, acSet.size()); 
    }
    
    @After
    public  void tearDown() {
        this.jedis.flushAll();
    }

}
