# RBAC (Role-Based Access Control) Design Document

## Table of Contents
1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Core Concepts](#core-concepts)
4. [Data Model](#data-model)
5. [Permission System](#permission-system)
6. [Role Management](#role-management)
7. [Security Considerations](#security-considerations)
8. [API Endpoints](#api-endpoints)

---

## Overview

### Goals
- **Flexible**: Support both standard and custom permissions
- **Extensible**: Easy to add new resources and permissions
- **Scalable**: Handle complex permission hierarchies
- **Developer-Friendly**: Simple to implement and maintain
- **Secure**: Protection against unauthorized access at multiple layers

### Key Features
- Admin-managed roles with dynamic creation
- Code-seeded base permissions (CREATE, READ, UPDATE, DELETE, BULK_UPLOAD, EXPORT)
- Resource-scoped permissions (e.g., `ASSET:CREATE`, `SITE:READ`)
- Custom permission support via configuration
- Multi-level permission checking (API, Service, UI)
- Users can have multiple roles simultaneously
- Core Masters accessible only by ADMIN role

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │  Frontend   │  │   API Layer  │  │  Service Layer   │   │
│  │   Guards    │  │ @PreAuthorize│  │  Permission      │   │
│  │             │  │  Annotations │  │   Checks         │   │
│  └─────────────┘  └──────────────┘  └──────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    RBAC Core System                          │
│  ┌──────────────────────────────────────────────────────┐   │
│  │            Permission Resolution Engine               │   │
│  │  - User → Roles → Permissions                        │   │
│  │  - Scope Resolution                                   │   │
│  │  - Permission Composition                            │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Data Model                              │
│  ┌──────┐      ┌──────┐      ┌────────────┐                │
│  │ User │◄────►│ Role │◄────►│ Permission │                │
│  └──────┘      └──────┘      └────────────┘                │
└─────────────────────────────────────────────────────────────┘
```

---

## Core Concepts

### 1. **Permissions**
Atomic units of access control that represent a specific action on a resource.

**Format**: `[SCOPE:]ACTION`

Examples:
- `CREATE` - Global create permission
- `ASSET:CREATE` - Create assets
- `SITE:READ` - Read sites
- `INVOICE:DELETE` - Delete invoices
- `BULK_UPLOAD` - Global bulk upload permission
- `ASSET:BULK_UPLOAD` - Bulk upload for assets only

### 2. **Roles**
Named collections of permissions that can be assigned to users.

**Types**:
- **System Roles**: Pre-seeded (ADMIN only)
- **Custom Roles**: Admin-created (e.g., SITE_MANAGER, ASSET_COORDINATOR, FINANCE_MANAGER)

**Important**: 
- Only ADMIN role is pre-seeded with ALL permissions
- ADMIN has complete system access including Core Masters
- Users can have multiple roles assigned simultaneously
- Custom roles can be created by ADMIN users

### 3. **Scopes**
Resource boundaries for permissions, mapped to your entity model.

**Core Master Scopes** (ADMIN only):
- `STATE` - State master
- `CITY` - City master
- `LOCATION` - Location master
- `WAREHOUSE` - Warehouse master
- `DATACENTER` - Datacenter master
- `ASSET_TYPE` - Asset type configuration
- `ASSET_CATEGORY` - Asset category configuration
- `MOVEMENT_TYPE` - Asset movement types
- `ASSET_TAG_GENERATOR` - Asset tag generation
- `SITE_TYPE` - Site type configuration
- `SITE_CATEGORY` - Site category configuration
- `SITE_CODE_GENERATOR` - Site code generation
- `ACTIVITY` - Activity master
- `BANK` - Bank master
- `COST_CATEGORY` - Cost category master
- `COST_TYPE` - Cost type master
- `COST_ITEM` - Cost item master
- `PAYMENT_METHOD` - Payment method master
- `PAYEE_TYPE` - Payee type master
- `VENDOR_CATEGORY` - Vendor category master
- `VENDOR_TYPE` - Vendor type master
- `PERSON_TYPE` - Person type master
- `STATUS_TYPE` - Generic status types

**Operational Scopes**:
- `ASSET` - Asset management
- `SITE` - Site management
- `ACTIVITY_LIST` - Activities list
- `ACTIVITY_WORK` - Activity work management
- `MANAGED_PROJECT` - Project management
- `INVOICE` - Invoice management
- `VOUCHER` - Voucher management
- `EXPENDITURE` - Expenditure management (general)
- `EXPENDITURE_INVOICE` - Invoice-specific expenditures
- `EXPENDITURE_VOUCHER` - Voucher-specific expenditures
- `ASSET_EXPENDITURE` - Asset expenditure and activity work linkage
- `SITE_ACTIVITY_WORK_EXPENDITURE` - Site activity work expenditures
- `PAYMENT` - Payment details
- `PAYEE_DETAILS` - Payee details
- `PAYEE` - Payee management
- `VENDOR` - Vendor management
- `LANDLORD` - Landlord management
- `PERSON_DETAILS` - Person details
- `ASSETS_ON_SITE` - Assets placed on sites
- `ASSETS_ON_WAREHOUSE` - Assets placed on warehouses
- `ASSETS_ON_DATACENTER` - Assets placed on datacenters

**System Scopes**:
- `USER` - User management
- `ROLE` - Role management
- `GLOBAL` - System-wide permissions

### 4. **Actions**
Standard CRUD operations plus custom actions.

**Base Actions**:
- `CREATE` - Create new records
- `READ` - View/list records
- `UPDATE` - Modify existing records
- `DELETE` - Remove records
- `BULK_UPLOAD` - Bulk import via Excel/CSV
- `EXPORT` - Export data
- `ASSIGN` - Assign resources
- `TRANSFER` - Transfer assets/resources

---

## Data Model

### Entity Structure

**Permission Entity**:
- `id` - Unique identifier
- `name` - Permission name (e.g., "ASSET:CREATE", "BULK_UPLOAD")
- `description` - Human-readable description
- `scope` - Resource scope (ASSET, SITE, GLOBAL, etc.)
- `action` - Action type (CREATE, READ, UPDATE, DELETE, etc.)
- `isSystemPermission` - Pre-seeded vs custom permission
- `isActive` - Active status
- `category` - UI grouping category (e.g., "Core Masters", "Operations")

**Role Entity**:
- `id` - Unique identifier
- `name` - Role name (e.g., "ADMIN", "SITE_MANAGER")
- `description` - Human-readable description
- `isSystemRole` - Pre-seeded (ADMIN) vs admin-created
- `isActive` - Active status
- `permissions` - Many-to-many relationship with Permission

**Note**: Color coding for UI display is managed on frontend, not stored in database.

**User Entity**:
- `id` - Unique identifier
- `email` - User email
- `password` - Encrypted password
- `name` - Full name
- `isActive` - Active status
- `roles` - Many-to-many relationship with Role (supports multiple roles)

### Database Schema

**Permissions Table**:
- Primary Key: `id`
- Unique Key: `name`
- Indexes: `scope`, `action`, `is_active`
- Relationships: Many-to-many with roles

**Roles Table**:
- Primary Key: `id`
- Unique Key: `name`
- Indexes: `is_active`
- Relationships: Many-to-many with permissions and users

**Role-Permission Junction Table**:
- Composite Primary Key: `(role_id, permission_id)`
- Foreign Keys: `role_id`, `permission_id`

**User-Role Junction Table**:
- Composite Primary Key: `(user_id, role_id)`
- Foreign Keys: `user_id`, `role_id`
- Supports multiple roles per user

---

## Permission System

### Permission Auto-Seeding Strategy

**Basic Actions** (auto-generated for ALL entity scopes):
1. `CREATE` - Create new records
2. `READ` - View/list records
3. `UPDATE` - Modify existing records
4. `DELETE` - Remove records
5. `BULK_UPLOAD` - Bulk import via Excel/CSV
6. `EXPORT` - Export data to Excel/CSV

These 6 basic permissions are automatically created for every entity scope when the application starts. This ensures consistency and reduces manual permission management.

**Custom Actions** (added manually when needed):
- Custom actions like `TRANSFER`, `ASSIGN`, `APPROVE`, etc. can be added to specific scopes as requirements evolve
- Example: `ASSET:TRANSFER` for transferring assets between locations
- Example: `ACTIVITY_WORK:ASSIGN` for assigning work to personnel

**Required Custom Permissions** (based on current features):
- `ASSET:PLACE` - Place asset on site/warehouse/datacenter (distinct from CREATE)
- `ASSET:TRANSFER` - Transfer asset between locations
- `ASSET:VIEW_MOVEMENT_HISTORY` - View asset movement/placement history
- `ASSET:VIEW_CURRENT_LOCATION` - View current asset location
- `ACTIVITY_WORK:ASSIGN` - Assign activity work to personnel
- `EXPENDITURE_INVOICE:CREATE` - Create expenditure linked to invoice
- `EXPENDITURE_INVOICE:READ` - View invoice expenditures
- `EXPENDITURE_INVOICE:UPDATE` - Update invoice expenditures
- `EXPENDITURE_INVOICE:DELETE` - Delete invoice expenditures
- `EXPENDITURE_VOUCHER:CREATE` - Create expenditure linked to voucher
- `EXPENDITURE_VOUCHER:READ` - View voucher expenditures
- `EXPENDITURE_VOUCHER:UPDATE` - Update voucher expenditures
- `EXPENDITURE_VOUCHER:DELETE` - Delete voucher expenditures
- `SITE_ACTIVITY_WORK_EXPENDITURE:CREATE` - Create site activity work expenditure
- `SITE_ACTIVITY_WORK_EXPENDITURE:READ` - View site activity work expenditures
- `SITE_ACTIVITY_WORK_EXPENDITURE:UPDATE` - Update site activity work expenditures
- `SITE_ACTIVITY_WORK_EXPENDITURE:DELETE` - Delete site activity work expenditures
- `ASSET_EXPENDITURE:CREATE` - Create asset expenditure and link to activity work
- `ASSET_EXPENDITURE:READ` - View asset expenditures
- `ASSET_EXPENDITURE:UPDATE` - Update asset expenditures
- `ASSET_EXPENDITURE:DELETE` - Delete asset expenditures

### Permission Categories

**Core Masters** (ADMIN only):
- State, City, Location, Warehouse, Datacenter
- Asset Types, Asset Categories, Movement Types, Asset Tag Generators
- Site Types, Site Categories, Site Code Generators
- Activity Master
- Bank, Cost Category, Cost Type, Cost Item, Payment Method, Payee Type
- Vendor Category, Vendor Type, Person Type, Status Types

**Operations**:
- Asset Management
- Site Management
- Activity Management (Activities, Activity Works)
- Project Management

**Financial**:
- Invoices, Vouchers
- Expenditures
- Payment Management

**People & Organizations**:
- Vendors
- Landlords
- Person Details

**System Administration**:
- User Management
- Role Management

### Permission Structure

Format: `SCOPE:ACTION`

**Auto-seeded Examples** (6 basic actions for each scope):
- Core Masters: `STATE:CREATE`, `STATE:READ`, `STATE:UPDATE`, `STATE:DELETE`, `STATE:BULK_UPLOAD`, `STATE:EXPORT`
- Operations: `ASSET:CREATE`, `ASSET:READ`, `ASSET:UPDATE`, `ASSET:DELETE`, `ASSET:BULK_UPLOAD`, `ASSET:EXPORT`
- Financial: `INVOICE:CREATE`, `INVOICE:READ`, `INVOICE:UPDATE`, `INVOICE:DELETE`, `INVOICE:BULK_UPLOAD`, `INVOICE:EXPORT`
- System: `USER:CREATE`, `USER:READ`, `USER:UPDATE`, `USER:DELETE`, `USER:BULK_UPLOAD`, `USER:EXPORT`

**Custom Action Examples** (added as needed):
- `ASSET:TRANSFER` - Transfer asset between locations
- `ACTIVITY_WORK:ASSIGN` - Assign work to personnel
- `INVOICE:APPROVE` - Approve invoice (if workflow needed in future)

---

## Role Management

### Pre-seeded Role

**ADMIN Role**:
- Only system role that is pre-seeded
- Has ALL permissions (complete system access)
- Can access all Core Masters
- Can create and manage custom roles
- Can assign roles to users
- Cannot be deleted or have isSystemRole changed

### Custom Roles

Custom roles are created by ADMIN users to match organizational needs:

**Example Custom Roles**:
- **ASSET_MANAGER**: Asset, Warehouse, Datacenter management
- **SITE_MANAGER**: Site and Location operations
- **FINANCE_MANAGER**: Invoice, Voucher, Payment management
- **VENDOR_COORDINATOR**: Vendor and Landlord management
- **ACTIVITY_COORDINATOR**: Activity and Activity Work management
- **READ_ONLY_USER**: View-only access to operational data

### Multi-Role Support

- Users can be assigned multiple roles simultaneously
- Permissions are combined from all assigned roles
- User has access if ANY role provides the permission
- Example: User with both ASSET_MANAGER and SITE_MANAGER roles can manage both assets and sites

---

## Security Considerations

### 1. Defense in Depth
- **Layer 1**: Frontend UI guards (user experience)
- **Layer 2**: API endpoint annotations (authorization)
- **Layer 3**: Service layer checks (business logic)
- **Layer 4**: Database constraints (data integrity)

### 2. Core Masters Protection
- All Core Master endpoints require ADMIN role
- Core Masters include: States, Cities, Banks, Asset Types, Site Types, Cost Categories, Vendor Categories, etc.
- Non-admin users cannot access or modify Core Masters
- System configuration is protected from unauthorized changes

### 3. System Role Protection
- ADMIN role cannot be deleted
- ADMIN role's `isSystemRole` flag cannot be changed
- Only ADMIN can create new roles
- Only ADMIN can assign/remove roles from users

### 4. Multi-Role Permission Resolution
- User permissions are union of all assigned roles
- If ANY role grants permission, user has access
- No permission conflicts - additive model only

### 5. Audit Logging
- Track all permission checks
- Log role assignments/removals
- Monitor Core Master access
- Record custom role creation/modification

---

## API Endpoints

### Authentication
```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/logout
GET    /api/auth/me
POST   /api/auth/forgot-password
POST   /api/auth/reset-password
```

### Role Management (ADMIN only)
```
GET    /api/roles
GET    /api/roles/{id}
POST   /api/roles
PUT    /api/roles/{id}
DELETE /api/roles/{id}
PUT    /api/roles/{id}/permissions
```

### Permission Management
```
GET    /api/permissions
GET    /api/permissions/grouped
GET    /api/permissions/my
POST   /api/permissions (ADMIN only - for custom permissions)
```

### User Management (ADMIN only)
```
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
POST   /api/users/{userId}/roles/{roleId}
DELETE /api/users/{userId}/roles/{roleId}
GET    /api/users/{userId}/permissions
```

### Core Masters (ADMIN only)
```
# Location Setup
GET    /api/states
POST   /api/states
PUT    /api/states/{id}
DELETE /api/states/{id}

GET    /api/cities
POST   /api/cities
PUT    /api/cities/{id}
DELETE /api/cities/{id}

GET    /api/locations
POST   /api/locations
PUT    /api/locations/{id}
DELETE /api/locations/{id}

GET    /api/warehouses
POST   /api/warehouses
PUT    /api/warehouses/{id}
DELETE /api/warehouses/{id}

GET    /api/datacenters
POST   /api/datacenters
PUT    /api/datacenters/{id}
DELETE /api/datacenters/{id}

# Asset Configuration
GET    /api/asset-types
POST   /api/asset-types
PUT    /api/asset-types/{id}
DELETE /api/asset-types/{id}

GET    /api/asset-categories
POST   /api/asset-categories
PUT    /api/asset-categories/{id}
DELETE /api/asset-categories/{id}

GET    /api/movement-types
POST   /api/movement-types
PUT    /api/movement-types/{id}
DELETE /api/movement-types/{id}

GET    /api/asset-tag-generators
POST   /api/asset-tag-generators
PUT    /api/asset-tag-generators/{id}
DELETE /api/asset-tag-generators/{id}

# Site Configuration
GET    /api/site-types
POST   /api/site-types
PUT    /api/site-types/{id}
DELETE /api/site-types/{id}

GET    /api/site-categories
POST   /api/site-categories
PUT    /api/site-categories/{id}
DELETE /api/site-categories/{id}

GET    /api/site-code-generators
POST   /api/site-code-generators
PUT    /api/site-code-generators/{id}
DELETE /api/site-code-generators/{id}

# Activity Configuration
GET    /api/activities
POST   /api/activities
PUT    /api/activities/{id}
DELETE /api/activities/{id}

# Financial Setup
GET    /api/banks
POST   /api/banks
PUT    /api/banks/{id}
DELETE /api/banks/{id}

GET    /api/cost-categories
POST   /api/cost-categories
PUT    /api/cost-categories/{id}
DELETE /api/cost-categories/{id}

GET    /api/cost-types
POST   /api/cost-types
PUT    /api/cost-types/{id}
DELETE /api/cost-types/{id}

GET    /api/cost-items
POST   /api/cost-items
PUT    /api/cost-items/{id}
DELETE /api/cost-items/{id}

GET    /api/payment-methods
POST   /api/payment-methods
PUT    /api/payment-methods/{id}
DELETE /api/payment-methods/{id}

GET    /api/payee-types
POST   /api/payee-types
PUT    /api/payee-types/{id}
DELETE /api/payee-types/{id}

# Classifications
GET    /api/vendor-categories
POST   /api/vendor-categories
PUT    /api/vendor-categories/{id}
DELETE /api/vendor-categories/{id}

GET    /api/vendor-types
POST   /api/vendor-types
PUT    /api/vendor-types/{id}
DELETE /api/vendor-types/{id}

GET    /api/person-types
POST   /api/person-types
PUT    /api/person-types/{id}
DELETE /api/person-types/{id}

GET    /api/generic-status-types
POST   /api/generic-status-types
PUT    /api/generic-status-types/{id}
DELETE /api/generic-status-types/{id}
```

### Operations (Permission-based)
```
# Asset Management
GET    /api/assets
POST   /api/assets
PUT    /api/assets/{id}
DELETE /api/assets/{id}
POST   /api/assets/bulk-upload
POST   /api/assets/{id}/transfer

# Site Management
GET    /api/sites
POST   /api/sites
PUT    /api/sites/{id}
DELETE /api/sites/{id}
POST   /api/sites/bulk-upload

# Activity Management
GET    /api/activities-list
POST   /api/activities-list
PUT    /api/activities-list/{id}
DELETE /api/activities-list/{id}

GET    /api/activity-works
POST   /api/activity-works
PUT    /api/activity-works/{id}
DELETE /api/activity-works/{id}
POST   /api/activity-works/{id}/assign

# Project Management
GET    /api/managed-projects
POST   /api/managed-projects
PUT    /api/managed-projects/{id}
DELETE /api/managed-projects/{id}
```

### Financial (Permission-based)
```
GET    /api/invoices
POST   /api/invoices
PUT    /api/invoices/{id}
DELETE /api/invoices/{id}

GET    /api/vouchers
POST   /api/vouchers
PUT    /api/vouchers/{id}
DELETE /api/vouchers/{id}

GET    /api/expenditures/invoices
POST   /api/expenditures/invoices
PUT    /api/expenditures/invoices/{id}
DELETE /api/expenditures/invoices/{id}

GET    /api/expenditures/vouchers
POST   /api/expenditures/vouchers
PUT    /api/expenditures/vouchers/{id}
DELETE /api/expenditures/vouchers/{id}

GET    /api/payment-details
POST   /api/payment-details
PUT    /api/payment-details/{id}
DELETE /api/payment-details/{id}

GET    /api/payee-details
POST   /api/payee-details
PUT    /api/payee-details/{id}
DELETE /api/payee-details/{id}

GET    /api/payees
POST   /api/payees
PUT    /api/payees/{id}
DELETE /api/payees/{id}
```

### People & Organizations (Permission-based)
```
GET    /api/vendors
POST   /api/vendors
PUT    /api/vendors/{id}
DELETE /api/vendors/{id}

GET    /api/landlords
POST   /api/landlords
PUT    /api/landlords/{id}
DELETE /api/landlords/{id}

GET    /api/person-details
POST   /api/person-details
PUT    /api/person-details/{id}
DELETE /api/person-details/{id}
```

---

## Architecture & Module Organization

### Module Separation

The RBAC system is split across two modules with clear separation of concerns:

#### auth-module (Authentication & Authorization Library)
**Purpose**: Reusable authentication library with no business logic

**Responsibilities**:
- User, Role, Permission entities (JPA entities only)
- Repository interfaces (`UserRepository`, `RoleRepository`, `PermissionRepository`)
- JWT token generation and validation
- Basic auth services (login, logout, token refresh)
- Permission checking utilities (`PermissionService` - generic permission validation)
- Auth-related DTOs and exceptions
- Security configuration

**Key Principle**: Contains NO business knowledge, NO entity scopes, NO permission lists. Pure authentication/authorization framework.

#### epsoneapi (Business Logic & RBAC Implementation)
**Purpose**: Application-specific RBAC implementation

**Responsibilities**:
- Permission registry (centralized list of all entity scopes and actions)
- Permission seeder (auto-creates 6 basic permissions for all scopes on startup)
- Role seeder (creates ADMIN role with all permissions on startup)
- Role management APIs (CRUD for roles)
- Permission assignment APIs (assign permissions to roles)
- User role assignment APIs (assign roles to users)
- Controller annotations (`@RequirePermission`, `@RequireAdmin`)
- AOP aspects for permission checking
- All business logic and domain knowledge

**Why This Separation?**
1. **Reusability**: `auth-module` can be used in other projects without modification
2. **Clear Boundaries**: Authentication logic separate from business logic
3. **Maintainability**: Changes to business permissions don't affect auth library
4. **Testability**: Each module can be tested independently

### Reusable Components (in epsoneapi)

To avoid code duplication and ensure consistency:

#### 1. Permission Registry (`PermissionRegistry.java`)
**Purpose**: Single source of truth for all entity scopes and their permissions

**Features**:
- Defines all entity scopes (ASSET, SITE, INVOICE, etc.)
- Auto-generates 6 basic actions per scope (CREATE, READ, UPDATE, DELETE, BULK_UPLOAD, EXPORT)
- Allows registration of custom actions per scope
- Used by permission seeder to create all permissions

**Example**:
```java
public class PermissionRegistry {
    private static final String[] BASIC_ACTIONS = {
        "CREATE", "READ", "UPDATE", "DELETE", "BULK_UPLOAD", "EXPORT"
    };
    
    public List<PermissionDefinition> getAllPermissions() {
        // Auto-generates basic 6 for each scope
        // Returns full list of permissions to seed
    }
}
```

#### 2. Permission Seeder (`PermissionSeeder.java`)
**Purpose**: Automatically creates all permissions on application startup

**Features**:
- Reads from PermissionRegistry
- Creates permissions if they don't exist
- Idempotent (safe to run multiple times)
- Runs on every application startup

#### 3. Role Seeder (`RoleSeeder.java`)
**Purpose**: Creates ADMIN role with all permissions

**Features**:
- Creates ADMIN role if not exists
- Assigns ALL permissions from PermissionSeeder
- Marks as system role (isSystemRole = true)
- Runs after PermissionSeeder

#### 4. Permission Aspect (`PermissionAspect.java`)
**Purpose**: AOP interceptor for `@RequirePermission` annotation

**Features**:
- Intercepts controller methods with permission annotations
- Checks user permissions via PermissionService from auth-module
- Returns 403 if permission denied
- Reusable across all controllers
- Write once, use everywhere

**Example**:
```java
@Aspect
public class PermissionAspect {
    @Before("@annotation(requirePermission)")
    public void checkPermission(RequirePermission requirePermission) {
        // Check permission using auth-module's PermissionService
    }
}
```

#### 5. Base Controller with Permission Helpers (`BaseController.java`)
**Purpose**: Common permission checking methods for all controllers

**Features**:
- `requirePermission(String permission)` - Throws if no permission
- `hasPermission(String permission)` - Returns boolean
- `requireAdmin()` - Throws if not admin
- Consistent error responses
- Inherited by all controllers

**Benefits of Reusable Components**:
- ✅ Write permission logic once
- ✅ Consistent behavior across all entities
- ✅ Easy to add new entities (just add scope to registry)
- ✅ Centralized maintenance
- ✅ Reduced code duplication
- ✅ Less chance of bugs or inconsistencies

---

## Summary

This RBAC design provides:

✅ **Simplicity**: Single ADMIN role with all permissions
✅ **Flexibility**: Admin creates custom roles as needed
✅ **Security**: Core Masters protected by ADMIN-only access
✅ **Scalability**: Multi-role support for complex permissions
✅ **Control**: Fine-grained permission system
✅ **Extensibility**: Easy to add new scopes and actions
✅ **Maintainability**: Code-based permission seeding

The system allows ADMIN to configure the exact permissions needed for each organizational role while keeping Core Masters secure.
