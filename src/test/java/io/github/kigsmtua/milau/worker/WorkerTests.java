package io.github.kigsmtua.milau.worker;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.kigsmtua.milau.Config;
import io.github.kigsmtua.milau.Task;
import io.github.kigsmtua.milau.TestAction;
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
    public void testJobsArePickedFromQueueAndExecuted() throws Exception {
       Map<String , Object> jopProperties = new HashMap<>();
       jopProperties.put("testActionID", 12333);
       jopProperties.put("someTestData", 23242);
       this.client.enqueue(null,  TestAction.class, jopProperties, 0);

       String  queueName = "test-queue";
       
       Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();

       Worker worker = new Worker(config, queueName);
       
       Set jobSet = worker.getReadyTasks();

       Map tasks = worker.getTaskPayloads(jobSet);
        
       Assert.assertEquals(1, tasks.size());

       worker.processTasks(tasks);
       
       String ackQueue = queueName + "ack-queue";
       
       long currentTime = System.currentTimeMillis();
       Set jobQueue = jedis.zrangeByScore(queueName, 0, 
                Double.valueOf(currentTime));
       Assert.assertEquals(0, jobQueue.size()); 

       Set acSet = jedis.zrangeByScore(ackQueue, 0, 
                Double.valueOf(currentTime));

       Assert.assertEquals(0, acSet.size());   
    }

    @Test
    public void testCorrectClassIsLoaded() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, 
            IllegalArgumentException, InvocationTargetException, 
            NoSuchMethodException {
      Task task = new Task();
      Map<String , Object> jopProperties = new HashMap<>();
      jopProperties.put("testActionID", 12333);
      jopProperties.put("someTestData", 23242);
      String taskClassName = TestActionNonAnnotated.class.getCanonicalName();
      task.setTaskClassName(taskClassName);
      task.setJobProperties(jopProperties);
      
      Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();
      Worker worker = new Worker(config, "test-queue");
      
      Object jobInstance = worker.getTaskToExecute(task);  

      Assert.assertTrue(jobInstance instanceof Runnable);
      Class taskClass = jobInstance.getClass();
      String taskName = taskClass.getCanonicalName();
      Assert.assertEquals(taskClassName, taskName);
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
