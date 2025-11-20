# RBAC Implementation Plan

## Overview
This document outlines the phased approach to implementing the RBAC system. Each phase builds upon the previous one to create a complete permission-based access control system where ADMIN manages everything, and custom roles provide granular access control.

### Module Architecture
- **auth-module**: Pure authentication/authorization library (entities, repositories, JWT, permission checking)
- **epsoneapi**: Business logic and RBAC implementation (permission registry, seeders, role management APIs, annotations)

All permission definitions, seeders, and business logic live in `epsoneapi`. The `auth-module` only contains generic entities and services.

---

## Phase 1: Database & Entity Updates

**Goal**: Add new fields to existing Permission and Role entities to support scoped permissions, categories, and system/custom role distinction.

### Tasks

#### 1.1 Update Permission Entity
#### 1.1 Update Permission Entity
File: `server-api/auth-module/src/main/java/com/eps/module/auth/entity/Permission.java`

**What**: Add fields to support scoped permissions with categories.

**Add fields**:
- `String scope` - Resource scope (e.g., ASSET, SITE, BANK)
- `String action` - Action type (CREATE, READ, UPDATE, DELETE)
- `Boolean isSystemPermission` - Distinguish pre-seeded vs custom permissions
- `Boolean isActive` - Enable/disable permissions
- `String category` - UI grouping (e.g., "Core Masters", "Operations")

#### 1.2 Update Role Entity
File: `server-api/auth-module/src/main/java/com/eps/module/auth/entity/Role.java`

**What**: Add fields to distinguish system roles from custom roles.

**Add fields**:
- `Boolean isSystemRole` - Marks ADMIN as system role (cannot be deleted)
- `Boolean isActive` - Enable/disable roles

**Note**: Color coding for UI badges is handled on frontend, not stored in database.

#### 1.3 Update Repository Interfaces

**PermissionRepository** (`server-api/auth-module/src/main/java/com/eps/module/auth/repository/PermissionRepository.java`)

**What**: Add query methods to find permissions by various criteria.

**Add methods**:
- `List<Permission> findByIsActive(Boolean isActive)` - Get active permissions
- `List<Permission> findByScope(String scope)` - Get permissions for specific resource
- `List<Permission> findByAction(String action)` - Get all CREATE, READ, etc.
- `List<Permission> findByCategory(String category)` - Get permissions by UI category
- `List<Permission> findByIsSystemPermission(Boolean isSystem)` - System vs custom permissions

**RoleRepository** (`server-api/auth-module/src/main/java/com/eps/module/auth/repository/RoleRepository.java`)

**What**: Add methods to query roles with their permissions efficiently.

**Add methods**:
- `List<Role> findByIsActive(Boolean isActive)` - Get active roles
- `List<Role> findByIsSystemRole(Boolean isSystemRole)` - Get system/custom roles
- `Optional<Role> findByIdWithPermissions(Long id)` - Fetch role with permissions in one query (use @Query with JOIN FETCH)

### Deliverables
- ✅ Permission entity has scope, action, category fields
- ✅ Role entity has isSystemRole, isActive fields
- ✅ Repository methods added for querying

---

## Phase 2: Permission Registry & Seeding

**Goal**: Create a code-based permission registry in epsoneapi that auto-generates 6 basic permissions for all entity scopes, then seed them into the database automatically on application startup.

### Tasks

#### 2.1 Create Permission Registry
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/registry/PermissionRegistry.java`

**What**: Central registry that auto-generates basic permissions for all entity scopes. Single source of truth for all permissions.

**Basic Actions** (auto-generated for every scope):
1. `CREATE` - Create new records
2. `READ` - View/list records
3. `UPDATE` - Modify existing records
4. `DELETE` - Remove records
5. `BULK_UPLOAD` - Bulk import via Excel/CSV
6. `EXPORT` - Export data to Excel/CSV

**Entity Scopes** (each gets all 6 basic actions):
- **Core Masters**: STATE, CITY, LOCATION, WAREHOUSE, DATACENTER, ASSET_TYPE, ASSET_CATEGORY, MOVEMENT_TYPE, ASSET_TAG_GENERATOR, SITE_TYPE, SITE_CATEGORY, SITE_CODE_GENERATOR, ACTIVITY, BANK, COST_CATEGORY, COST_TYPE, COST_ITEM, PAYMENT_METHOD, PAYEE_TYPE, VENDOR_CATEGORY, VENDOR_TYPE, PERSON_TYPE, STATUS_TYPE
- **Operations**: ASSET, SITE, ACTIVITY_LIST, ACTIVITY_WORK, MANAGED_PROJECT, ASSETS_ON_SITE, ASSETS_ON_WAREHOUSE, ASSETS_ON_DATACENTER
- **Financial**: INVOICE, VOUCHER, EXPENDITURE_INVOICE, EXPENDITURE_VOUCHER, ASSET_EXPENDITURE, SITE_ACTIVITY_WORK_EXPENDITURE, PAYMENT, PAYEE_DETAILS, PAYEE
- **People & Organizations**: VENDOR, LANDLORD, PERSON_DETAILS
- **System**: USER, ROLE

**Custom Actions** (added manually as needed):
- `ASSET:PLACE` - Place asset on site/warehouse/datacenter (distinct from CREATE which creates new asset records)
- `ASSET:TRANSFER` - Transfer asset between locations
- `ASSET:VIEW_MOVEMENT_HISTORY` - View complete asset movement/placement history
- `ASSET:VIEW_CURRENT_LOCATION` - Check where asset is currently placed
- `ACTIVITY_WORK:ASSIGN` - Assign activity work to personnel
- (Add more custom permissions as features are developed)

**Implementation**:
```java
public class PermissionRegistry {
    private static final String[] BASIC_ACTIONS = {
        "CREATE", "READ", "UPDATE", "DELETE", "BULK_UPLOAD", "EXPORT"
    };
    
    public List<PermissionDefinition> getAllPermissions() {
        List<PermissionDefinition> permissions = new ArrayList<>();
        
        // Auto-generate basic 6 for each scope
        for (String scope : getAllScopes()) {
            for (String action : BASIC_ACTIONS) {
                permissions.add(new PermissionDefinition(
                    scope + ":" + action,
                    "Can " + action.toLowerCase() + " " + scope.toLowerCase(),
                    scope,
                    action,
                    getCategoryForScope(scope)
                ));
            }
        }
        
        // Add custom actions
        permissions.addAll(getCustomPermissions());
        
        return permissions;
    }
}
```

**Why**: Adding new entity = just add scope name. Automatically gets 6 basic permissions. No manual permission definition needed.

**Important**: Asset placement (ASSET:PLACE) is a custom permission separate from ASSETS_ON_SITE:CREATE, ASSETS_ON_WAREHOUSE:CREATE, and ASSETS_ON_DATACENTER:CREATE. This is because:
- ASSET:PLACE is the user action ("I want to place this asset somewhere")
- ASSETS_ON_SITE:CREATE creates the placement record in the database
- Typically, users with ASSET:PLACE also need ASSETS_ON_SITE:CREATE, ASSETS_ON_WAREHOUSE:CREATE, ASSETS_ON_DATACENTER:CREATE
- But you could have users who can create placement records programmatically without the PLACE permission

#### 2.2 Create Permission Constants
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/permissions/Permissions.java`

**What**: String constants for all permissions so you can reference them in code without typos.

**Auto-generated from registry**: Use PermissionRegistry to generate constants at build time or runtime.

**Example**:
```java
public class Permissions {
    // Asset permissions (auto-generated basic 6)
    public static final String ASSET_CREATE = "ASSET:CREATE";
    public static final String ASSET_READ = "ASSET:READ";
    public static final String ASSET_UPDATE = "ASSET:UPDATE";
    public static final String ASSET_DELETE = "ASSET:DELETE";
    public static final String ASSET_BULK_UPLOAD = "ASSET:BULK_UPLOAD";
    public static final String ASSET_EXPORT = "ASSET:EXPORT";
    
    // Asset custom permissions
    public static final String ASSET_PLACE = "ASSET:PLACE";
    public static final String ASSET_TRANSFER = "ASSET:TRANSFER";
    public static final String ASSET_VIEW_MOVEMENT_HISTORY = "ASSET:VIEW_MOVEMENT_HISTORY";
    public static final String ASSET_VIEW_CURRENT_LOCATION = "ASSET:VIEW_CURRENT_LOCATION";
    
    // Assets on Site (auto-generated basic 6)
    public static final String ASSETS_ON_SITE_CREATE = "ASSETS_ON_SITE:CREATE";
    public static final String ASSETS_ON_SITE_READ = "ASSETS_ON_SITE:READ";
    public static final String ASSETS_ON_SITE_UPDATE = "ASSETS_ON_SITE:UPDATE";
    public static final String ASSETS_ON_SITE_DELETE = "ASSETS_ON_SITE:DELETE";
    // ... same for ASSETS_ON_WAREHOUSE and ASSETS_ON_DATACENTER
    
    // Activity Work custom permissions
    public static final String ACTIVITY_WORK_ASSIGN = "ACTIVITY_WORK:ASSIGN";
    
    // Expenditure permissions (auto-generated for each expenditure type)
    public static final String EXPENDITURE_INVOICE_CREATE = "EXPENDITURE_INVOICE:CREATE";
    public static final String EXPENDITURE_VOUCHER_CREATE = "EXPENDITURE_VOUCHER:CREATE";
    public static final String ASSET_EXPENDITURE_CREATE = "ASSET_EXPENDITURE:CREATE";
    public static final String SITE_ACTIVITY_WORK_EXPENDITURE_CREATE = "SITE_ACTIVITY_WORK_EXPENDITURE:CREATE";
    
    // State permissions (Core Master - auto-generated basic 6)
    public static final String STATE_CREATE = "STATE:CREATE";
    public static final String STATE_READ = "STATE:READ";
    // ... etc
}
```

#### 2.3 Create Permission Seeder
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/seeder/PermissionSeeder.java`

#### 2.3 Create Permission Seeder
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/seeder/PermissionSeeder.java`

**What**: Seeder that uses PermissionRegistry to automatically create all permissions in database.

**Logic**:
1. Inject `PermissionRepository` from auth-module
2. Get all permission definitions from `PermissionRegistry.getAllPermissions()`
3. For each permission:
   - Check if it exists in database (by name)
   - If not, create new Permission entity with scope, action, category, isSystemPermission=true
   - If exists, update description/category if changed
4. Run on application startup using `@PostConstruct` or `ApplicationRunner`

**Reusability**: Generic logic works for any permission. Adding new scope = zero code changes to seeder.

**Why**: Permissions stay in sync with code. No manual SQL scripts needed.

#### 2.4 Test Permission Seeding

**What**: Run application and verify permissions are created correctly.

**Check**:
- All basic 6 permissions created for each scope (6 x number of scopes)
- Custom permissions (ASSET:TRANSFER, etc.) created
- Correct scope, action, category values
- isSystemPermission = true for all
- No duplicates

**Example**: If you have 30 scopes, you should see ~180 basic permissions (30 x 6) plus custom ones.

### Deliverables
- ✅ PermissionRegistry auto-generates 6 basic actions per scope
- ✅ Permission constants file created
- ✅ Seeder uses registry (lives in epsoneapi, not auth-module)
- ✅ All permissions seeded on startup
- ✅ Reusable and extensible (add scope = auto-get 6 permissions)

---

## Phase 3: Role Seeder & ADMIN Role

**Goal**: Create role seeder in epsoneapi that creates only the ADMIN role with all permissions.

### Tasks

#### 3.1 Create Role Seeder
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/seeder/RoleSeeder.java`

**What**: Create ADMIN role with all permissions. Runs after PermissionSeeder.

**Logic**:
1. Inject `RoleRepository` and `PermissionRepository` from auth-module
2. Check if ADMIN role exists
3. If not, create ADMIN role:
   - Fetch ALL permissions from database
   - Assign all permissions to ADMIN
   - Set `isSystemRole = true`
   - Set description: "Administrator with complete system access"
4. If exists, update to ensure it has all current permissions
5. Run on application startup after PermissionSeeder

**Why**: ADMIN should have everything. Other roles are created by ADMIN through UI as needed.

#### 3.2 Update User Seeder
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/seeder/UserSeeder.java` (or keep in auth-module if it exists there)

**What**: Ensure default admin user gets ADMIN role.

**Changes**:
- Find ADMIN role (created by RoleSeeder)
- Assign ADMIN role to admin user
- Remove references to other roles

#### 3.3 Test ADMIN Role

**Verify**:
- ADMIN role exists in database
- ADMIN role has ALL permissions (check count matches permission seeder)
- Admin user has ADMIN role
- Can login as admin
- Admin has access to all endpoints

### Deliverables
- ✅ ADMIN role created with all permissions
- ✅ No other pre-seeded roles
- ✅ Admin user has ADMIN role
- ✅ Admin can access everything
- ✅ Seeder lives in epsoneapi (business logic layer)

---

## Phase 4: Permission Service & Checking

**Goal**: Create service to check if users have specific permissions. Support multi-role permission resolution (user has permission if ANY of their roles grants it).

### Tasks

#### 4.1 Create Permission Service
File: `server-api/auth-module/src/main/java/com/eps/module/auth/service/PermissionService.java`

**What**: Central service for all permission checking logic.

**Methods**:
- `hasPermission(Authentication auth, String permission)` - Check if authenticated user has permission
- `hasPermission(User user, String permission)` - Check if user has permission
- `hasAnyPermission(User user, String... permissions)` - User has at least one
- `hasAllPermissions(User user, String... permissions)` - User has all
- `getUserPermissions(User user)` - Get all permission names for user
- `isAdmin(User user)` - Check if user has ADMIN role

**Multi-Role Logic**: 
- Get all roles for user
- Get all permissions from all roles
- If permission is in the combined set, return true

#### 4.2 Create Custom Annotations
Files: 
- `RequirePermission.java` - Single permission required
- `RequireAnyPermission.java` - At least one permission required
- `RequireAdmin.java` - Must be ADMIN role

**What**: Annotations to protect controller endpoints.

**Usage**:
```java
@RequirePermission(permission = "ASSET:CREATE")
public ResponseEntity<AssetDTO> createAsset(...) { }

@RequireAdmin
public ResponseEntity<RoleDTO> createRole(...) { }
```

#### 4.3 Update CustomUserDetailsService
File: `server-api/auth-module/src/main/java/com/eps/module/auth/service/CustomUserDetailsService.java`

**What**: Update `getAuthorities()` method to load all permissions from all user roles.

**Logic**:
- For each role user has
- Add "ROLE_" + role name (for @PreAuthorize compatibility)
- Add all permissions from that role
- Return combined authorities

**Why**: Spring Security needs all authorities loaded for security checks.

### Deliverables
- ✅ PermissionService created
- ✅ Custom annotations created
- ✅ Multi-role permission union working
- ✅ Spring Security authorities include all permissions

---

## Phase 5: Role Management APIs

**Goal**: Create APIs in epsoneapi for ADMIN to manage roles - create custom roles, assign permissions to roles, assign roles to users.

### Tasks

#### 5.1 Create Role Service
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/service/RoleManagementService.java`

**What**: Business logic for role management. Uses repositories from auth-module.

**Implement methods**:
- `RoleDTO createRole(CreateRoleRequest request)` - ADMIN only
- `RoleDTO updateRole(Long id, UpdateRoleRequest request)` - ADMIN only
- `void deleteRole(Long id)` - ADMIN only, prevent system role deletion
- `RoleDTO updateRolePermissions(Long id, UpdateRolePermissionsRequest request)` - ADMIN only
- `List<RoleDTO> getAllRoles()`
- `RoleDTO getRoleById(Long id)`

**Reusability**: Inject `RoleRepository` and `PermissionRepository` from auth-module. Generic role management logic.

#### 5.2 Create DTOs
Files in: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/dto/`
- `CreateRoleRequest.java` - name, description, permissionIds
- `UpdateRoleRequest.java` - name, description
- `UpdateRolePermissionsRequest.java` - permissionIds (array)
- `RoleDTO.java` - id, name, description, permissions, isSystemRole
- `PermissionDTO.java` - id, name, description, scope, action, category

#### 5.3 Create Role Controller
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/controller/RoleController.java`

**Endpoints**:
- `GET /api/roles` - List all roles
- `GET /api/roles/{id}` - Get role details
- `POST /api/roles` - Create role (ADMIN only via @RequireAdmin)
- `PUT /api/roles/{id}` - Update role (ADMIN only)
- `DELETE /api/roles/{id}` - Delete role (ADMIN only)
- `PUT /api/roles/{id}/permissions` - Update role permissions (ADMIN only)

**Why epsoneapi?**: Business logic and API layer. auth-module only has core entities.

#### 5.4 Create User Role Service
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/service/UserRoleService.java`

**Add methods**:
- `UserDTO assignRoleToUser(Long userId, Long roleId)` - ADMIN only
- `UserDTO removeRoleFromUser(Long userId, Long roleId)` - ADMIN only
- `Set<String> getUserPermissions(Long userId)` - Get all permission names

**Reusability**: Uses `UserRepository` and `RoleRepository` from auth-module.

#### 5.5 Create User Role Controller
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/controller/UserRoleController.java`

**Add endpoints**:
- `POST /api/users/{userId}/roles/{roleId}` - Assign role (ADMIN only)
- `DELETE /api/users/{userId}/roles/{roleId}` - Remove role (ADMIN only)
- `GET /api/users/{userId}/permissions` - Get user's all permissions

### Deliverables
- ✅ RoleManagementService implemented (in epsoneapi)
- ✅ Role CRUD endpoints working
- ✅ User-role assignment working
- ✅ ADMIN-only access enforced via annotations
- ✅ System role protection working (cannot delete ADMIN role)
- ✅ Reusable services using auth-module repositories

---

## Phase 6: Permission Management APIs

**Goal**: Create permission management endpoints in epsoneapi for viewing and grouping permissions.

### Tasks

#### 6.1 Create Permission Management Service
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/service/PermissionManagementService.java`

**What**: Service for querying and managing permissions.

**Implement methods**:
- `List<PermissionDTO> getAllPermissions()` - Get all active permissions
- `Map<String, List<PermissionDTO>> getPermissionsByCategory()` - Group by category for UI
- `List<PermissionDTO> getPermissionsByScope(String scope)` - Get all permissions for entity
- `PermissionDTO createCustomPermission(CreatePermissionRequest request)` - ADMIN only (for custom actions)

**Reusability**: Uses `PermissionRepository` from auth-module. Can be reused across different contexts.

**Why**: Frontend needs grouped permissions for role creation UI. Custom permissions allow extending beyond 6 basic actions.

#### 6.2 Create Permission Controller
File: `server-api/epsoneapi/src/main/java/com/eps/epsoneapi/rbac/controller/PermissionController.java`

**Endpoints**:
- `GET /api/permissions` - Get all permissions
- `GET /api/permissions/grouped` - Get permissions grouped by category
- `GET /api/permissions/my` - Get current user's permissions
- `POST /api/permissions` - Create custom permission (ADMIN only)

**Why**: ADMIN needs to see all available permissions when creating roles.

#### 6.3 Update Auth DTOs
File: `server-api/auth-module/src/main/java/com/eps/module/auth/dto/UserDTO.java` (or create in epsoneapi)

**What**: Add `Set<String> allPermissions` field.

**Why**: Frontend needs to know user's permissions for showing/hiding UI elements.

**Logic**: When mapping User to UserDTO, compute all permission names from all user's roles (union of permissions).

**Example**:
```java
user.getRoles().stream()
    .flatMap(role -> role.getPermissions().stream())
    .map(Permission::getName)
    .collect(Collectors.toSet());
```

### Deliverables
- ✅ Permission endpoints created (in epsoneapi)
- ✅ Permissions grouped by category for UI
- ✅ UserDTO includes allPermissions
- ✅ Custom permission creation works

---

## Phase 7: Core Masters Protection

**Goal**: Protect all Core Master endpoints so only ADMIN can access them.

### Tasks

#### 7.1 Apply @RequireAdmin to Core Master Controllers

**What**: Add @RequireAdmin annotation to all controller methods for Core Masters.

**Core Master Controllers**:

**Location Setup**:
- `StateController` - @RequireAdmin on all methods
- `CityController` - @RequireAdmin on all methods
- `LocationController` - @RequireAdmin on all methods  
- `WarehouseController` - @RequireAdmin on all methods
- `DatacenterController` - @RequireAdmin on all methods

**Asset Configuration**:
- `AssetTypeController` - @RequireAdmin
- `AssetCategoryController` - @RequireAdmin
- `MovementTypeController` - @RequireAdmin
- `AssetTagGeneratorController` - @RequireAdmin

**Site Configuration**:
- `SiteTypeController` - @RequireAdmin
- `SiteCategoryController` - @RequireAdmin
- `SiteCodeGeneratorController` - @RequireAdmin

**Activity Configuration**:
- `ActivityController` - @RequireAdmin (this is the master, not activities list)

**Financial Setup**:
- `BankController` - @RequireAdmin
- `CostCategoryController` - @RequireAdmin
- `CostTypeController` - @RequireAdmin
- `CostItemController` - @RequireAdmin
- `PaymentMethodController` - @RequireAdmin
- `PayeeTypeController` - @RequireAdmin

**Classifications**:
- `VendorCategoryController` - @RequireAdmin
- `VendorTypeController` - @RequireAdmin
- `PersonTypeController` - @RequireAdmin
- `GenericStatusTypeController` - @RequireAdmin

**Why**: Core Masters are system configuration. Only ADMIN should modify them.

#### 7.2 Test Core Masters Access

**Test Cases**:
1. Login as ADMIN - should access all Core Masters ✅
2. Login as non-admin user - should get 403 Forbidden ❌
3. Try to create/update/delete Core Master without ADMIN - should fail

### Deliverables
- ✅ All Core Master controllers protected
- ✅ Only ADMIN can access Core Masters
- ✅ Non-admin users get proper error responses

---

## Phase 8: Operational Endpoints Protection

**Goal**: Apply permission-based protection to operational endpoints (Assets, Sites, Invoices, etc.).

### Tasks

#### 8.1 Apply @RequirePermission to Controllers

**What**: Add @RequirePermission annotation to operational controllers based on CRUD operation.

**Pattern**:
- GET list/detail → `@RequirePermission("SCOPE:READ")`
- POST create → `@RequirePermission("SCOPE:CREATE")`
- PUT update → `@RequirePermission("SCOPE:UPDATE")`
- DELETE → `@RequirePermission("SCOPE:DELETE")`
- POST bulk-upload → `@RequirePermission("SCOPE:BULK_UPLOAD")`

**Controllers to Update**:

**Asset Management**:
- `AssetController` - ASSET:CREATE, ASSET:READ, ASSET:UPDATE, ASSET:DELETE, ASSET:BULK_UPLOAD

**Site Management**:
- `SiteController` - SITE:CREATE, SITE:READ, SITE:UPDATE, SITE:DELETE, SITE:BULK_UPLOAD

**Activity Management**:
- `ActivitiesListController` - ACTIVITY_LIST:CREATE, READ, UPDATE, DELETE
- `ActivityWorkController` - ACTIVITY_WORK:CREATE, READ, UPDATE, DELETE, ASSIGN

**Project Management**:
- `ManagedProjectController` - MANAGED_PROJECT:CREATE, READ, UPDATE, DELETE

**Financial**:
- `InvoiceController` - INVOICE:CREATE, READ, UPDATE, DELETE
- `VoucherController` - VOUCHER:CREATE, READ, UPDATE, DELETE
- `PaymentDetailsController` - PAYMENT:CREATE, READ, UPDATE, DELETE
- `ExpenditureController` - EXPENDITURE:CREATE, READ, UPDATE, DELETE

**People & Organizations**:
- `VendorController` - VENDOR:CREATE, READ, UPDATE, DELETE
- `LandlordController` - LANDLORD:CREATE, READ, UPDATE, DELETE
- `PersonDetailsController` - PERSON_DETAILS:CREATE, READ, UPDATE, DELETE

#### 8.2 Test with Custom Roles

**Create Test Roles** (via API as ADMIN):
1. **ASSET_MANAGER** - ASSET:*, WAREHOUSE:*, DATACENTER:* permissions
2. **SITE_MANAGER** - SITE:*, LOCATION:* permissions
3. **FINANCE_MANAGER** - INVOICE:*, VOUCHER:*, PAYMENT:* permissions

**Create Test Users**:
- User with ASSET_MANAGER role
- User with SITE_MANAGER role
- User with both ASSET_MANAGER and SITE_MANAGER roles (multi-role test)

**Test Access**:
- ASSET_MANAGER can access assets but not sites ✅
- SITE_MANAGER can access sites but not assets ✅
- User with both roles can access both ✅
- User without permission gets 403 ❌

### Deliverables
- ✅ All operational controllers protected
- ✅ Permission-based access working
- ✅ Custom roles can be created
- ✅ Multi-role permission union working

---

## Phase 9: Frontend Permission Infrastructure

**Goal**: Build the foundation for permission-based UI - auth store, permission hooks, and guard components that show/hide UI elements based on user permissions.

### Tasks

#### 9.1 Update Auth Types
File: `client-app/src/lib/auth-api.ts`

**What**: Update TypeScript interfaces to include permission data.

**Updates**:
```typescript
export interface Permission {
  id: number
  name: string
  description: string
  scope: string
  action: string
  category: string
}

export interface Role {
  id: number
  name: string
  description: string
  colorCode: string
  permissions: Permission[]
}

export interface AuthUser {
  id: number
  email: string
  name: string
  isActive: boolean
  roles: Role[]
  allPermissions: string[] // Array of permission names like ["ASSET:CREATE", "SITE:READ"]
}
```

**Why**: Frontend needs permission data to show/hide UI elements.

#### 9.2 Create Permission Hook
File: `client-app/src/hooks/use-permission.ts`

**What**: React hook that provides permission checking functions.

**Functions**:
- `hasPermission(permission: string)` - Check single permission
- `hasAnyPermission(permissions: string[])` - Check if user has at least one
- `isAdmin()` - Check if user has ADMIN role
- `can(permission: string)` - Alias for hasPermission

**Usage**:
```typescript
const { can, isAdmin } = usePermission()

if (can('ASSET:CREATE')) {
  // Show create button
}

if (isAdmin()) {
  // Show admin menu
}
```

**Why**: Centralized permission checking for all components.

#### 9.3 Create Permission Guard Component
File: `client-app/src/components/permission-guard.tsx`

**What**: Component that conditionally renders children based on permissions.

**Props**:
- `permission?: string` - Single permission required
- `anyPermissions?: string[]` - At least one required
- `requireAdmin?: boolean` - Must be admin
- `children: ReactNode` - What to show if has permission
- `fallback?: ReactNode` - What to show if lacks permission (default: null)

**Usage**:
```typescript
<PermissionGuard permission="ASSET:CREATE">
  <Button>Create Asset</Button>
</PermissionGuard>

<PermissionGuard requireAdmin>
  <Link to="/states">States</Link>
</PermissionGuard>
```

**Why**: Declarative way to show/hide UI elements.

#### 9.4 Create Admin Guard Component
File: `client-app/src/components/admin-guard.tsx`

**What**: Special guard for admin-only content (wrapper around PermissionGuard).

**Usage**:
```typescript
<AdminGuard>
  <AdminPanel />
</AdminGuard>
```

### Deliverables
- ✅ Auth types include permission data
- ✅ usePermission hook created
- ✅ PermissionGuard component created
- ✅ AdminGuard component created

---

## Phase 10: Frontend Permission UI Integration

**Goal**: Apply permission guards throughout the UI to show/hide buttons, links, and sections based on user permissions.

### Tasks

#### 10.1 Update Sidebar Navigation
File: `client-app/src/components/layout/data/sidebar-data.ts`

**What**: Add permission requirements to navigation items.

**Updates**:
```typescript
{
  title: 'Core Masters',
  requireAdmin: true, // Only show to ADMIN
  items: [...]
}

{
  title: 'Assets',
  permission: 'ASSET:READ', // Show if has ASSET:READ
  url: '/assets'
}
```

File: `client-app/src/components/layout/nav-group.tsx`

**What**: Check permissions before rendering nav items.

**Logic**:
- If item has `requireAdmin`, check if user is admin
- If item has `permission`, check if user has that permission
- Only render nav item if check passes
- Hide entire groups if user has no access to any items in group

**Why**: Users only see menu items they can access.

#### 10.2 Update List Pages (Assets, Sites, etc.)

**What**: Wrap action buttons with PermissionGuard.

**Example**: `client-app/src/features/assets/index.tsx`

**Updates**:
```typescript
// Create button
<PermissionGuard permission="ASSET:CREATE">
  <Button onClick={handleCreate}>
    Create Asset
  </Button>
</PermissionGuard>

// Bulk upload button
<PermissionGuard permission="ASSET:BULK_UPLOAD">
  <Button onClick={handleBulkUpload}>
    Bulk Upload
  </Button>
</PermissionGuard>

// Edit button in table row
<PermissionGuard permission="ASSET:UPDATE">
  <IconButton onClick={() => handleEdit(asset.id)}>
    <EditIcon />
  </IconButton>
</PermissionGuard>

// Delete button in table row
<PermissionGuard permission="ASSET:DELETE">
  <IconButton onClick={() => handleDelete(asset.id)}>
    <DeleteIcon />
  </IconButton>
</PermissionGuard>
```

**Apply to**:
- Asset pages
- Site pages
- Invoice pages
- All operational pages

**Why**: Users can view data but only perform actions they're permitted.

#### 10.3 Update Form Pages

**What**: Hide form fields or entire forms based on permissions.

**Example**: Edit Asset Page

```typescript
// Entire form only for users with UPDATE permission
<PermissionGuard permission="ASSET:UPDATE">
  <AssetForm asset={asset} onSubmit={handleUpdate} />
</PermissionGuard>

<PermissionGuard permission="ASSET:UPDATE" fallback={
  <Alert>You don't have permission to edit assets</Alert>
}>
  <AssetForm asset={asset} />
</PermissionGuard>
```

**Why**: Prevent users from seeing forms they can't submit.

#### 10.4 Protect Core Master Pages
File: `client-app/src/routes/` (various Core Master routes)

**What**: Wrap entire Core Master pages with AdminGuard.

**Example**:
```typescript
// State page
export default function StatePage() {
  return (
    <AdminGuard fallback={<Navigate to="/unauthorized" />}>
      <StateTable />
      <CreateStateButton />
    </AdminGuard>
  )
}
```

**Apply to all Core Masters**:
- States, Cities, Locations, Warehouses, Datacenters
- Asset Types, Asset Categories, Movement Types
- Site Types, Site Categories
- Banks, Cost Categories, Cost Types
- Vendor Categories, Vendor Types, Person Types

**Why**: Non-admin users can't even see Core Master pages.

#### 10.5 Update Table Action Columns

**What**: Conditionally show action buttons in data tables.

**Example**: Asset Table

```typescript
const columns = [
  // ... other columns ...
  {
    id: 'actions',
    cell: ({ row }) => {
      const { can } = usePermission()
      
      return (
        <div className="flex gap-2">
          {can('ASSET:UPDATE') && (
            <IconButton onClick={() => handleEdit(row.original)}>
              <EditIcon />
            </IconButton>
          )}
          {can('ASSET:DELETE') && (
            <IconButton onClick={() => handleDelete(row.original.id)}>
              <DeleteIcon />
            </IconButton>
          )}
        </div>
      )
    }
  }
]
```

**Apply to**: All data tables in the application.

**Why**: Action columns adapt to user permissions.

### Deliverables
- ✅ Sidebar shows only accessible items
- ✅ Create buttons hidden without CREATE permission
- ✅ Edit buttons hidden without UPDATE permission
- ✅ Delete buttons hidden without DELETE permission
- ✅ Bulk upload hidden without BULK_UPLOAD permission
- ✅ Core Master pages only accessible to ADMIN
- ✅ Proper fallback messages for denied access

---

## Phase 11: Role Management UI

**Goal**: Build admin interface for creating and managing custom roles, assigning permissions, and assigning roles to users.

### Tasks

#### 11.1 Create Role List Page
File: `client-app/src/features/roles/index.tsx`

**What**: Page that shows all roles in a table.

**Features**:
- Table with columns: Role Name, Description, Color (badge), # Permissions, Actions
- Edit/Delete action buttons (ADMIN only)
- Create Role button at top (ADMIN only)
- Click row to view role details

**Why**: ADMIN needs to see all roles at a glance.

#### 11.2 Create Role Form
File: `client-app/src/features/roles/components/role-form.tsx`

**What**: Form for creating/editing roles.

**Fields**:
- Name (text input, required)
- Description (textarea)
- Color code (color picker, default random)
- Permissions (grouped checkboxes by category)

**Validation**:
- Name required and unique
- At least one permission selected

**Why**: ADMIN creates custom roles with specific permissions.

#### 11.3 Create Permission Selector
File: `client-app/src/features/roles/components/permission-selector.tsx`

**What**: UI for selecting permissions when creating/editing roles.

**Features**:
- Group permissions by category (Core Masters, Operations, Financial, etc.)
- Expand/collapse each category
- "Select All" checkbox for each category
- Individual permission checkboxes
- Search/filter permissions by name
- Show permission scope and action clearly

**Why**: Makes it easy to assign many permissions without overwhelming UI.

#### 11.4 Create User Role Assignment UI
File: `client-app/src/features/users/components/user-roles-dialog.tsx`

**What**: Dialog for assigning roles to users.

**Features**:
- Multi-select dropdown or checkbox list of all roles
- Show currently assigned roles with badge
- Add/remove roles
- Show effective permissions (all permissions from all roles)
- Preview permission changes before saving

**Why**: ADMIN needs to assign multiple roles to users.

#### 11.5 Add Routes

**Routes to add**:
- `/roles` - Role list (ADMIN only)
- `/roles/create` - Create new role (ADMIN only)
- `/roles/:id/edit` - Edit existing role (ADMIN only)

**Update existing**:
- `/users/:id` - Add section for managing user roles (ADMIN only)

### Deliverables
- ✅ Role list page showing all roles
- ✅ Create/edit role forms working
- ✅ Permission selector intuitive and grouped
- ✅ User role assignment dialog working
- ✅ Routes protected with AdminGuard

---

## Phase 12: Testing & Validation

**Goal**: Test the entire RBAC system to ensure it works correctly and securely.

### Tasks

#### 12.1 Backend Testing

**Test Scenarios**:
1. ✅ ADMIN user can access all endpoints
2. ✅ Custom role user can only access permitted endpoints
3. ✅ Multi-role user has union of all role permissions
4. ✅ Non-admin cannot access Core Masters (403)
5. ✅ Non-admin cannot create/edit/delete roles (403)
6. ✅ System ADMIN role cannot be deleted
7. ✅ User without permission gets 403 error
8. ✅ Permission checking works in PermissionService

**Create Test Users**:
- Admin user (ADMIN role)
- Asset manager (ASSET_MANAGER custom role)
- Site manager (SITE_MANAGER custom role)
- Multi-role user (both ASSET_MANAGER and SITE_MANAGER)
- No-role user (no roles assigned)

**Test Each Role**:
- Can access permitted endpoints
- Cannot access non-permitted endpoints
- Gets proper error messages

#### 12.2 Frontend Testing

**Test Scenarios**:
1. ✅ Sidebar only shows accessible menu items
2. ✅ Create button hidden without CREATE permission
3. ✅ Edit button hidden without UPDATE permission
4. ✅ Delete button hidden without DELETE permission
5. ✅ Bulk upload button hidden without BULK_UPLOAD permission
6. ✅ Core Master pages redirect non-admin users
7. ✅ Role management pages only show to ADMIN
8. ✅ Multi-role user sees union of all permitted actions

**Test with Different Users**:
- Login as each test user
- Verify UI shows/hides appropriate elements
- Try to access protected pages directly (should redirect)
- Check that API calls fail gracefully if somehow triggered

#### 12.3 Security Testing

**Check for Issues**:
- No permission bypass vulnerabilities
- Backend validation even if frontend checks pass
- Proper error messages (don't leak sensitive info)
- SQL injection protection in role/permission names
- XSS protection in role/permission descriptions

**Test Edge Cases**:
- User with no roles
- User with role that has no permissions
- Deleted role still assigned to user
- Inactive permissions/roles
- Custom permission with special characters

### Deliverables
- ✅ All test scenarios pass
- ✅ No security vulnerabilities found
- ✅ Backend and frontend in sync
- ✅ Edge cases handled properly

---

## Implementation Complete

Once all 12 phases are complete, your RBAC system will be fully functional:

- ✅ Only ADMIN can access Core Masters
- ✅ Custom roles can be created by ADMIN
- ✅ Users can have multiple roles with union of permissions
- ✅ Frontend hides UI elements based on permissions
- ✅ Backend enforces all permissions at API level
- ✅ All system permissions auto-seeded (6 basic actions per entity)
- ✅ Role management UI available for ADMIN

### Key Architecture Decisions

**Module Separation**:
- `auth-module`: Generic authentication library (entities, repositories, JWT, permission checking)
- `epsoneapi`: Business logic (permission registry, seeders, RBAC APIs, annotations)

**Reusability & Extensibility**:
- Permission Registry auto-generates 6 basic actions for ALL entities
- Adding new entity = just add scope name to registry, automatically gets CREATE/READ/UPDATE/DELETE/BULK_UPLOAD/EXPORT
- Custom actions (TRANSFER, ASSIGN, etc.) added manually as needed
- Single PermissionAspect handles all @RequirePermission annotations
- Single RoleSeeder handles ADMIN role creation
- Generic services use auth-module repositories

**No Manual Work Required**:
- No SQL migration scripts (entities handle schema)
- No manual permission insertion (seeder auto-creates from registry)
- No manual role creation (ADMIN auto-seeded)
- No code duplication (reusable aspects and services)

Proceed phase by phase, testing each phase before moving to the next.
