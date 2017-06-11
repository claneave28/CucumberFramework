@json
Feature:  Demo of parsing with Json data formats

  Scenario Outline: Accessing a json string inside of a json file
    When a user pulls a service call from "<url>" and validates environment: "<env1>" against environment: "<env2>"

    Examples:
      |url |                                                                                                                                                                            | env1 | env2 |

