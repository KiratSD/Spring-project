# DAT250 – Experiment Assignment 6

In this experiment I added event sourcing functionality to the PollApp using RabbitMQ.  
When a poll is created a topic is registered in RabbitMQ, and vote events are sent and received through it.  
I tested the application using Bruno.

## Technical problems 
I had some issues with the RabbitMQ setup and errors, which were fixed by configuring the JSON converter and restarting the RabbitMQ service.

## Pending issues
There does not seem to be any remaining problems, all tests worked as expected.
