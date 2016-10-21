Feature: ControlBrick

  Background:
    Given I have a Program

  Scenario: Check the Brick Control
    Given   I am in the main menu
    When    I press the Continue button
    And     I press the Background button
    And     I press the Scripts button
    And     I press the add button
    Then    I test the Control bricks existence



