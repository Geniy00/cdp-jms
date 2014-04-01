cdp-jms
=======

This test project developed to learn next stack of technologies:
-Spring MVC
-Hibernate
-JMS
-REST-services
-REST-requests
-JSP

It consists of next modules:
1. Core. It has domain model and some util classes
2. Sender. Customer can order taxi for his needs.  Sender module sends ReservationRequests that are created by cutomer to different taxi services. 
3. Router. Manage all JMS queues, updates DB. Router gets ReservationRequests from sender, then it creates Orders, then it sends BookingRequests to different taxi services. Router manages all orders, their expiration time so on.
4. Taxi-service. It's an example of implementation of any taxi service, that allow to handle BookingRequests from customers.
5. Management module. It allows to find all orders were created by Router, to enter new taxi dispatcher account and to show fail queue. 


What have to be installed:
1. Database MySql with default config
2. JMS ActiveMQ

How to deploy?
1.Create cdp_taxi_service(for Router module) and cdp_aviz_taxi_service(for Taxi-service module) database schemas
2.ActiveMQ at this path tcp://localhost:61616

