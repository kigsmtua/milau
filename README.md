# Milau
[![Build Status](https://travis-ci.org/kigsmtua/milau.svg?branch=master)](https://travis-ci.org/kigsmtua/milau) [![License MIT](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/kigsmtua/milau/blob/master/LICENSE) [![Maintainability](https://api.codeclimate.com/v1/badges/c092be6110abdbb2857d/maintainability)](https://codeclimate.com/github/kigsmtua/milau/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/c092be6110abdbb2857d/test_coverage)](https://codeclimate.com/github/kigsmtua/milau/test_coverage)
[![codecov](https://codecov.io/gh/kigsmtua/milau/branch/master/graph/badge.svg)](https://codecov.io/gh/kigsmtua/milau)


> A distributed task queue supporting priorities and time based exection based on redis. Named after the famous milau bridge (yes architecture fascinates me)

Milau aims to achieve the following queue recipe
> 1. Distributed task execution
> 2. Highly concurrent
> 3. At-least-once delivery semantics
> 4. No strict FIFO
> 5. Delayed queue (message is not taken out of the queue until some time in the future)

The following sequence describes the high level operations used to push/poll messages into the system.
For each queue three set of Redis data structures are maintained:

> 1. A Sorted Set containing queued elements by score.
> 2. A Hash set that contains message payload, with key as message ID.
> 3. A Sorted Set containing messages consumed by client but yet to be acknowledged. Un-ack set.

How pushing to the queue works
> 1. Calculate the score as a function of message timeout (delayed queue) and priority
> 2. Add to sorted set for queue
> 3. Add message payload by ID into Redis hashed set with key as message ID.

How polling from queue works
> 1. Calculate max score as current time
> 2. Get messages with score between 0 and max
> 3. Add the message ID to unack set and remove from the sorted set for the queue.
> 4. If the previous step succeeds, retrieve the message payload from the Redis set based on ID

How ack works
> 1. Remove from unack set by ID
> 2. Remove from the message payload set
> 3. Messages that are not acknowledged by the client are pushed back to the queue (at-least once semantics).

How do I use it ?

Maven central gradle :-)

```xml
<dependency>
  <groupId>io.github.kigsmtua</groupId>
  <artifactId>milau</artifactId>
  <version>0.1.0</version>
</dependency>
```

Quickstart

```java


```

