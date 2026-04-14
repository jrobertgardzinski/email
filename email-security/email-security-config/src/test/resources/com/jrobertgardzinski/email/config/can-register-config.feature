@allure.label.epic:Email
Feature: Repository-backed registration configuration

  CanRegisterConfig adapts three typed RepositoryConfigPorts into EmailConfigPort.
  Each port is responsible for one domain list: blocked, disposable, or company-only.
  An absent entry in the repository means no restriction for that list.

  Scenario Outline: Domain list resolved from repository
    Given the repository contains <type> domains "<values>"
    When registration configuration is resolved
    Then <type> domains are "<expected>"

    Examples:
      | type       | values                           | expected                         |
      | blocked    | spam.com,junk.org                | spam.com,junk.org                |
      | disposable | mailinator.com,guerrillamail.com | mailinator.com,guerrillamail.com |
      | company    | acme.com,corp.org                | acme.com,corp.org                |
      | blocked    |                                  |                                  |
      | disposable |                                  |                                  |
      | company    |                                  |                                  |
