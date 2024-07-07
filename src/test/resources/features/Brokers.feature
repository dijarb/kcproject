Feature: Verify Yavlena Broker Details

  Scenario: Verify broker details by searching for each broker
    Given I am on the Yavlena brokers page
    When I search for brokers
    Then I should be able to verify the details for each broker