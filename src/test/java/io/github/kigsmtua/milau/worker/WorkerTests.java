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
    public void testCorrectJobClassIsLoaded(){
    
    }
    
    @After
    public  void tearDown() {
        this.jedis.flushAll();
    }

}
