# Entity Metadata

## Entity Dependency Hierarchy

### Level 0: Independent Entities (No Dependencies)

| Entity | Table | Description |
|--------|-------|-------------|
| **State** | `state` | Geographic states - no dependencies |
| **Bank** | `bank` | Bank master data |
| **Activity** | `activity` | Activity types (installation, maintenance, etc.) |
| **VendorCategory** | `vendor_category` | Vendor category classifications |
| **PersonType** | `person_type` | Person role types |
| **PayeeType** | `payee_type` | Payee classification |
| **PaymentMethod** | `payment_method` | Payment methods (NEFT, RTGS, etc.) |
| **GenericStatusType** | `generic_status_type` | Generic status lookup |
| **OwnershipStatus** | `ownership_status` | Asset ownership status |
| **SiteType** | `site_type` | Site classifications |
| **SiteCategory** | `site_category` | Site categories |
| **AssetType** | `asset_type` | Asset type classifications (ATM, Power Backup, etc.) |
| **AssetMovementType** | `asset_movement_type` | Asset movement types (Installation, Relocation, etc.) |
| **CostCategory** | `cost_category` | Expenditure category types |

---

### Level 1: Single Dependency

| Entity | Table | Dependencies | Description |
|--------|-------|--------------|-------------|
| **City** | `city` | State | Cities belong to states |
| **VendorType** | `vendor_type` | VendorCategory | Vendor type classifications under categories |
| **PersonDetails** | `person_details` | PersonType | Person information with type |
| **ManagedProject** | `managed_project` | Bank | Bank-managed projects |
| **PayeeDetails** | `payee_details` | Bank | Payee banking details |
| **Activities** | `activities` | Activity | Specific activity instances |
| **CostType** | `cost_type` | CostCategory | Cost types under categories |
| **AssetCategory** | `asset_category` | AssetType | Asset categories under types |

---

### Level 2: Two Dependencies

| Entity | Table | Dependencies | Description |
|--------|-------|--------------|-------------|
| **Location** | `location` | City | Detailed locations within cities |
| **Vendor** | `vendor` | VendorType, PersonDetails | Vendor with type and details |
| **Landlord** | `landlord` | PersonDetails | Landlord entity |
| **CostItem** | `cost_item` | CostType | Specific cost items |

---

### Level 3: Three Dependencies

| Entity | Table | Dependencies | Description |
|--------|-------|--------------|-------------|
| **Warehouse** | `warehouse` | Location | Warehouse at location |
| **Datacenter** | `datacenter` | Location | Datacenter at location |
| **ActivityWork** | `activity_work` | Activities, Vendor, GenericStatusType | Activity work assignment |
| **SiteCodeGenerator** | `site_code_generator` | ManagedProject, SiteCategory, State | Site code generation logic |
| **AssetTagCodeGenerator** | `asset_tag_code_generator` | AssetCategory, Vendor, Bank | Asset tag generation logic |
| **Payee** | `payee` | PayeeType, PayeeDetails, Vendor, Landlord | Payee master linking vendors/landlords |

---

### Level 4: Four+ Dependencies

| Entity | Table | Dependencies | Description |
|--------|-------|--------------|-------------|
| **Site** | `site` | ManagedProject, SiteCategory, Location, SiteType, GenericStatusType, PersonDetails (5 contacts) | Main site entity |
| **Asset** | `asset` | AssetType, AssetCategory, Vendor, Bank, GenericStatusType, OwnershipStatus | Asset master |
| **ActivityWorkRemarks** | `activity_work_remarks` | ActivityWork | Work remarks/comments |
| **Invoice** | `invoice` | Payee, PaymentDetails | Invoice master |
| **Voucher** | `voucher` | Payee, PaymentDetails | Voucher master |
| **PaymentDetails** | `payment_details` | PaymentMethod, Bank | Payment transaction details |

---

### Level 5: Complex Junction/Transaction Entities

| Entity | Table | Dependencies | Description |
|--------|-------|--------------|-------------|
| **AssetsOnSite** | `assets_on_site` | Asset, Site, GenericStatusType, ActivityWork, AssetMovementTracker | Asset-to-site mapping |
| **AssetsOnWarehouse** | `assets_on_warehouse` | Asset, Warehouse, GenericStatusType, ActivityWork, AssetMovementTracker | Asset-to-warehouse mapping |
| **AssetsOnDatacenter** | `assets_on_datacenter` | Asset, Datacenter, GenericStatusType, ActivityWork, AssetMovementTracker | Asset-to-datacenter mapping |
| **AssetMovementTracker** | `asset_movement_tracker` | Asset, AssetMovementType, Location | Asset movement history |
| **ExpendituresInvoice** | `expenditures_invoice` | CostItem, Invoice, ManagedProject | Invoice expenditure tracking |
| **ExpendituresVoucher** | `expenditures_voucher` | CostItem, Voucher, ManagedProject | Voucher expenditure tracking |
| **SiteActivityWorkExpenditure** | `site_activity_work_expenditure` | Site, ActivityWork, ExpendituresInvoice | Site work expenditure linkage |
| **AssetExpenditureAndActivityWork** | `asset_expenditure_and_activity_work` | Asset, ExpendituresInvoice, ActivityWork | Asset procurement/expenditure tracking |

---

## Key Relationships Summary

### Location Hierarchy
```
State → City → Location → (Site/Warehouse/Datacenter)
```

### Cost/Expenditure Flow
```
CostCategory → CostType → CostItem → (ExpendituresInvoice/ExpendituresVoucher)
```

### Vendor Flow
```
VendorCategory → VendorType → Vendor (with PersonDetails)
```

### Payment Flow
```
PayeeType → Payee (Vendor/Landlord) → (Invoice/Voucher) → PaymentDetails
```

### Asset Management Flow
```
AssetType → AssetCategory → Asset → AssetsOn(Site/Warehouse/Datacenter)
```

### Activity/Work Flow
```
Activity → Activities → ActivityWork → Site/Asset Linking
```

### Site Management
```
Bank → ManagedProject → Site → Activities/Assets
```

---

## Critical Dependencies

**Before creating/updating dependent entities, ensure parent data exists:**

1. **Geographic**: State before City before Location
2. **Bank Projects**: Bank before ManagedProject before Site
3. **Vendors**: VendorCategory before VendorType, PersonType before PersonDetails, then both before Vendor
4. **Assets**: AssetType → AssetCategory → Asset
5. **Costs**: CostCategory → CostType → CostItem
6. **Work Orders**: Activity → Activities → ActivityWork
7. **Payments**: PayeeType → PayeeDetails → Payee → Invoice/Voucher
