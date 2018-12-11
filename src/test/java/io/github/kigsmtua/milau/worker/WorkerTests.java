package io.github.kigsmtua.milau.worker;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.TestActionNonAnnotated;
import io.github.kigsmtua.milau.TestUtils;
import io.github.kigsmtua.milau.client.Client;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * Created by john.kiragu on 19/08/2018.
 */
public class WorkerTests {
    /**
     * What does this configuration even mean
     * We can use the values as they come alongside here
     */
    private Client client;
    
    private Jedis jedis;
    

    @Before
    public  void setUp() {
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        this.client = new Client(config);
        this.jedis = TestUtils.getJedisConnection(config);
    }
    /**
     * Test the jobs are picked from the queue
     * @throws Exception 
     */
    @Test
    public void testGetReadyTasksGetsOnlyReadyTasks() throws Exception {
        
        String queue = "test-queue";
        Map<String , Object> jopProperties = new HashMap<>();
        jopProperties.put("testActionID", 12333);
        jopProperties.put("someTestData", 23242);
        
        /**
         * This job is due now
         */
        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 0);
        
        /**
         * Enqueue job to done 24hrs from now
         */
        
        this.client.enqueue("test-queue",  TestActionNonAnnotated.class, 
            jopProperties, 86400000);
        
        /**
         * You need to make your methods in such a manner that 
         */
        Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
        
        
        Worker worker = new Worker(config, queue);
        /**
         * Lets 
         */
        Set jobSet = worker.getReadyTasks();
     
        Assert.assertEquals(1, jobSet.size());
        
    }
    
    /**
     * Do we need to select correct job class is called
     */
    @Test
    public void testCorrectJobClassIsLoaded(){
    
    }
    
    @After
    public  void tearDown() {
        this.jedis.flushAll();
    }

}
