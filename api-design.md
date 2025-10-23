# API Design

## API Development Flow by Dependency Order

### Phase 1: Master Data Setup (Independent Entities)

#### 1.1 Geographic Masters ✅ COMPLETED
- ✅ `POST /api/states` - Create state
- ✅ `GET /api/states` - List all states (with pagination and search)
- ✅ `GET /api/states/search` - Search states
- ✅ `GET /api/states/list` - Get all states as list
- ✅ `GET /api/states/{id}` - Get state by ID
- ✅ `PUT /api/states/{id}` - Update state
- ✅ `DELETE /api/states/{id}` - Delete state
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.2 Bank Masters ✅ COMPLETED
- ✅ `POST /api/banks` - Create bank
- ✅ `GET /api/banks` - List all banks (with pagination and search)
- ✅ `GET /api/banks/search` - Search banks
- ✅ `GET /api/banks/list` - Get all banks as list
- ✅ `GET /api/banks/{id}` - Get bank details
- ✅ `PUT /api/banks/{id}` - Update bank
- ✅ `DELETE /api/banks/{id}` - Delete bank (with dependency protection for managed projects)
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination, file upload for logos
- ✅ **Dependency Protection**: Shows specific error with project names when deleting banks with managed projects
  - Error format: "Cannot delete 'Bank of Baroda' bank because it is being used by 3 managed projects: Project Alpha, Project Beta, Project Gamma. Please delete or reassign these managed projects first."

#### 1.3 Person Type Masters ✅ COMPLETED
- ✅ `POST /api/person-types` - Create person type
- ✅ `GET /api/person-types` - List person types (with pagination and search)
- ✅ `GET /api/person-types/search` - Search person types
- ✅ `GET /api/person-types/list` - Get all person types as list
- ✅ `GET /api/person-types/{id}` - Get person type by ID
- ✅ `PUT /api/person-types/{id}` - Update person type
- ✅ `DELETE /api/person-types/{id}` - Delete person type
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.4 Vendor Category Masters ✅ COMPLETED
- ✅ `POST /api/vendor-categories` - Create vendor category
- ✅ `GET /api/vendor-categories` - List vendor categories (with pagination and search)
- ✅ `GET /api/vendor-categories/search` - Search vendor categories
- ✅ `GET /api/vendor-categories/list` - Get all vendor categories as list
- ✅ `GET /api/vendor-categories/{id}` - Get vendor category by ID
- ✅ `PUT /api/vendor-categories/{id}` - Update vendor category
- ✅ `DELETE /api/vendor-categories/{id}` - Delete vendor category
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.5 Activity Masters ✅ COMPLETED
- ✅ `POST /api/activity` - Create activity
- ✅ `GET /api/activity` - List activities (with pagination and search)
- ✅ `GET /api/activity/search` - Search activities
- ✅ `GET /api/activity/list` - Get all activities as list
- ✅ `GET /api/activity/{id}` - Get activity by ID
- ✅ `PUT /api/activity/{id}` - Update activity
- ✅ `DELETE /api/activity/{id}` - Delete activity (with dependency validation)
- ✅ Backend: Full CRUD with validation and dependency checking
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination
- ✅ Enhanced: Friendly error messages showing which Activities records are using the Activity
- ✅ Fields: activityName, activityDescription

#### 1.6 Generic Status Type Masters ✅ COMPLETED
- ✅ `POST /api/generic-status-types` - Create generic status type
- ✅ `GET /api/generic-status-types` - List generic status types (with pagination and search)
- ✅ `GET /api/generic-status-types/search` - Search generic status types
- ✅ `GET /api/generic-status-types/list` - Get all generic status types as list
- ✅ `GET /api/generic-status-types/{id}` - Get generic status type by ID
- ✅ `PUT /api/generic-status-types/{id}` - Update generic status type
- ✅ `DELETE /api/generic-status-types/{id}` - Delete generic status type
- ✅ Backend: Full CRUD with validation
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination
- ✅ Fields: statusName, statusCode, description
- ✅ **Note**: This master serves as a generic reusable status type system that can be used for Ownership Status, Site Status, Asset Status, etc. by categorizing entries with appropriate status codes.

#### 1.7 Site Classification Masters ✅ COMPLETED
- ✅ `POST /api/site-types` - Create site type
- ✅ `GET /api/site-types` - List site types (with pagination and search)
- ✅ `GET /api/site-types/search` - Search site types
- ✅ `GET /api/site-types/list` - Get all site types as list
- ✅ `GET /api/site-types/{id}` - Get site type by ID
- ✅ `PUT /api/site-types/{id}` - Update site type
- ✅ `DELETE /api/site-types/{id}` - Delete site type
- ✅ Backend: Full CRUD with validation
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination, Sheet drawer
- ✅ Fields: typeName, description
- ✅ `POST /api/site-categories` - Create site category
- ✅ `GET /api/site-categories` - List site categories (with pagination and search)
- ✅ `GET /api/site-categories/search` - Search site categories
- ✅ `GET /api/site-categories/list` - Get all site categories as list
- ✅ `GET /api/site-categories/{id}` - Get site category by ID
- ✅ `PUT /api/site-categories/{id}` - Update site category
- ✅ `DELETE /api/site-categories/{id}` - Delete site category
- ✅ Backend: Full CRUD with validation (with categoryCode regex validation)
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination, Sheet drawer, delete dialog
- ✅ Fields: categoryName, categoryCode (optional, uppercase alphanumeric), description
- ✅ Sidebar: Grouped under "Sites" parent menu with Site Types and Site Categories

#### 1.8 Asset Type Masters ✅ COMPLETED
- ✅ `POST /api/asset-types` - Create asset type
- ✅ `GET /api/asset-types` - List asset types (with pagination and search)
- ✅ `GET /api/asset-types/search` - Search asset types
- ✅ `GET /api/asset-types/list` - Get all asset types as list
- ✅ `GET /api/asset-types/{id}` - Get asset type by ID
- ✅ `PUT /api/asset-types/{id}` - Update asset type
- ✅ `DELETE /api/asset-types/{id}` - Delete asset type
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.9 Asset Movement Type Masters ✅ COMPLETED
- ✅ `POST /api/movement-types` - Create movement type
- ✅ `GET /api/movement-types` - List movement types (with pagination and search)
- ✅ `GET /api/movement-types/search` - Search movement types
- ✅ `GET /api/movement-types/list` - Get all movement types as list
- ✅ `GET /api/movement-types/{id}` - Get movement type by ID
- ✅ `PUT /api/movement-types/{id}` - Update movement type
- ✅ `DELETE /api/movement-types/{id}` - Delete movement type
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.10 Cost Category Masters ✅ COMPLETED
- ✅ `POST /api/cost-categories` - Create cost category
- ✅ `GET /api/cost-categories` - List cost categories (with pagination and search)
- ✅ `GET /api/cost-categories/search` - Search cost categories
- ✅ `GET /api/cost-categories/list` - Get all cost categories as list
- ✅ `GET /api/cost-categories/{id}` - Get cost category by ID
- ✅ `PUT /api/cost-categories/{id}` - Update cost category
- ✅ `DELETE /api/cost-categories/{id}` - Delete cost category
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.11 Payment Method Masters ✅ COMPLETED
- ✅ `POST /api/payment-methods` - Create payment method
- ✅ `GET /api/payment-methods` - List payment methods (with pagination and search)
- ✅ `GET /api/payment-methods/search` - Search payment methods
- ✅ `GET /api/payment-methods/list` - Get all payment methods as list
- ✅ `GET /api/payment-methods/{id}` - Get payment method by ID
- ✅ `PUT /api/payment-methods/{id}` - Update payment method
- ✅ `DELETE /api/payment-methods/{id}` - Delete payment method
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

#### 1.12 Payee Type Masters ✅ COMPLETED
- ✅ `POST /api/payee-types` - Create payee type
- ✅ `GET /api/payee-types` - List payee types (with pagination and search)
- ✅ `GET /api/payee-types/search` - Search payee types
- ✅ `GET /api/payee-types/list` - Get all payee types as list
- ✅ `GET /api/payee-types/{id}` - Get payee type by ID
- ✅ `PUT /api/payee-types/{id}` - Update payee type
- ✅ `DELETE /api/payee-types/{id}` - Delete payee type
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination

---

### Phase 2: Level 1 Dependent Entities

#### 2.1 City Management (depends: State) ✅ COMPLETED
- ✅ `POST /api/cities` - Create city (requires stateId)
- ✅ `GET /api/cities` - List cities (with pagination and search)
- ✅ `GET /api/cities/search` - Search cities
- ✅ `GET /api/cities/state/{stateId}` - List cities by state
- ✅ `GET /api/cities/list` - Get all cities as list
- ✅ `GET /api/cities/{id}` - Get city details
- ✅ `PUT /api/cities/{id}` - Update city
- ✅ `DELETE /api/cities/{id}` - Delete city
- ✅ Backend: Full CRUD with State FK validation and duplicate cityCode checks
- ✅ Frontend: Full CRUD UI with State dropdown, data table, real-time search, pagination, Sheet drawer
- ✅ Fields: cityName, cityCode (optional, alphanumeric), stateId (FK to State)
- ✅ Sidebar: Added under Master Data → Location
- ✅ Repository Pattern: Uses CityRepository (not SeederRepository)
- ✅ State Delete Protection: Optimized with pagination (fetches only 6 records), shows first 5 city names
- ✅ Error Messages: Shows specific dependency details (e.g., "Cannot delete 'Maharashtra' state because it is being used by 15 cities: Mumbai, Pune, Nagpur, Thane, Nashik and 10 more")

#### 2.2 Vendor Type Masters (depends: VendorCategory) ✅ COMPLETED - REFACTORED
- ✅ `POST /api/vendor-types` - Create vendor type (requires vendorCategoryId)
- ✅ `GET /api/vendor-types` - List vendor types (with pagination and search)
- ✅ `GET /api/vendor-types/search` - Search vendor types
- ✅ `GET /api/vendor-types/list` - Get all vendor types as list
- ✅ `GET /api/vendor-types/{id}` - Get vendor type by ID
- ✅ `PUT /api/vendor-types/{id}` - Update vendor type
- ✅ `DELETE /api/vendor-types/{id}` - Delete vendor type
- ✅ Frontend: Full CRUD UI with category dropdown, data table, real-time search, pagination
- ✅ **Note**: VendorType now has FK to VendorCategory (vendorCategoryId required)

#### 2.2.1 Activities (depends: Activity) ✅ COMPLETED
- ✅ `POST /api/activities` - Create activities entry (requires activityId)
- ✅ `GET /api/activities` - List activities (with pagination and search)
- ✅ `GET /api/activities/search` - Search activities
- ✅ `GET /api/activities/list` - Get all activities as list
- ✅ `GET /api/activities/{id}` - Get activities entry by ID
- ✅ `PUT /api/activities/{id}` - Update activities entry
- ✅ `DELETE /api/activities/{id}` - Delete activities entry
- ✅ Backend: Full CRUD with Activity FK validation
- ✅ Frontend: Full CRUD UI with Activity dropdown, data table, real-time search, pagination
- ✅ Relationship: ManyToOne with Activity entity (activityId required)
- ✅ Fields: activityId (FK), activityName, activityCategory, activityDescription

#### 2.3 Person Details (depends: PersonType) ✅ COMPLETED
- ✅ `POST /api/person-details` - Create person (requires personTypeId)
- ✅ `GET /api/person-details` - List persons (with pagination and search)
- ✅ `GET /api/person-details/search` - Search persons
- ✅ `GET /api/person-details/person-type/{personTypeId}` - List persons by type
- ✅ `GET /api/person-details/list` - Get all persons as list
- ✅ `GET /api/person-details/{id}` - Get person details
- ✅ `PUT /api/person-details/{id}` - Update person
- ✅ `DELETE /api/person-details/{id}` - Delete person
- ✅ Backend: Full CRUD with PersonType FK validation and email uniqueness checks
- ✅ Frontend: Full CRUD UI with PersonType dropdown, multi-field form (name, email, contact, addresses), backend search
- ✅ Fields: personTypeId (FK), firstName, middleName, lastName, fullName (computed), contactNumber (10 digits), email (unique, required), permanentAddress, correspondenceAddress
- ✅ Mapper: Builds fullName from firstName + middleName + lastName
- ✅ Search: Backend searches across firstName, middleName, lastName, email, contactNumber
- ✅ Sidebar: Added under Master Data → Person → Person Details
- ✅ PersonType Delete Protection: Shows specific person names (e.g., "Cannot delete 'Vendor' person type because it is being used by 3 person details: Pratham Shah, John Doe, Jane Smith")
- ✅ UI Features: Sheet drawer (600px), 3-column name grid, 2-column email/contact grid, Textarea for addresses, pagination fix (0 instead of -1)

#### 2.4 Managed Projects ✅ COMPLETED (depends: Bank)
- ✅ `POST /api/managed-projects` - Create managed project (requires bankId)
- ✅ `GET /api/managed-projects` - List all managed projects (with pagination and search)
- ✅ `GET /api/managed-projects/search` - Search managed projects
- ✅ `GET /api/managed-projects/list` - Get all managed projects as list
- ✅ `GET /api/managed-projects/bank/{bankId}` - List projects by bank
- ✅ `GET /api/managed-projects/{id}` - Get project details
- ✅ `PUT /api/managed-projects/{id}` - Update project
- ✅ `DELETE /api/managed-projects/{id}` - Delete project
- ✅ Backend: Full CRUD with Bank FK validation, unique projectCode validation
- ✅ Frontend: Full CRUD UI with Bank dropdown, multi-field form (bankId, projectName, projectCode, projectType, projectDescription)
- ✅ Fields: bankId (FK, required), projectName (required, max 255), projectCode (optional, unique, alphanumeric with hyphens/underscores, max 50), projectType (optional, max 50), projectDescription (optional, max 5000)
- ✅ Validation: Bank existence check, projectCode uniqueness (create/update), alphanumeric pattern for projectCode
- ✅ Search: Searches across projectName, projectCode, projectType
- ✅ Mapper: Maps bank.id → bankId, bank.bankName → bankName
- ✅ Sidebar: Added under Master Data → Banks → Managed Projects
- ✅ UI Features: Header/Main layout with navbar, Sheet drawer with proper padding, proper flex layout for form/footer
- ✅ API Error Handling: Uses handleServerError to display backend error messages instead of generic messages

#### 2.4 Activity Instances (depends: Activity)
- `POST /api/activity-instances` - Create activity instance
- `GET /api/activity-instances` - List activity instances
- `GET /api/activity-instances/{id}` - Get details

#### 2.5 Cost Types (depends: CostCategory)
- `POST /api/cost-types` - Create cost type (requires costCategoryId)
- `GET /api/cost-types` - List cost types
- `GET /api/cost-categories/{categoryId}/types` - List types by category

#### 2.6 Asset Categories (depends: AssetType)
- `POST /api/asset-categories` - Create category (requires assetTypeId)
- `GET /api/asset-categories` - List categories
- `GET /api/asset-types/{typeId}/categories` - List categories by type

#### 2.7 Payee Details (depends: Bank)
- `POST /api/payee-details` - Create payee details (requires bankId)
- `GET /api/payee-details` - List payee details
- `GET /api/payee-details/{id}` - Get details

---

### Phase 3: Level 2 Dependent Entities

#### 3.1 Location Management (depends: City)
- `POST /api/locations` - Create location (requires cityId)
- `GET /api/locations` - List locations
- `GET /api/cities/{cityId}/locations` - List locations by city
- `GET /api/locations/{id}` - Get location details
- `PUT /api/locations/{id}` - Update location
- `DELETE /api/locations/{id}` - Delete location

#### 3.2 Vendor Management (depends: VendorType, PersonDetails)
- `POST /api/vendors` - Create vendor (requires vendorTypeId, personDetailsId)
- `GET /api/vendors` - List vendors
- `GET /api/vendors/{id}` - Get vendor details
- `PUT /api/vendors/{id}` - Update vendor
- `DELETE /api/vendors/{id}` - Delete vendor

#### 3.3 Landlord Management (depends: PersonDetails)
- `POST /api/landlords` - Create landlord (requires personDetailsId)
- `GET /api/landlords` - List landlords
- `GET /api/landlords/{id}` - Get landlord details

#### 3.4 Cost Items (depends: CostType)
- `POST /api/cost-items` - Create cost item (requires costTypeId)
- `GET /api/cost-items` - List cost items
- `GET /api/cost-types/{typeId}/items` - List items by type

---

### Phase 4: Level 3 Dependent Entities

#### 4.1 Warehouse Management (depends: Location)
- `POST /api/warehouses` - Create warehouse (requires locationId)
- `GET /api/warehouses` - List warehouses
- `GET /api/warehouses/{id}` - Get warehouse details
- `PUT /api/warehouses/{id}` - Update warehouse

#### 4.2 Datacenter Management (depends: Location)
- `POST /api/datacenters` - Create datacenter (requires locationId)
- `GET /api/datacenters` - List datacenters
- `GET /api/datacenters/{id}` - Get datacenter details

#### 4.3 Activity Work Orders (depends: Activities, Vendor, GenericStatusType)
- `POST /api/activity-works` - Create work order (requires activitiesId, vendorId, statusTypeId)
- `GET /api/activity-works` - List work orders
- `GET /api/activity-works/{id}` - Get work order details
- `PUT /api/activity-works/{id}` - Update work order status
- `GET /api/vendors/{vendorId}/activity-works` - List works by vendor

#### 4.4 Payee Management (depends: PayeeType, PayeeDetails, Vendor, Landlord)
- `POST /api/payees` - Create payee (requires payeeTypeId, payeeDetailsId, optional vendorId/landlordId)
- `GET /api/payees` - List payees
- `GET /api/payees/{id}` - Get payee details

---

### Phase 5: Level 4 Core Business Entities

#### 5.1 Site Management (depends: Project, SiteCategory, Location, SiteType, Status, PersonDetails)
- `POST /api/sites` - Create site (requires projectId, locationId, etc.)
- `GET /api/sites` - List sites (filter by project, location, status)
- `GET /api/sites/{id}` - Get site full details
- `PUT /api/sites/{id}` - Update site
- `DELETE /api/sites/{id}` - Delete site
- `GET /api/projects/{projectId}/sites` - List sites by project
- `GET /api/locations/{locationId}/sites` - List sites by location

#### 5.2 Asset Management (depends: AssetType, AssetCategory, Vendor, Bank, Status)
- `POST /api/assets` - Create asset (requires assetTypeId, categoryId, vendorId, lenderBankId)
- `GET /api/assets` - List assets (filter by type, category, vendor, status)
- `GET /api/assets/{id}` - Get asset details
- `PUT /api/assets/{id}` - Update asset
- `DELETE /api/assets/{id}` - Delete asset
- `GET /api/vendors/{vendorId}/assets` - List assets by vendor

#### 5.3 Activity Work Remarks (depends: ActivityWork)
- `POST /api/activity-works/{workId}/remarks` - Add remark
- `GET /api/activity-works/{workId}/remarks` - List remarks for work

#### 5.4 Payment Details (depends: PaymentMethod, Bank)
- `POST /api/payment-details` - Create payment record
- `GET /api/payment-details` - List payment records
- `GET /api/payment-details/{id}` - Get payment details

#### 5.5 Invoice Management (depends: Payee, PaymentDetails)
- `POST /api/invoices` - Create invoice (requires payeeId)
- `GET /api/invoices` - List invoices (filter by payee, status, dates)
- `GET /api/invoices/{id}` - Get invoice details
- `PUT /api/invoices/{id}` - Update invoice
- `PUT /api/invoices/{id}/payment` - Update payment status
- `GET /api/payees/{payeeId}/invoices` - List invoices by payee

#### 5.6 Voucher Management (depends: Payee, PaymentDetails)
- `POST /api/vouchers` - Create voucher (requires payeeId)
- `GET /api/vouchers` - List vouchers
- `GET /api/vouchers/{id}` - Get voucher details
- `PUT /api/vouchers/{id}` - Update voucher
- `GET /api/payees/{payeeId}/vouchers` - List vouchers by payee

---

### Phase 6: Complex Transaction Entities

#### 6.1 Asset Placement Operations
- `POST /api/assets/{assetId}/place-on-site` - Place asset on site (requires siteId, statusId)
- `POST /api/assets/{assetId}/place-on-warehouse` - Place asset in warehouse
- `POST /api/assets/{assetId}/place-on-datacenter` - Place asset in datacenter
- `GET /api/sites/{siteId}/assets` - List assets on site
- `GET /api/warehouses/{warehouseId}/assets` - List assets in warehouse
- `GET /api/datacenters/{datacenterId}/assets` - List assets in datacenter

#### 6.2 Asset Movement Tracking
- `POST /api/asset-movements` - Track asset movement
- `GET /api/assets/{assetId}/movements` - Get asset movement history
- `GET /api/asset-movements/{id}` - Get movement details

#### 6.3 Expenditure Management - Invoice Based
- `POST /api/expenditures/invoices` - Create expenditure invoice (requires costItemId, invoiceId, projectId)
- `GET /api/expenditures/invoices` - List expenditure invoices
- `GET /api/projects/{projectId}/expenditures/invoices` - List project invoice expenditures
- `GET /api/invoices/{invoiceId}/expenditures` - Get expenditures for invoice

#### 6.4 Expenditure Management - Voucher Based
- `POST /api/expenditures/vouchers` - Create expenditure voucher (requires costItemId, voucherId, projectId)
- `GET /api/expenditures/vouchers` - List expenditure vouchers
- `GET /api/projects/{projectId}/expenditures/vouchers` - List project voucher expenditures

#### 6.5 Site Activity Work Expenditure Linkage
- `POST /api/sites/{siteId}/activity-work-expenditures` - Link site activity work with expenditure
- `GET /api/sites/{siteId}/activity-work-expenditures` - List site work expenditures
- `GET /api/activity-works/{workId}/expenditures` - Get expenditures for activity work

#### 6.6 Asset Expenditure and Activity Work
- `POST /api/assets/{assetId}/expenditure-work-link` - Link asset procurement with expenditure and work
- `GET /api/assets/{assetId}/expenditure-work-details` - Get asset expenditure and work details

---

## Common API Patterns

### Filtering & Pagination
All list APIs support:
- `?page=1&size=20` - Pagination
- `?sortBy=fieldName&sortOrder=asc|desc` - Sorting
- Entity-specific filters (status, dates, parent entities)

### Response Structure
```
Success: { data: {}, message: "", status: 200 }
Error: { error: "", message: "", status: 4xx/5xx }
List: { data: [], totalCount: 0, page: 1, size: 20 }
```

### Validation Flow
1. Check required parent entities exist
2. Validate foreign key references
3. Check business rules
4. Perform transaction

### Delete Operations
- Soft delete where audit trail needed
- Cascade considerations for dependent entities
- Prevent deletion if referenced by active records
