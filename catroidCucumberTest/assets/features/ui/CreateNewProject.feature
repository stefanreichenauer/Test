Feature: New Project

  Background:
    Given I have a Program

  Scenario: Create New Project
    Given I am in the main menu
    When  I press the New button
    And   I input program name in New button
    Then  I play the program

  Scenario: Check the Brick Categories one by one
    Given I am in the main menu
    When  I press the Continue button
    And   I press the Background button
    And   I press the Scripts button
    And   I press the add button
    And   I test the brick categories existence