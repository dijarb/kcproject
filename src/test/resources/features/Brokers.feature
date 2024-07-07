Feature: Verify Yavlena Broker Details

  Scenario: Verify broker details by searching for each broker
    Given I am on the Yavlena brokers page
    When I get the names of all brokers
    Then I search and verify details for each broker