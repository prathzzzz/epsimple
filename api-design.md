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

#### 2.5 Cost Types (depends: CostCategory) ✅ COMPLETED
- ✅ `POST /api/cost-types` - Create cost type (requires costCategoryId)
- ✅ `GET /api/cost-types` - List cost types (with pagination and search)
- ✅ `GET /api/cost-types/search` - Search cost types
- ✅ `GET /api/cost-types/list` - Get all cost types as list
- ✅ `GET /api/cost-types/{id}` - Get cost type by ID
- ✅ `PUT /api/cost-types/{id}` - Update cost type
- ✅ `DELETE /api/cost-types/{id}` - Delete cost type
- ✅ Backend: Full CRUD with CostCategory FK validation
- ✅ Frontend: Full CRUD UI with CostCategory dropdown, data table, real-time search, pagination
- ✅ Fields: typeCode, typeName, typeDescription, costCategoryId (FK)

#### 2.6 Asset Categories (depends: AssetType) ✅ COMPLETED
- ✅ `POST /api/asset-categories` - Create category (requires assetTypeId)
- ✅ `GET /api/asset-categories` - List categories (with pagination and search)
- ✅ `GET /api/asset-categories/search` - Search categories
- ✅ `GET /api/asset-categories/list` - Get all categories as list
- ✅ `GET /api/asset-categories/{id}` - Get category by ID
- ✅ `PUT /api/asset-categories/{id}` - Update category
- ✅ `DELETE /api/asset-categories/{id}` - Delete category
- ✅ Backend: Full CRUD with AssetType FK validation, dependency protection
- ✅ Frontend: Full CRUD UI with AssetType dropdown, data table, real-time search, pagination
- ✅ Fields: categoryCode, categoryName, categoryDescription, assetTypeId (FK)
- ✅ Dependency Protection: AssetType cannot be deleted if AssetCategories exist

#### 2.7 Payee Details (depends: Bank) ✅ COMPLETED
- ✅ `POST /api/payee-details` - Create payee details (requires bankId)
- ✅ `GET /api/payee-details` - List payee details (with pagination and search)
- ✅ `GET /api/payee-details/search` - Search payee details
- ✅ `GET /api/payee-details/list` - Get all payee details as list
- ✅ `GET /api/payee-details/{id}` - Get payee details by ID
- ✅ `PUT /api/payee-details/{id}` - Update payee details
- ✅ `DELETE /api/payee-details/{id}` - Delete payee details
- ✅ Backend: Full CRUD with Bank FK validation, unique PAN/Aadhaar/AccountNumber validation
- ✅ Frontend: Full CRUD UI with Bank dropdown, multi-field form with validation patterns
- ✅ Fields: bankId (FK, required), payeeName (required), panNumber (10 chars, optional), aadhaarNumber (12 digits, optional), ifscCode (11 chars, optional), accountNumber (9-18 digits, required), branch, upiId
- ✅ Validation: PAN pattern (^[A-Z]{5}[0-9]{4}[A-Z]{1}$), Aadhaar (^\d{12}$), IFSC (^[A-Z]{4}0[A-Z0-9]{6}$), Account Number (^\d{9,18}$)
- ✅ Uniqueness: PAN, Aadhaar (optional fields), AccountNumber+Bank combination must be unique
- ✅ Bank Delete Protection: Enhanced to check for dependent PayeeDetails, shows up to 5 payee names
- ✅ UI Features: Auto-uppercase for PAN/IFSC, font-mono styling for sensitive fields, Sheet drawer with responsive grid
- ✅ Error Messages: Shows specific dependency details when deleting banks with payee details

---

### Phase 3: Level 2 Dependent Entities

#### 3.1 Location Management (depends: City) ✅ COMPLETED
- ✅ `POST /api/locations` - Create location (requires cityId)
- ✅ `GET /api/locations` - List locations (with pagination and search)
- ✅ `GET /api/locations/search` - Search locations by name/address/district/pincode
- ✅ `GET /api/locations/list` - Get all locations as list
- ✅ `GET /api/locations/{id}` - Get location details
- ✅ `PUT /api/locations/{id}` - Update location
- ✅ `DELETE /api/locations/{id}` - Delete location
- ✅ Backend: Full CRUD with City FK validation, comprehensive field validation
- ✅ Frontend: Full CRUD UI with City dropdown (shows cityName + stateName), data table, real-time search, pagination
- ✅ Fields: cityId (FK, required), locationName (required), address, district, pincode (6 digits), region, zone, latitude (-90 to 90, 2 int + 8 decimal), longitude (-180 to 180, 3 int + 8 decimal)
- ✅ Validation: Pincode pattern (^\d{6}$), Latitude/Longitude ranges with precision constraints
- ✅ Search: Searches across locationName, address, district, pincode
- ✅ Response: Includes cityName and stateName for display
- ✅ City Delete Protection: Enhanced to check for dependent Locations, shows up to 5 location names
- ✅ **Warehouse Delete Protection**: Enhanced to check for dependent Warehouses, shows up to 5 warehouse names
- ✅ UI Features: Sheet drawer with responsive grid, number inputs for lat/long, proper flex layout
- ✅ Table Columns: locationName, cityName, stateName, district, pincode, address
- ✅ Sidebar: Added under Location section
- ✅ Error Messages: Shows specific dependency details (e.g., "Cannot delete 'Mumbai' location because it is being used by N warehouses: Warehouse A, Warehouse B...")

#### 3.2 Vendor Management (depends: VendorType, PersonDetails) ✅ COMPLETED
- ✅ `POST /api/vendors` - Create vendor (requires vendorTypeId, personDetailsId)
- ✅ `GET /api/vendors` - List vendors (with pagination and search)
- ✅ `GET /api/vendors/search` - Search vendors
- ✅ `GET /api/vendors/list` - Get all vendors as list
- ✅ `GET /api/vendors/type/{typeId}` - Get vendors by type
- ✅ `GET /api/vendors/{id}` - Get vendor details
- ✅ `PUT /api/vendors/{id}` - Update vendor
- ✅ `DELETE /api/vendors/{id}` - Delete vendor
- ✅ Postman Collection: Created postman-vendor-apis.json with all 8 endpoints
- ✅ Frontend: Full CRUD UI with data table, drawer form, pagination
- ✅ Dependency Protection: VendorType and PersonDetails check before deletion
- ✅ UI Features: Dropdown for VendorType (shows category), PersonDetails (shows fullName and email), vendorCodeAlt uppercase input
- ✅ Table Columns: vendorCodeAlt, vendorName, vendorEmail, vendorContact, vendorTypeName, vendorCategoryName, actions
- ✅ Sidebar: Added under Vendor section
- ✅ Pagination Fix: Updated all API files to handle backend's nested pagination structure (page object)
- ✅ Error Messages: Consistent error handling via GlobalExceptionHandler

#### 3.3 Landlord Management ✅ COMPLETED (depends: PersonDetails)
- ✅ `POST /api/landlords` - Create landlord (requires personDetailsId)
- ✅ `GET /api/landlords` - List landlords (with pagination and search)
- ✅ `GET /api/landlords/search` - Search landlords
- ✅ `GET /api/landlords/list` - Get all landlords as list
- ✅ `GET /api/landlords/{id}` - Get landlord details
- ✅ `PUT /api/landlords/{id}` - Update landlord
- ✅ `DELETE /api/landlords/{id}` - Delete landlord
- ✅ Backend: Entity, Repository, DTOs, Mapper, Service, Controller
- ✅ Frontend: Full CRUD UI with drawer, table, columns, row actions
- ✅ Postman: Complete collection with all 7 endpoints
- ✅ PersonDetails dependency protection for landlords
- ✅ Rent share percentage validation (0-100)

#### 3.4 Cost Items (depends: CostType) ✅ COMPLETED
- ✅ `POST /api/cost-items` - Create cost item (requires costTypeId)
- ✅ `GET /api/cost-items` - List cost items (with pagination and search)
- ✅ `GET /api/cost-items/search` - Search cost items
- ✅ `GET /api/cost-items/list` - Get all cost items as list
- ✅ `GET /api/cost-items/{id}` - Get cost item by ID
- ✅ `PUT /api/cost-items/{id}` - Update cost item
- ✅ `DELETE /api/cost-items/{id}` - Delete cost item
- ✅ Backend: Full CRUD with CostType FK validation
- ✅ Frontend: Full CRUD UI with CostType dropdown, data table, real-time search, pagination

---

### Phase 4: Level 3 Dependent Entities

#### 4.1 Warehouse Management (depends: Location) ✅ COMPLETED
- ✅ `POST /api/warehouses` - Create warehouse (requires locationId)
- ✅ `GET /api/warehouses` - List warehouses (with pagination and search)
- ✅ `GET /api/warehouses/search` - Search warehouses
- ✅ `GET /api/warehouses/list` - Get all warehouses as list
- ✅ `GET /api/warehouses/{id}` - Get warehouse details
- ✅ `PUT /api/warehouses/{id}` - Update warehouse
- ✅ `DELETE /api/warehouses/{id}` - Delete warehouse
- ✅ Backend: Full CRUD with Location FK validation, unique warehouseCode validation, standard ResponseBuilder responses, comprehensive logging
- ✅ Frontend: Full CRUD UI with Location dropdown, data table, real-time search, pagination
- ✅ Fields: warehouseName (required), warehouseCode (optional, unique, uppercase alphanumeric with hyphens/underscores), warehouseType (optional), locationId (FK)
- ✅ Location Delete Protection: Shows specific warehouse names when deleting locations with warehouses
- ✅ Response Format: Uses standard ApiResponse<T> format via ResponseBuilder (consistent with all other controllers)
- ✅ Sidebar: Added under Master Data → Location → Warehouses with Warehouse icon
- ✅ Postman: Complete collection with all 7 endpoints (postman-warehouse-apis.json)

#### 4.2 Datacenter Management (depends: Location) ✅ COMPLETED
- ✅ `POST /api/datacenters` - Create datacenter (requires locationId)
- ✅ `GET /api/datacenters` - List datacenters (with pagination and search)
- ✅ `GET /api/datacenters/search` - Search datacenters
- ✅ `GET /api/datacenters/list` - Get all datacenters as list
- ✅ `GET /api/datacenters/{id}` - Get datacenter details
- ✅ `PUT /api/datacenters/{id}` - Update datacenter
- ✅ `DELETE /api/datacenters/{id}` - Delete datacenter
- ✅ Frontend: Full CRUD UI with data table, real-time search, pagination
- ✅ **Dependency Protection**: Location deletion checks for dependent datacenters
  - Error format: "Cannot delete 'Location Name' location because it is being used by X datacenter(s): DC1, DC2, DC3. Please delete or reassign these datacenters first."
- ✅ **Features**:
  - Unique datacenter code validation
  - Location FK validation
  - Standardized ResponseBuilder pattern
  - Real-time backend search (datacenterName, datacenterCode, datacenterType, locationName)
  - Integrated into sidebar navigation under Location section

#### 4.3 Activity Work Orders (depends: Activities, Vendor, GenericStatusType) ✅ COMPLETED
- ✅ `POST /api/activity-works` - Create work order (requires activitiesId, vendorId, statusTypeId)
- ✅ `GET /api/activity-works` - List work orders (with pagination and search)
- ✅ `GET /api/activity-works/search` - Search work orders
- ✅ `GET /api/activity-works/list` - Get all work orders as list
- ✅ `GET /api/activity-works/{id}` - Get work order details
- ✅ `PUT /api/activity-works/{id}` - Update work order
- ✅ `DELETE /api/activity-works/{id}` - Delete work order
- ✅ `GET /api/activity-works/count` - Get total count of work orders
- ✅ Backend: Full CRUD with Activities, Vendor, GenericStatusType FK validation
- ✅ Frontend: Full CRUD UI with dropdowns for Activities, Vendor, StatusType, data table with date formatting
- ✅ Fields: activitiesId (FK), vendorId (FK), vendorOrderNumber (max 100 chars), workOrderDate, workStartDate, workCompletionDate, statusTypeId (FK)
- ✅ **Dependency Protection**: 
  - Activities cannot be deleted if referenced by activity work orders
  - Vendors cannot be deleted if referenced by activity work orders
  - GenericStatusType cannot be deleted if referenced by activity work orders
  - Shows specific work order counts in error messages
- ✅ Search: Searches across activitiesName, vendorName (firstName, lastName), vendorOrderNumber, statusName
- ✅ Repository: Custom JPQL queries with JOIN FETCH for vendorDetails to build vendor name, prevents N+1 queries
- ✅ Mapper: Builds vendor name from PersonDetails (firstName, middleName, lastName)
- ✅ UI Features: Header/Main layout with navbar, Sheet drawer with 3 dropdowns + date pickers, responsive grid
- ✅ Table Columns: activitiesName, vendorName, vendorOrderNumber, workOrderDate, workStartDate, workCompletionDate, statusTypeName, actions
- ✅ Sidebar: Added under Activity section as "Activity Works"
- ✅ Postman: Complete collection with all 8 endpoints (postman-activitywork-apis.json)
- ✅ Date Handling: Frontend uses HTML5 date inputs, backend stores as LocalDate

#### 4.4 Payee Management (depends: PayeeType, PayeeDetails, Vendor, Landlord) ✅ COMPLETED
- ✅ `POST /api/payees` - Create payee (requires payeeTypeId, payeeDetailsId, optional vendorId/landlordId)
- ✅ `GET /api/payees` - List payees (with pagination and search)
- ✅ `GET /api/payees/search` - Search payees
- ✅ `GET /api/payees/list` - Get all payees as list
- ✅ `GET /api/payees/{id}` - Get payee details
- ✅ `PUT /api/payees/{id}` - Update payee
- ✅ `DELETE /api/payees/{id}` - Delete payee
- ✅ `GET /api/payees/count` - Get total count of payees
- ✅ Backend: Full CRUD with PayeeType, PayeeDetails, optional Vendor/Landlord FK validation
- ✅ Frontend: Full CRUD UI with conditional dropdowns, data table with polymorphic display
- ✅ Fields: payeeTypeId (FK), payeeDetailsId (FK), vendorId (FK, optional), landlordId (FK, optional)
- ✅ **Dependency Protection**:
  - PayeeType cannot be deleted if referenced by payees
  - PayeeDetails cannot be deleted if referenced by payees
  - Vendor cannot be deleted if referenced by payees (in addition to activity work orders)
  - Landlord cannot be deleted if referenced by payees
  - Shows specific payee counts in error messages
- ✅ Search: Searches across payee details name, vendor name (PersonDetails), landlord name (PersonDetails)
- ✅ Repository: Custom JPQL queries with JOIN FETCH for vendorDetails and landlordDetails, prevents N+1 queries
- ✅ Mapper: Builds vendor/landlord names from PersonDetails (firstName, middleName, lastName)
- ✅ UI Features: Header/Main layout with navbar, narrower drawer (500px), conditional vendor/landlord fields
- ✅ Table Columns: payeeTypeName, payeeDetailsName, vendorName (if applicable), landlordName (if applicable), actions
- ✅ Sidebar: Added under Master Data → Financial section
- ✅ Polymorphic Design: Payee can be linked to either vendor or landlord (or neither), validated at service layer

---

### Phase 5: Level 4 Core Business Entities

#### 5.1 Site Management (depends: ManagedProject, SiteCategory, Location, SiteType, GenericStatusType, PersonDetails) ✅ COMPLETED
- ✅ `POST /api/sites` - Create site (requires locationId, siteCode; optional projectId, siteCategoryId, siteTypeId, siteStatusId, 5 contact PersonDetails)
- ✅ `GET /api/sites` - List sites (with pagination and search)
- ✅ `GET /api/sites/search` - Search sites by siteCode, oldSiteCode, locationName, projectName, categoryName, typeName
- ✅ `GET /api/sites/list` - Get all sites as list (for dropdowns)
- ✅ `GET /api/sites/{id}` - Get site full details with all 60+ fields
- ✅ `PUT /api/sites/{id}` - Update site
- ✅ `DELETE /api/sites/{id}` - Delete site (protected by AssetsOnSite and SiteActivityWorkExpenditure dependencies)
- ✅ Backend: Full CRUD with comprehensive FK validation, dependency protection with detailed error messages
- ✅ Frontend: Tabbed drawer UI with 6 tabs (Basic, Dates, Infrastructure, Network, Technical, Cassettes)
- ✅ Frontend: Refactored into smaller components (site-drawer-tabs folder)
- ✅ Fields: 60+ fields across categories:
  - Basic: siteCode*, locationId*, projectId, siteCategoryId, siteTypeId, siteStatusId, projectPhase, oldSiteCode, previousMspTermId, locationClass
  - Dates: techLiveDate, cashLiveDate, siteCloseDate, possessionDate, actualPossessionDate
  - Infrastructure: groutingStatus, itStabilizer, rampStatus, upsBatteryBackupCapacity, connectivityType, acUnits, mainDoorGlassWidth, fixedGlassWidth, signboardSize, brandingSize
  - Network: gatewayIp, atmIp, subnetMask, natIp, switchIp, tlsPort, tlsDomainName, ejDocket
  - Technical: tssDocket, otcActivationStatus, craName
  - Cassettes: cassetteSwapStatus, cassetteType1, cassetteType2, cassetteType3, cassetteType4
  - Contacts: channelManagerContactId, regionalManagerContactId, stateHeadContactId, bankPersonContactId, masterFranchiseeContactId (all FK to PersonDetails)
- ✅ **Dependency Protection**: 
  - Location deletion checks for Sites with specific site codes
  - Site deletion checks for AssetsOnSite and SiteActivityWorkExpenditure
  - Shows counts and specific names: "Cannot delete 'Site-001' because it is being used by 5 assets on site and 3 activity work expenditures"
- ✅ Validation: Unique siteCode (uppercase alphanumeric only, no separators), Location exists, optional FK validation for Project/Category/Type/Status/Contacts
- ✅ Search: Comprehensive backend search across siteCode, oldSiteCode, location, project, category, type
- ✅ Mapper: Maps all FKs with names (locationName, projectName, etc.), builds PersonDetails fullName from firstName+middleName+lastName
- ✅ UI Features: 
  - Sheet drawer with sm:max-w-3xl width
  - Tabbed interface with proper spacing and rounded corners
  - Dark mode support for date inputs
  - Consistent field widths (flex-1) across all tabs
  - Responsive grid layouts (sm:grid-cols-2)
- ✅ Sidebar: Added under Sites section
- ✅ Postman: Complete collection with all 7 endpoints (postman-site-apis.json)

#### 5.1.1 Site Code Generator (depends: ManagedProject, State) ✅ COMPLETED
- ✅ `POST /api/site-code-generators` - Create code generation rule (requires projectId, stateId)
- ✅ `GET /api/site-code-generators` - List all code generation rules (with pagination and search)
- ✅ `GET /api/site-code-generators/search` - Search code generators by projectName, stateName
- ✅ `GET /api/site-code-generators/list` - Get all generators as list (for dropdowns)
- ✅ `GET /api/site-code-generators/{id}` - Get generator details
- ✅ `PUT /api/site-code-generators/{id}` - Update generator (modify maxSeqDigit, runningSeq)
- ✅ `DELETE /api/site-code-generators/{id}` - Delete generator (protected if sites exist with codes from this generator)
- ✅ `POST /api/site-code-generators/generate` - Generate next site code (requires projectId, stateId in body)
- ✅ `GET /api/site-code-generators/preview` - Preview next code without incrementing (query params: projectId, stateId)
- ✅ Backend: Entity, Repository, Service with smart sequence detection, Controller, DTOs, Mapper
- ✅ Frontend: Full CRUD UI with drawer, table, auto-generate integration in Site form
- ✅ Postman: Complete collection with all 9 endpoints (postman-sitecode-apis.json)
- **Code Generation Logic**:
  - Unique constraint: (projectId + stateId) combination
  - Format: `{ProjectCode}{StateCode}{Sequence}` (no separators, no category code)
  - Example: `7BOIKA00004` (7-Eleven BOI + Karnataka + sequence 4)
  - maxSeqDigit: Configurable sequence padding (default: 5 digits, range: 1-10)
  - Thread-safe: PESSIMISTIC_WRITE lock for sequence increments
  - Auto-create: Generator auto-created on first generate if not exists
  - **Smart Sequence Detection**: Queries database for existing site codes, extracts max sequence using regex (`\\d+$`), uses Math.max(maxExisting+1, runningSeq)
  - **Bulk Upload Protection**: Self-healing after manual data imports, automatically adjusts sequence to prevent duplicates
- **Integration with Sites**:
  - Sparkles button ✨ next to Site Code input in Site form BasicTab
  - Requires Project + Location (extracts State from Location) selection first
  - Category removed from entire system (no longer required)
  - Auto-populate: Clicking ✨ generates code and fills siteCode field
  - Toast feedback: Shows generated code and next available sequence
- **Dependency Protection**: 
  - Cannot delete generator if Sites with matching project+state exist
  - Shows specific site codes in error message
- **Fields**: 
  - projectId (FK, required)
  - stateId (FK, required)
  - maxSeqDigit (integer, default: 5, range: 1-10)
  - runningSeq (integer, default: 1, auto-increments on generate)
- **Validation**: 
  - Unique (projectId + stateId)
  - Project must exist
  - State must exist
  - maxSeqDigit must be 1-10
- **Search**: Backend searches across projectName, stateName (JOIN queries)
- **Response DTOs**:
  - SiteCodeGeneratorResponseDto: id, projectId, projectName, projectCode, stateId, stateName, stateCode, maxSeqDigit, runningSeq
  - GeneratedSiteCodeDto: siteCode, projectCode, stateCode, sequence, nextSequence
- **Sidebar**: Added under Sites section as "Site Code Generators"
- **Table Columns**: Project (with code), State (with code), Max Digits, Current Seq, Actions

#### 5.2 Asset Management System ✅ COMPLETED

##### 5.2.1 Asset Tag Code Generator (depends: AssetCategory, Vendor, Bank) ✅ COMPLETED
- ✅ `POST /api/asset-tag-generators` - Create asset tag generation rule (requires assetCategoryId, vendorId, bankId)
- ✅ `GET /api/asset-tag-generators` - List all asset tag generators (with pagination and search)
- ✅ `GET /api/asset-tag-generators/search` - Search generators by categoryName, vendorName, bankName
- ✅ `GET /api/asset-tag-generators/list` - Get all generators as list (for dropdowns)
- ✅ `GET /api/asset-tag-generators/{id}` - Get generator details
- ✅ `PUT /api/asset-tag-generators/{id}` - Update generator (modify maxSeqDigit, runningSeq)
- ✅ `DELETE /api/asset-tag-generators/{id}` - Delete generator (protected if assets exist with tags from this generator)
- ✅ `POST /api/asset-tag-generators/generate` - Generate next asset tag (requires assetCategoryId, vendorId, bankId in body)
- ✅ `GET /api/asset-tag-generators/preview` - Preview next tag without incrementing (query params: assetCategoryId, vendorId, bankId)
- ✅ Backend: Entity, Repository, DTOs, Mapper, Service, Controller all implemented
- ✅ Frontend: Full CRUD UI with drawer, table, auto-generate integration
- ✅ Smart Sequence Detection: Queries Asset repository for existing tags, prevents duplicates
- ✅ Bulk Upload Protection: Self-healing after manual data imports
- ✅ Navigation: Added to sidebar under Assets section
- **Tag Generation Logic**:
  - Unique constraint: (assetCategoryId + vendorId + bankId) combination
  - Format: `{AssetCategoryCode}{VendorCode}{BankCode}{Sequence}` (no separators)
  - Example: `ATMHDbob00001` (ATM category + HD vendor + bob bank + sequence 1)
  - maxSeqDigit: Configurable sequence padding (default: 5 digits, range: 1-10)
  - Thread-safe: PESSIMISTIC_WRITE lock for sequence increments
  - Auto-create: Generator auto-created on first generate if not exists
- **Integration with Assets**:
  - Sparkles button ✨ next to Asset Tag input in Asset form
  - Requires AssetCategory + Vendor + Bank selection first
  - Auto-populate: Clicking ✨ generates tag and fills assetTag field
  - Toast feedback: Shows generated tag and next available sequence
- **Dependency Protection**: 
  - Cannot delete generator if Assets with matching category+vendor+bank exist
  - Shows specific asset tags in error message
- **Fields**: 
  - assetCategoryId (FK, required)
  - vendorId (FK, required)
  - bankId (FK, required)
  - maxSeqDigit (integer, default: 5, range: 1-10)
  - runningSeq (integer, default: 1, auto-increments on generate)
- **Validation**: 
  - Unique (assetCategoryId + vendorId + bankId)
  - AssetCategory must exist
  - Vendor must exist
  - Bank must exist
  - maxSeqDigit must be 1-10
- **Search**: Backend searches across assetCategoryName, vendorName, bankName (JOIN queries)
- **Response DTOs**:
  - AssetTagCodeGeneratorResponseDto: id, assetCategoryId, assetCategoryName, assetCategoryCode, vendorId, vendorName, vendorCodeAlt, bankId, bankName, bankCode, maxSeqDigit, runningSeq
  - GeneratedAssetTagDto: assetTag, assetCategoryCode, vendorCode, bankCode, sequence, nextSequence

##### 5.2.2 Asset CRUD (depends: AssetType, AssetCategory, Vendor, Bank, GenericStatusType) ✅ COMPLETED
- ✅ `POST /api/assets` - Create asset
- ✅ `GET /api/assets` - List all assets (with pagination)
- ✅ `GET /api/assets/search` - Search assets (by tag, serial, model, name, category, type, vendor, bank)
- ✅ `GET /api/assets/list` - Get all assets with full details
- ✅ `GET /api/assets/{id}` - Get asset by ID
- ✅ `PUT /api/assets/{id}` - Update asset
- ✅ `DELETE /api/assets/{id}` - Delete asset
- ✅ Backend: Entity, Repository, DTOs, Mapper, Service, Controller all implemented
- ✅ Frontend: Full CRUD UI with drawer, table, pagination, real-time search
- ✅ Navigation: Added to sidebar under Assets section
- ✅ Auto-Generate Integration: Sparkles button ✨ next to Asset Tag input
- ✅ Pagination Fix: Backend returns Page<AssetResponseDto> instead of List
- ✅ Bulk Select: Removed from table
- **Fields**: assetTagId (unique, required), assetName, serialNumber (unique), assetTypeId, assetCategoryId, vendorId, lenderBankId, statusTypeId, purchaseOrderNumber, purchaseOrderDate, purchaseOrderCost, dispatchOrderNumber, dispatchOrderDate, warrantyPeriod, warrantyExpiryDate, endOfLifeDate, endOfSupportDate
- **Validation**: Unique assetTagId and serialNumber
- **Search**: Backend searches across assetTagId, assetName, serialNumber, categoryName, typeName, vendorName, bankName
- **Integration**: Auto-generate button integrated with AssetTagCodeGenerator
- **Postman**: Complete collection with all 7 endpoints (postman-asset-apis.json)

#### 5.3 Activity Work Remarks (depends: ActivityWork) ✅ COMPLETED
- ✅ `POST /api/activity-work-remarks` - Add remark (requires activityWorkId, comment)
- ✅ `GET /api/activity-work-remarks/activity-work/{activityWorkId}` - List remarks for work (with pagination)
- ✅ `GET /api/activity-work-remarks/activity-work/{activityWorkId}/list` - Get all remarks for work
- ✅ `GET /api/activity-work-remarks/{id}` - Get remark by ID
- ✅ `PUT /api/activity-work-remarks/{id}` - Update remark
- ✅ `DELETE /api/activity-work-remarks/{id}` - Delete remark
- ✅ `GET /api/activity-work-remarks/activity-work/{activityWorkId}/count` - Count remarks for activity work
- ✅ Backend: Entity (already exists), Repository, DTOs, Mapper, Service, Controller all implemented
- ✅ Frontend: Dialog component with add/edit/delete, real-time count badge, integrated into Activity Work row actions
- **Features**:
  - Automatic timestamp on creation (commentedOn)
  - Sorted by commentedOn DESC (newest first)
  - Pagination support with configurable page size
  - Activity work validation before creating remark
  - Count endpoint for badge display
  - Edit/Delete functionality with confirmation
  - Responsive dialog with ScrollArea for long lists
  - Formatted relative timestamps (e.g., "2 hours ago")
- **Fields**: activityWorkId (FK, required), comment (TEXT, required), commentedOn (auto-generated), commentedBy (Long, optional - for future user integration)
- **Validation**: ActivityWork must exist, comment cannot be blank
- **Response**: Includes activityWorkId for easy reference
- **UI Integration**: Remarks button in Activity Work table actions with badge showing count

#### 5.4 Payment Details (depends: PaymentMethod, Bank) ✅ COMPLETED
- ✅ `POST /api/payment-details` - Create payment record (requires paymentMethodId, bankId)
- ✅ `GET /api/payment-details` - List payment records (with pagination and search)
- ✅ `GET /api/payment-details/search` - Search payment details by transactionNumber, payerName, beneficiaryName, description
- ✅ `GET /api/payment-details/list` - Get all payment details as list
- ✅ `GET /api/payment-details/{id}` - Get payment details by ID
- ✅ `PUT /api/payment-details/{id}` - Update payment details
- ✅ `DELETE /api/payment-details/{id}` - Delete payment details
- ✅ Backend: Entity, Repository, DTOs, Mapper, Service, Controller all implemented
- ✅ Frontend: Full CRUD UI with drawer, table, pagination, real-time search
- ✅ Postman: Complete collection with all 7 endpoints (postman-payment-details-apis.json)
- ✅ Navigation: Added to sidebar under Financial section
- **Fields**: paymentMethodId (FK, required), bankId (FK, required), transactionNumber (unique, required), transactionDate (required), transactionAmount (required, positive), payerName (required), beneficiaryName (required), description (TEXT, optional)
- **Validation**: PaymentMethod exists, Bank exists, unique transactionNumber, positive amount
- **Search**: Backend searches across transactionNumber, payerName, beneficiaryName, description
- **Response**: Includes paymentMethodName and bankName for display
- **UI Features**: Header/Main layout with navbar, Sheet drawer with date picker, responsive grid, formatted currency display
- **Table Columns**: transactionNumber, paymentMethodName, bankName, transactionDate, transactionAmount, payerName, beneficiaryName, actions
- **Date Handling**: Uses modernized DatePicker component with date-fns formatting

#### 5.5 Invoice Management (depends: Payee, PaymentDetails) ✅ COMPLETED
- ✅ `POST /api/invoices` - Create invoice (requires payeeId)
- ✅ `GET /api/invoices` - List invoices (with pagination and search, filter by payee, status, dates)
- ✅ `GET /api/invoices/search` - Search invoices by invoice number, vendor name, order number, payee name, payment status, transaction number
- ✅ `GET /api/invoices/list` - Get all invoices as list
- ✅ `GET /api/invoices/payee/{payeeId}` - List invoices by payee
- ✅ `GET /api/invoices/{id}` - Get invoice details
- ✅ `PUT /api/invoices/{id}` - Update invoice
- ✅ `PUT /api/invoices/{id}/payment-status` - Update payment status
- ✅ `DELETE /api/invoices/{id}` - Delete invoice
- ✅ Backend: Entity (exists), Repository, DTOs, Mapper, Service, Controller all implemented
- ✅ Frontend: Full CRUD UI with tabbed drawer (Basic, Financial, Other), table, pagination, real-time search
- ✅ Postman: Complete collection with all 9 endpoints (Invoice-API.postman_collection.json)
- ✅ Navigation: Added to sidebar under Financial section
- **Fields**: invoiceNumber (unique, required), invoiceDate (required), invoiceReceivedDate, orderNumber, vendorName, payeeId (FK, required), paymentDetailsId (FK, optional), paymentDueDate, paymentStatus, quantity, unit, unitPrice, taxCgstPercentage, taxSgstPercentage, taxIgstPercentage, basicAmount, cgst, sgst, igst, amount1, amount2, discountPercentage, discountAmount, tds, advanceAmount, totalAmount, totalInvoiceValue, netPayable, paidDate, machineSerialNumber, masterPoNumber, masterPoDate, dispatchOrderNumber, dispatchOrderDate, utrDetail, billedByVendorGst, billedToEpsGst, remarks
- **Validation**: Payee exists, unique invoice number, optional payment details validation
- **Search**: Backend searches across invoiceNumber, vendorName, orderNumber, payeeName, paymentStatus, transactionNumber
- **Response**: Includes payeeName, payeeTypeName, transactionNumber for display
- **UI Features**: Tabbed form with Basic (invoice details, payee, dates), Financial (pricing, taxes, amounts), Other (remarks), formatted currency display
- **Table Columns**: invoiceNumber, invoiceDate, payeeName, vendorName, totalInvoiceValue, paymentStatus, paymentDueDate, actions
- **Date Handling**: Uses modernized DatePicker component with date-fns formatting

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
