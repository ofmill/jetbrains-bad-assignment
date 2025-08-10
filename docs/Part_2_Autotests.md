## Problems & Limitations

While doing this part of the assignment I noticed several problems, making the tests far from acceptable.

First of all, it's unable to revoke the license via API if it was assigned less than 30 days ago.
The JetBrains Account Portal in the other hand allows to revoke such license, which makes it a subject for discussion.
So, **the tests are not repeatable** - eventually you will come across the situation when there are no licenses to assign left.

Secondly, there's no API method for creating the team and no method for getting a list of teams.
Unfortunately, several **team IDs had to be hardcoded**.

Thirdly, due to all above, I had to create a function for getting a list of licenses by different Predicates.
This **adds a complications (logic) to the tests** that are meant to be as simple as possible.

These problems can be solved by improving the API, extending it with some useful methods.

## Probable Bugs

1. Inconsistency with the Web Portal in terms of license revoking
2. Empty bodies in successful responses
   2.1. In case of assigning license by product code and team ID it's hard to define which license was assigned
3. Inconsistency in error's descriptions
4. In some cases the http response has 404 code, which isn't mentioned in the swagger spec
5. Request body examples in the swagger spec aren't matching their schema
6. License IDs aren't validated when changing team for them
7. First and Last names are required even if license is assigned to JetBrains account (but not required for the same assignment from Web Portal)


## Should be Clarified

1. Rules for name masking when assigning licenses