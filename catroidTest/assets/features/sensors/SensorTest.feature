Feature: Testing sensors

  A first test case for sensors

  Background:
    Given I have a Program
    And this program has an Object 'Object'
    And Robotarm should be in default position

  Scenario: Robotarm not moving
    Given   I check the sensor values
    And I am in the main menu

    When The Robotarm didn't move
    Then   'X acceleration' should not be changed
    And    'Y acceleration' should not be changed
    And    'Z acceleration' should not be changed
    And    'X inclination' should not be changed
    And    'Y inclination' should not be changed
    And    'compass direction' should not be changed

  Scenario Outline: Robotarm turned on different axis by different degree
    Given   I check the sensor values
    And I am in the main menu

    When The Robotarm turns in '<axis>' axis by <degree> degree
    Then    '<axis>' should be changed by <degree> degree

    Examples:
    | axis                 | degree |
    | X inclination        | 45     |
    | Y inclination        | 45     |
    | compass direction    | 45     |

  Scenario Outline: Test accelerometer of DUT
    Given   I check the sensor values
    And I am in the main menu

    When The Robotarm prepares the test for the '<axis>' accelerometer
    Then  The '<axis>' accelerometer test values should be checked

    Examples:
      | axis |
      | X    |
      | Y    |
      | Z    |