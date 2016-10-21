Feature: Testing sensors

  A first test case for sensors

  Background:
    Given I have a Program
    And this program has an Object 'Object'
    And Robotarm should be in default position

  Scenario: X acceleration test on not moving devise

    Given   I am in the main menu
    Then    X acceleration value should be zero

  Scenario: Y acceleration test on not moving devise

    Given   I am in the main menu
    Then    Y acceleration value should be zero

  Scenario: Z acceleration test on not moving devise

    Given   I am in the main menu
    Then    Z acceleration value should be zero

  Scenario: X inclination test on not moving devise

    Given   I am in the main menu
    Then    X inclination value should be zero

  Scenario: Y inclination test on not moving devise

    Given   I am in the main menu
    Then    Y inclination value should be zero

  Scenario: compass direction test on not moving devise

    Given   I am in the main menu
    Then    compass direction value should not change
