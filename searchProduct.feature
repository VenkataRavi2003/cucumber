Feature: Search vegetables and fruits and place the order

  Scenario Outline: User wants to buy fresh vegetables and fruits online
    Given user is on the Greenkart Landing Page
    When user searched for "Bro" product And selects "10" quantity
    And clicks on AddToCart button
    And user clicks on cart button
    And clicks on Proceed to checkout button
    And clicks on Place Order Button
    And selects the country and Agree to the Terms and Conditions
    And cicks on Proceed button
    Then users sees order placed successfully message on screen
    And quits the browser
