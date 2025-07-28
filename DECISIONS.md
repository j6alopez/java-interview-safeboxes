## Summary

The purpose of this document is to explain decisions made as well as the scope of the current application.

## API test

In order to test the application follow next steps:

1. Tell your tech team how to deploy the application in a server or locally
2. Run the application by using make command `make app` or gradle command `./gradlew bootRun` with a database running
3. For valid credentials:

- **Username:** testUser
- **Password:** testWordPass1!

## Persistance

`Spring Data` offers different options for persistence. As the database is relational-one, we have different JPA options
that are suitable.
`CrudRepository` contract provides exactly what was needed. If either more advanced query tuning
or more fields/entities will be required, it will not be too much difficult to implement it.

Behind the scenes we will use an ORM called Hibernate which implements all this functionality.

## Security

`Spring Security` offers a high customizable way to implement security.

Current implementation is only allowed for development and testing.
The application contains a valid user which is never persisted and only
lives as long as the application does.

Before going into production:

- Implement Bearer Token authentication
- Implement CRUD operations and credentials management
- Secure all endpoints based on roles

## Jackson

In order to serialize/deserialize I have decided to use `Jackson` library.
Due to Jackson is used by default by `Spring Boot` and I have not worked with
gson, it was clear to work with `Jackson`.

## Cache

Cache will skip unneeded calls to the database
and will smooth application workloads. In order to not to have memory leaks
caused by the cache:

- A `ttl` of 60s is implemented(data will be removed if no needed)
- A maximum of 1000 safeboxes
- This will need to be tested by simulating expected traffic and adjust it accordingly

`Hazelcast` was selected as the cache provider as it has an open-source version, provides good performance
and could scale horizontally.

## Profiles

I leave the application in a status where it is suitable only for testing.
Therefore, some profiles will be required in scenarios such as development and testing
with different configuration than in PROD. Profiles will be useful.

## Logging

Logging is activated and implemented in the service layer.
Depending on the profile, logging levels are set.
Logs will allow monitoring activities.

Those logs could be collected and sent to a monitoring system as could be `ElasticSearch` later on by using
`FileBeat` for instance.

## Testing

Controllers and Services are tested using the `Junit5` framework along with `Mockito`.

## Architecture Decisions
-  Layered architecture is used to separate concerns and responsibilities. The application most likely will grow
   and it is a good practice to separate the layers. If this scales enough, files could be moved into a modular monolith. Doing
    so, it will be easier to extract them into microservices later on if that is needed.
- `Lombok` is used to reduce boilerplate code in DTOs and entities. 
  It is a good practice to use it in order to reduce the amount of code.
- `DTOS` are used to isolate data transfer structures. They are a bridge between the service and the controller. Therefore, they are at application level though outside the domain.