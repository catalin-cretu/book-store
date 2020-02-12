# Book store RESTful API

### Objective 1

#### 1. How would you access a *NIX server remotely in order to debug a problem?
- I used SSH before to connect to servers running on AWS EC2. 
- Monitoring and log aggregation is a more robust and safer solution 
for debugging because there are not a lot of things that can be done 
on the server without causing more issues. Maybe connectivity issues 
might be easier to debug that way.
     
#### 2. How would you version your application?
- I've used semantic versioning (major.minor.patch) before, with the minor
part being auto incremented by the CI server and committed to version control
to keep an audit trail
- In terms of APIs, I prefer to avoid versioning and make the API backwards
compatible in order to simplify development
     
#### 3. How do you deliver your application to your team and for deployment?
- I've used Docker containers before to deliver applications internally and 
for deployment, which avoids 'works on my machine' issues

#### 4. If you would have to implement an authorisation / authentication system, what kind of patterns would you choose?
- OAuth 2 and JWT tokens are a pretty robust solution for authorisation and 
authentication against an externally controlled server


#### Objective 2

#### 5. We need to implement a Book store REST API using Spring Framework for Company XYZ. The Following are the requirements: 

- 5.1. Frontend should be able to display a list of books which contain the 
Name and Author's name
- 5.2. Book has fields such as: `ID`, `ISBN`, `Name`, `Author`, `Categories`
- 5.3. Frontend should be able to show, all the details of each book.
- 5.4. An admin in the XYZ company should be able to add books to the store.
- 5.5. The XYZ company should have the ability to know when the book was inserted 
and updated in the system.
- 5.6. The API should be able to handle Unexpected scenarios and return to the clients.
- 5.7.Please, share your code using GIT.


### Objective 3

#### 6. Frontend should be able to show the last access time of a book (Extension of requirement no 5.3)
This requirement does not seem to be very valuable to have for a book store API, 
unless I misunderstood what access time means for a book in a virtual book store.

I think it actually goes against some of the REST design principles because `GET`
requests should not have side effects.

#### 6.1. Please explain at least 2 different approaches and their pros and cons to solve the above problem.
1. Every time a single book is retrieved (GET request), an access time field would 
be updated with the request time if it's more recent than the existing time
    - pros:
        - easy to implement
    - cons:
        - suffers from concurrency issues, especially when the other fields 
        are updated for a book
        - the request would have a side effect and it will not be idempotent,
        which goes against some of the REST design principles/good practices
2. Every time a single book is retrieved, an async call is made to update the access
time field transactionally
    - pros:
        - reads and write are separate and can be optimized, without affecting 
        each other
        - cleaner code
    - cons:
        - more complex to develop because of the nature of async programming
        - the latest timestamp might not be the one written because updates to
        the timestamp are out of sync, but this might not be an issue

#### 6.2. If you use any technology or architecture to solve this problem, please explain the pros and cons for doing so.
- the simplest method to solve this problem using the 2nd approach might be 
to use async methods or application events, which work in a similar way 
in the Spring Framework:
    - add some Spring configuration to run code asynchronously
    - a publisher bean that will update the access time
    - inject and use the publisher when processing the request to retrieve a
    single book

#### 6.3. Choose one approach from 6.1. And explain why you have chosen this solution and the potential problems
- I would pick the 2nd approach because the code should have better separation
of concerns and easier to optimise if needed
- async programming is more complex
- reads are not consistent which could be a problem if all books must have 
the latest access time