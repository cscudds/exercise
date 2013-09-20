## Getting started....

1. Fork the repository
1. Clone to your machine
1. Create a branch
1. Run through the project adding the features
1. Commit often
1. Push
1. Send a Pull Request


## Running the project...

To run the project, use maven

```
mvn verify
```

This will:
* compile the code
* run any tests
* start the web server on `http://localhost:8080/`
* runs the integration test features

## Finding stuff...

* Each feature is described in the `src/test/resources/features` directory
* Expected responses are in `src/test/resources/json`


## Features

### Abandoned Basket and Order Details

Call the `UserGraphRepository#getUser` method to get the sample data.

This is a graph with:
* Customer --> Visit --[add to basket]--> Product
* Customer --> Visit --[check-out] --> Order

The code is arranged in the `UserGraphRepository` in the order that the events happen so that
a check-out will purchase all the items that have been added to a basket since the last transaction.




