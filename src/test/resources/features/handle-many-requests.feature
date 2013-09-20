Feature:
  So that I can get on with my work
  As a busy user
  I want the server to handle many requests without making me wait

  Scenario:
    When I POST 10 processes
    Then I get a valid response for each one
    And it takes less than 1 second