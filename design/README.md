# design

## user flow / navigation & content

### access without login
landingpage (https://catena-x.net/de/)
Portal (content and navigation needs to be defined for ART1)
- App Store
- Services
- Data Catalog
- Vocabulary
- Developers Hub
- Semantic Hub
- Digital Twin
- Imprint / Legal information
- Share
- Help & FAQ
- Search
- Login
- ...


### access with Login
#### Role: Company Admin
- Portal +
- Notification Center
- Purchasing / Bank information
- User Management
- Company Profile
- Home
  - my Apps
  - my Data
  - my Connectors
- Profile
- Logout

#### Role: (Standard-) User
- Portal +
- Notification Center
- Home
  - my Apps
  - my Data
  - my Connectors
- Profile
- Logout

#### Role: Catena-X Admin
- Portal +
- Notification
- Member Management
  - invite Company
    - Email of contact person
    - Company name / one ID
    -> Send invite
  - edit Company
  - delete Company
...



### Flow for onboarding as Company Admin
Invite email & credentials (authentication process needs tbd)
- Register company
- Enter credentials to verify identity

Getting started (Wizard)
1. Verify company data
2. Set up Responsibilities & Accounts
3. Choose Catena-X Company Role
4. Terms & Conditions
5. IDP (where to place this step tbd)
6. Certificates
