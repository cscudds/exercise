Feature:
  So that I can
  As a user
  I want to be greeted

  @manual
  Scenario: get hello world
    When I GET "/hello"
    Then I see "Hello World!"
    And it has status 200