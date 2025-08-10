> ℹ️ This README file covers only autotests launch
> 
> Part 1 of the assignment is split between two docs:
> - [Part_1_Merge_Teams.md](docs/Part_1_Merge_Teams.md)
> - [Part_1_User_Management.md](docs/Part_1_User_Management.md)
> 
> Problems, Limitations, Possible Bugs and Improvements related to autotests are described in [Part_2_Autotests.md](docs/Part_2_Autotests.md)

### Data Required for Launch
Several variables should be provided via env vars:
- CUSTOMER_CODE
- ORG_ADMIN_API_KEY
- TEAM_1_ADMIN_API_KEY
- TEAM_1_VIEWER_API_KEY

### Allure Report
Assuming that allure is installed, run this command from the project root in order to display Allure report:
```shell
allure serve
```