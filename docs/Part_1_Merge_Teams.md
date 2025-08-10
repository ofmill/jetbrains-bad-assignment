## Requirements

If Team A is merged into Team B, then:

1. All licenses from Team A transferred to Team B
2. All admins of Team A become admins of Team B
3. All bulk invitations from Team A transferred to Team B, including expired ones
4. AI Assistant setting for Team B stays unchanged, AI Assistant setting of Team A is ignored
    1. What about API accessibility setting?
5. Team A is deleted
6. Team B inherits all the licenses from Team A and properties except AI Assistant setting
    1. Which are “properties” from Team A should be inherited?
7. Only Org Admin could merge teams

## Test Cases

For simplicity, a rule has been introduced that Team A is always merged into Team B unless otherwise specified.

### License Transfer

- TC-1. If Team A has 1 active license and Team B has active 1 license, then Team B has these 2 active licenses after merge
- TC-2. If Team A has 1 active license and Team B has 0 licenses, then Team B has this 1 active license after merge
- TC-3. If Team A has 0 license and Team B has 1 active licenses, then Team B has 1 active license after merge
- TC-4. If Team A has 0 license and Team B has 1 expired licenses, then Team B has 1 expired license after merge
- TC-5. If Team A has 1 expired license and Team B has 0 licenses, then Team B has 1 expired license after merge
- TC-6. If Team A has 1 expired license and Team B has expired 1 license, then Team B has these 2 expired licenses after merge
- TC-7. If Team A has 2 active and 2 expired licenses and Team B has 2 expired and 2 active licenses, then Team B has these 8 licenses after merge

### JetBrains AI Setting

- TC-8. If Team A has JetBrains AI ON and Team B has it turned OFF, then Team B has JetBrains AI turned OFF
- TC-9. If Team A has JetBrains AI OFF and Team B has it turned ON, then Team B has JetBrains AI turned ON
- TC-10. If Team A has JetBrains AI ON and Team B has it turned ON, then Team B has JetBrains AI turned ON
- TC-11. If Team A has JetBrains AI OFF and Team B has it turned OFF, then Team B has JetBrains AI turned OFF

### Teams Administrators and Users

- TC-12. If Team A has User A as Viewer, Team B has User B as Viewer, then Team B has both these Viewers after merge
- TC-13. If Team A has User A as Viewer, Team B has User B as Admin, then Team B has these Viewer and Admin after merge
- TC-14. If Team A has User A as Admin, Team B has User B as Admin, then Team B has these 2 Admins after merge 
- TC-15. If Team A has User A as Admin, Team B has User B as Viewer, then Team B has 1 Viewer and 1 Admin after merge
- TC-16. If User A as Admin in both Teams, then Team B has this 1 Admin after merge
- TC-17. If User A as Viewer in both Teams, then Team B has this 1 Viewer after merge
- TC-18. If Team A has no administrators, Team B has User B as Viewer, then Team B has 1 Viewer after merge
- TC-19. If Team A has no administrators, Team B has User B as Administrator, then Team B has 1 Administrator after merge
- TC-20. If Team A has User A as Viewer, Team B has no administrators, then Team B has this 1 Viewer after merge
- TC-21. If Team A has User A as Administrator, Team B has no administrators, then Team B has this 1 Administrator after merge

### Generated API Tokens

- TC-22. If Viewer in Team A has API Token generated, then it will be invalid after Team A is merged into Team B
- TC-23. If Viewer has API Token generated only in Team A, then Viewer won't have any valid tokens in Team B after merge
- TC-24. If Viewer has tokens generated in both teams, then only token in Team B will be valid after merge
- TC-25. If Administrator in Team A has API Token generated, then it will be invalid after Team A is merged into Team B
- TC-26. If Administrator has API Token generated only in Team A, then Administrator won't have any valid tokens in Team B after merge
- TC-27. If Administrator has tokens generated in both teams, then only token in Team B will be valid after merge

### License Users

- TC-28. If Team A has license user and Team B doesn't have one, then Team B has the license user after merge
- TC-29. If Team A has no license users and Team B has one, then Team B has the license user after merge
- TC-30. If one License User has 1 license per each team, then he will have these 2 licenses in Team B after merge
- TC-31. If Team A has 1 license user and Team B has 1 license user, then Team B has these 2 license users after merge
- TC-32. If Team A has 2 license users and Team B has 2 license users, then Team B has these 4 license users after merge

### Invitations

- TC-33. If Team A has active invitation and Team B has no invitations, then Team B has this active invitation after merge
- TC-34. If Team A has no invitations and Team B has active invitation, then Team B has the active invitation after merge
- TC-35. If Team A has expired invitation and Team B has no invitations, then Team B has this expired invitation after merge
- TC-36. If Team A has no invitations and Team B has expired invitation, then Team B has this expired invitation after merge

### Server Team

- TC-37. Server Team couldn't be merged to any other team
- TC-38. Team could not be merged into Server Team

## Bug Reports

### BUG-1. If the User is Viewer in Team A and Administrator in Team B, he becomes a Viewer in Team B after merge 

#### Preconditions
- Team A has a User with Viewer role
- Team B has the same User, but with Administrator role

#### Scenario
1. Team A is merged into Team B

#### Expected Result
- The User's role in resulting team is Administrator

#### Actual Result
- The User's role in resulting team is Viewer

### BUG-2. Administrator is able to merge one team to another if he is Administrator in both of them
- Team A has a User as Admin
- Team B has the same User as Admin

#### Scenario
1. As the User, go to Team A -> Administration

#### Expected Result
- There should be no Merge button according to [API Reference](https://sales.jetbrains.com/hc/en-gb/articles/208460175-Merge-teams#h_01J1PW20ZKJMMEF52NZRMJ2CES)

#### Actual Result
- Merge button presence

### BUG-3. Redundant invitation is created if team with used invitation is merged into another one

#### Preconditions
- Team A has used invitation
- Team B has no invitations

#### Scenario
1. Merge Team A into Team B

#### Expected Result
- Team B has only one used invitation - the same as Team A had

#### Actual Result
- New invitation is created with generic name
- License and its User are moved under this invitation