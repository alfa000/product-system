# Order System - Spring Boot "

## How to Run

* Clone this repository
* Make sure you are using JDK and Maven
* You can build the project and run the tests by running ```mvn clean package```

* Check the stdout or boot_example.log file to make sure no exceptions are thrown

## About the Service

The service is a order system REST service. It uses an in-memory database (H2) to store the data. You can also do with a relational database like MySQL or PostgreSQL. **port 8080**. (see below)

More interestingly, you can start calling some of the operational endpoints (see full list below) like ```/proect``` and ```/cart``` (these are available on **port 8091**)

You can use this sample service to understand the conventions and configurations that allow you to create a DB-backed RESTful service. Once you understand and get comfortable with the sample app you can add your own services following the same patterns as the sample service.

### Get information about system health, configurations, etc.

```
http://localhost:8091/env
http://localhost:8091/health
http://localhost:8091/info
http://localhost:8091/metrics
```

### Get product

```
GET /api/prooduct
Accept: application/json
Content-Type: application/json

Response: HTTP 200
Content: {
    "message": "Data Products",
    "data": [
        {
            "productId": 2,
            "name": "KeyChain JKT",
            "type": "Accessories",
            "price": 1000.0,
            "stock": 9
        }
    ],
    "errors": null,
    "paging": {
        "currentPage": 0,
        "totalPage": 1,
        "size": 10
    }
}
```

