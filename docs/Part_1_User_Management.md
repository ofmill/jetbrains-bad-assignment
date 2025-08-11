## Test Cases

### Role Change

- TC-1. Team Administrator is unable to change his own role
- TC-2. Team Administrator is unable to change the role of Viewer
- TC-3. Team Administrator is unable to change the role of another Administrator

### Team Access

- TC-4. Team Administrator is able to revoke another Administrator's access to the team
- TC-5. Team Administrator is able to revoke Viewer's access to the team
- TC-6. Team Administrator is able to invite another Administrator to the Team
- TC-7. Team Administrator is able to invite Viewer to the Team
- TC-8. Team Administrator is able to leave the team
- TC-9. Team Administrator can see all team's licenses
- TC-10. Team Viewer is unable to invite Administrator to the Team
- TC-11. Team Viewer is unable to invite another Viewer to the Team
- TC-12. Team Viewer is unable to revoke Administrator's access to the team
- TC-13. Team Viewer is unable to revoke another Viewer's access to the team
- TC-14. Team Viewer is able to leave the team
  - This case should be aligned with product requirements - there's no info in docs
- TC-15. Team Viewer can see all team's licenses


### API Tokens

- TC-16. Team Administrator is able to generate personal API token
- TC-17. Team Administrator is able to remove personal API token
- TC-18. Team Administrator is able to re-generate personal API token
- TC-19. Team Viewer is able to generate personal API token
- TC-20. Team Viewer is able to remove personal API token
- TC-21. Team Viewer is able to re-generate personal API token
- TC-22. Team Administrator is able to revoke Viewer's API token
- TC-23. Team Administrator is able to revoke another Administrator's API token

### License Management

- TC-24. Team Administrator can transfer active license to a new team
- TC-25. Team Administrator can transfer expired license to a new team
- TC-26. Team Administrator can assign active license to a user
- TC-27. Team Administrator can revoke assigned license
- TC-28. Team Administrator can purchase new licenses for company
- TC-29. Team Administrator can purchase new licenses for personal use
- TC-30. Team Viewer can not transfer active license to a new team
- TC-31. Team Viewer can not transfer expired license to a new team
- TC-32. Team Viewer can not assign active license to a user
- TC-33. Team Viewer can not revoke assigned license
- TC-34. Team Viewer can not purchase new licenses for company
- TC-35. Team Viewer can purchase new licenses for personal use
- TC-36. Team Administrator can create Invitation
- TC-37. Team Administrator can invalidate Invitation
- TC-38. Team Viewer can not create Invitation
- TC-39. Team Viewer can not invalidate Invitation

## Bug Reports

### BUG-1. Viewer is unable to leave the team

### BUG-2. Team Administrator is able to change roles of others Viewers and Administrators

### BUG-3. Viewer has buttons for JetBrains AI availability displayed

### BUG-4. 'Spending Report' menu item in a Team is not displayed for Org Administrator

### BUG-5. 'Licenses' menu item is not displayed for Team's Administrator and Viewer

### BUG-6. Team's Administrator sees Spending Report for his team by default