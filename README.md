# Milau
[![Build Status](https://travis-ci.org/kigsmtua/milau.svg?branch=master)](https://travis-ci.org/kigsmtua/milau) [![License MIT](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/kigsmtua/milau/blob/master/LICENSE) [![Maintainability](https://api.codeclimate.com/v1/badges/c092be6110abdbb2857d/maintainability)](https://codeclimate.com/github/kigsmtua/milau/maintainability) [![codecov](https://codecov.io/gh/kigsmtua/milau/branch/master/graph/badge.svg)](https://codecov.io/gh/kigsmtua/milau)


> A distributed task queue supporting priorities and time based exection based on redis. Named after the famous milau bridge (yes architecture fascinates me)
> The worker only supports one queue and uses Traditional threads based on CPU count (Work is in progress to make it support multiple queues)

Maven central 

```xml
<dependency>
  <groupId>io.github.kigsmtua</groupId>
  <artifactId>milau</artifactId>
  <version>0.1.0</version>
</dependency>
```

How To Use

```java

@Task(
   queueName = "my-task-queue"
)
Class MyJob implements Runnable {
   
   private String name;
   
   public String getName() {
     return this.name;
   }
   public void setName(String name) {
     this.name = name;
   }
   public void run () {
       try {
            /// Sleep for some time to simulate execution
            Thread.sleep(1);
        } catch (InterruptedException e) {
      }
   }
}

Config config = new Config.ConfigBuilder("127.0.0.1", 6379).build();

Map<String , Object> jopProperties = new HashMap<>();
jopProperties.put("name", "johnDoe");

Client client = new Client(config);
client.enqueue(null,  MyJob.class, jopProperties, 0);

Worker worker = new Worker(config, queue);
Thread workerThread = new Thread(worker);
workerThread.start()

```

> **Whats Remaining** 
> 1. Worker to run for all/multiple queues
> 2. Record number of failures/keep stats
> 3. Ability to pause given queues
> 4. Use green threads to see if performance actually gets to improves
> 5. Finish up on the Ack module
> 6. Build recovery strategy for the worker module

Contributions and usages are welcome

