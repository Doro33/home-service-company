# home-service-company
Hello dear viewer.
I'm glad you are visiting my very first project using Spring Boot.
This project can be used as a back-end layer for a home-service company.
I would be honored to explain how it works.
There are 3 types of users, admin, client and worker.

## Admin: (AdminController)
Obviously admin has been defined in the Data Base already.
Admin is in charge of adding categories and jobs.
Admin is able to confirm workers and their skills. 
Moreover, he can filter clients, workers and requests (orders) by different things such as name or email, date of sign up for both clients and workers,
and number of requests for clients + 
number of completed tasks and score for workers.
Requests can be filtered by date, status, category and job.

## Client: (ClientController)
Clients can sign up (via GeneralController class) and after confirming their email address, they can use our services.
First step is choosing a job that they want to add a new Request for (showing all categories and jobs are in GeneralController).
Then they are able to choose one offer between offers from different workers. 
Client is responsible to set "started" and "completed" status for their requests. 
And they can pay the bill using their credit.
In case that the credit amount is not enough they can increase their credit using bank cards.
At the end they can add a comment to show how much they are satisfied with the worker.
In addition they can see their requested history or their credit if they want to.

## Workers: (WorkerController)
Workers must sign up (GeneralController), they have to add a photo of themselves(300 kb or less, jpg only).
And after confirming their email address, the admin must confirm their profile.
After that, they can ask for adding a new skill to their resume, once again the admin must confirm their skill to add it to the resume. 
Next, they can add an offer for every request they are able to do.
Each worker has a score. This score will be increased by the client's comment's rate, 
and decreased whenever the task takes longer than what they said in their offer. (-1 per hour).
If the score is less than zero, the worker will be suspended and he cannot work anymore.
Workers also can see the history of their offers and their credit.

##
In this project I used BCryptPasswordEncoder (Hashing) as password encoder.
I also customized the GlobalExceptionHandler (you can find it at utils.exception.handler).

Thank you for reading this text.
Hope you enjoy my coding.

Regards, Doro33. 
