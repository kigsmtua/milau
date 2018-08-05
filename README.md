# Milau
[![Build Status](https://travis-ci.org/kigsmtua/milau.svg?branch=master)](https://travis-ci.org/kigsmtua/milau) [![License MIT](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/kigsmtua/milau/blob/master/LICENSE)[![Maintainability](https://api.codeclimate.com/v1/badges/c092be6110abdbb2857d/maintainability)](https://codeclimate.com/github/kigsmtua/milau/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/c092be6110abdbb2857d/test_coverage)](https://codeclimate.com/github/kigsmtua/milau/test_coverage)
[![codecov](https://codecov.io/gh/kigsmtua/milau/branch/master/graph/badge.svg)](https://codecov.io/gh/kigsmtua/milau)


> A distributed task queue supporting priorities and time based exection based on redis. Named after the famous milau bridge (yes architecture fascinates me)

Milau aims to achieve the following queue recipe
> 1. Distributed task execution
> 2. Highly concurrent
> 3. At-least-once delivery semantics
> 4. No strict FIFO
> 5. Delayed queue (message is not taken out of the queue until some time in the future)
