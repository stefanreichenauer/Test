
Feature: Set&ChangeVariable

  Background:
    Given I have a Program

  Scenario: Check the Brick Set Variable
    Given   I am in the main menu
    When    I press the Continue button
    And     I press the Background button
    And     I press the Scripts button
    And     I press the add button
    Then    I test the set variable

  Scenario: Check the Brick Change Variable
    Given   I am in the main menu
    When    I press the Continue button
    And     I press the Background button
    And     I press the Scripts button
    And     I press the add button
    Then    I test the change variable

