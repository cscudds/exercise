Feature:
  So that I can see potential sales
  As a marketer
  I want to see what's left in baskets

Scenario:
  When I GET "/abandoned-basket/tom@example.com"
  Then I see JSON like "basket.json"
  And it has status 200
