Feature: the health checks return global, readiness and liveness status
  Scenario: client makes call to GET /health
    When the client calls /actuator/health
    Then the client receives status code of 200
    And the client receives body containing "status":"UP"
  Scenario: client makes call to GET /health/readiness
    When the client calls /actuator/health/readiness
    Then the client receives status code of 200
    And the client receives body containing "status":"UP"
  Scenario: client makes call to GET /health/liveness
    When the client calls /actuator/health/liveness
    Then the client receives status code of 200
    And the client receives body containing "status":"UP"