cdp-jms
=======

**This test project developed to learn next stack of technologies:**

1. Spring MVC
2. Hibernate 
3. JMS
4. REST-services
5. REST-requests
6. JSP(Javascript, Ajax)



**It consists of the next modules:**

1. Core. It has domain model and some util classes.
2. Sender. Front-end module where customer can order taxi for his needs.  Sender module sends ReservationRequests that are created by customer to different taxi services and displays the request status.
3. Router. Back-end module to mManage all JMS queues, updates DB. Router gets ReservationRequests from sender, then it creates Orders, then it sends BookingRequests to different taxi services. Router manages all orders, their expiration time so on.
4. Taxi-service. It's an example of implementation of any taxi service, that allow to handle BookingRequests from customers. This module allows any operator to delivery requests to taxi drivers and tell client details of current order
5. Management module. Front module that allows to find all orders were created by Router, to enter new taxi dispatcher account and to show fail queue.



**What have to be installed:**

1. Database MySql with default config
2. JMS ActiveMQ
3. Add new taxi dispatcher to management module


**How to deploy?:**

1. Create cdp_taxi_service(for Router module) and cdp_aviz_taxi_service(for Taxi-service module) database schemas
2. ActiveMQ at this path tcp://localhost:61616

