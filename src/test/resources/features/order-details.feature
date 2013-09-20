Feature:
  So that I can see what someone likes
  As a marketer
  I want to see what's in a customers orders

Scenario:
  When I GET "/transaction/tom@example.com"
  Then I see JSON like "order-history.json"
  And it has status 200

Scenario:
  When I GET "/transaction/tom@example.com/ABC2"
  Then I see JSON like "order-abc2.json"
  And it has status 200
