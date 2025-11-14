# Data Collection Template Order Guide

## 📋 Overview
This document outlines the **exact order** in which to send bulk upload templates to your team for data collection. The order is based on database foreign key dependencies - you **must** collect data for parent tables before child tables.

**Important:** Follow this order strictly to avoid data import failures due to missing foreign key references.

---

## 🎯 Quick Summary: Phase-wise Data Collection

| Phase | Focus Area | Number of Templates | Timeline Suggestion |
|-------|-----------|---------------------|---------------------|
| **Phase 1** | Foundation Masters | 11 templates | Week 1 |
| **Phase 2** | Location & Financial Setup | 7 templates | Week 2 |
| **Phase 3** | Vendors & Infrastructure | 7 templates | Week 3 |
| **Phase 4** | Assets & Sites | 3 templates | Week 4 |
| **Phase 5** | Operations & Activities | 3 templates | Week 5 |
| **Phase 6** | Financial Transactions | 4 templates | Week 6 |
| **Phase 7** | Asset Movements | 5 templates | Week 7 |
| **Phase 8** | Complex Relationships | 3 templates | Week 8 |

**Total: 43 Templates**

---

## 📊 Detailed Phase-by-Phase Collection Order

---

### 🟢 **PHASE 1: Foundation Masters** (NO DEPENDENCIES)
*Collect these FIRST - they have no dependencies on other tables*

#### **Week 1, Day 1-2:**
1. **States** ⭐ HIGHEST PRIORITY
   - Template: `State_Upload_Template.xlsx`
   - Columns: State Code, State Code Alt, State Name
   - Why First: Required by Cities, which is required by almost everything
   - Expected Records: 30-40 (all Indian states)

2. **Banks** ⭐ HIGHEST PRIORITY
   - Template: `Bank_Upload_Template.xlsx`
   - Columns: Bank Name, EPS Bank Code, RBI Bank Code, Bank Code Alt
   - Why Early: Required by Projects, Payees, and Payment Details
   - Expected Records: 50-100 (major banks)

3. **Generic Status Types** ⭐ HIGHEST PRIORITY
   - Template: `StatusTypes_Upload_Template.xlsx`
   - Columns: Status Code, Status Name, Description
   - Why Early: Required by Sites, Assets, Activity Works, and many others
   - Expected Records: 10-20 (Pending, In Progress, Completed, etc.)

#### **Week 1, Day 3-4:**
4. **Asset Types**
   - Template: `AssetType_Upload_Template.xlsx`
   - Columns: Type Code, Type Name, Description
   - Expected Records: 5-10 (Hardware, Software, etc.)

5. **Asset Movement Types**
   - Template: `MovementTypes_Upload_Template.xlsx`
   - Columns: Movement Type, Description
   - Expected Records: 10-15 (Factory to Warehouse, Warehouse to Site, etc.)

6. **Site Types**
   - Template: `SiteType_Upload_Template.xlsx`
   - Columns: Type Name, Description
   - Expected Records: 5-10 (On-Site, Off-Site, etc.)

7. **Site Categories**
   - Template: `SiteCategory_Upload_Template.xlsx`
   - Columns: Category Code, Category Name, Description
   - Expected Records: 5-10 (Metro, Urban, Rural, etc.)

#### **Week 1, Day 5:**
8. **Activity Master** (Base Activities)
   - Template: `Activity_Upload_Template.xlsx`
   - Columns: Activity Name, Activity Description
   - Expected Records: 20-50 (Site Survey, Installation, Maintenance, etc.)

9. **Cost Categories**
   - Template: `CostCategories_Upload_Template.xlsx`
   - Columns: Category Name, Category Description
   - Expected Records: 5-10 (CAPEX, OPEX, etc.)

10. **Payment Methods**
    - Template: `PaymentMethod_Upload_Template.xlsx`
    - Columns: Method Name, Description
    - Expected Records: 10-15 (NEFT, RTGS, IMPS, Cheque, etc.)

11. **Payee Types**
    - Template: `PayeeType_Upload_Template.xlsx`
    - Columns: Payee Type, Payee Category
    - Expected Records: 5-10 (Vendor, Landlord, Service Provider, etc.)

12. **Person Types**
    - Template: `PersonType_Upload_Template.xlsx`
    - Columns: Type Name, Description
    - Expected Records: 5-10 (Employee, Vendor Contact, Landlord, etc.)

13. **Vendor Categories**
    - Template: `VendorCategories_Upload_Template.xlsx`
    - Columns: Category Name, Description
    - Expected Records: 10-20 (Hardware Supplier, Service Provider, etc.)

---

### 🟡 **PHASE 2: Location & Financial Setup** (DEPENDS ON PHASE 1)
*Collect these AFTER Phase 1 is complete*

#### **Week 2, Day 1-2:**
14. **Cities** ⭐ HIGH PRIORITY
    - Template: `City_Upload_Template.xlsx`
    - Columns: City Code, City Name, **State Name** (from Phase 1)
    - **Dependency:** Requires States data
    - Why Important: Required by Locations, which is required by many entities
    - Expected Records: 200-500 (major cities)

15. **Managed Projects** ⭐ HIGH PRIORITY
    - Template: `ManagedProject_Upload_Template.xlsx`
    - Columns: Project Code, Project Name, Project Type, **Bank Name** (from Phase 1)
    - **Dependency:** Requires Banks data
    - Why Important: Required by Sites, Expenditures, and Site Code Generators
    - Expected Records: 10-50 (active projects)

#### **Week 2, Day 3-4:**
16. **Asset Categories**
    - Template: `AssetCategory_Upload_Template.xlsx`
    - Columns: Category Code, Category Name, Description, **Asset Type Name** (from Phase 1)
    - **Dependency:** Requires Asset Types
    - Expected Records: 20-50 (ATM, UPS, Router, etc.)

17. **Cost Types**
    - Template: `CostTypes_Upload_Template.xlsx`
    - Columns: Type Name, Type Description, **Cost Category Name** (from Phase 1)
    - **Dependency:** Requires Cost Categories
    - Expected Records: 20-50 (Hardware Purchase, Site Rent, AMC, etc.)

18. **Activities** (Detailed)
    - Template: `Activities_Upload_Template.xlsx`
    - Columns: Activity Name, Activity Category, **Activity Master Name** (from Phase 1)
    - **Dependency:** Requires Activity Master
    - Expected Records: 50-100

19. **Vendor Types**
    - Template: `VendorTypes_Upload_Template.xlsx`
    - Columns: Type Name, **Vendor Category Name** (from Phase 1)
    - **Dependency:** Requires Vendor Categories
    - Expected Records: 20-50 (ATM OEM, Network Provider, etc.)

#### **Week 2, Day 5:**
20. **Person Details** ⭐ IMPORTANT
    - Template: `PersonDetails_Upload_Template.xlsx`
    - Columns: First Name, Last Name, Contact Number, **Person Type Name** (from Phase 1)
    - **Dependency:** Requires Person Types
    - Why Important: Required for Vendors, Landlords, and Site contacts
    - Expected Records: 100-500 (all contacts)

---

### 🟠 **PHASE 3: Vendors, Infrastructure & Payments** (DEPENDS ON PHASE 2)
*Collect these AFTER Phase 2 is complete*

#### **Week 3, Day 1-2:**
21. **Locations** ⭐ HIGH PRIORITY
    - Template: `Locations_Upload_Template.xlsx`
    - Columns: Location Name, Address, District, Pincode, **City Name** (from Phase 2)
    - **Dependency:** Requires Cities
    - Why Important: Required by Warehouses, Datacenters, and Sites
    - Expected Records: 500-2000 (all site/warehouse locations)

22. **Cost Items**
    - Template: `CostItems_Upload_Template.xlsx`
    - Columns: Cost Item For, **Cost Type Name** (from Phase 2)
    - **Dependency:** Requires Cost Types
    - Expected Records: 100-500

23. **Payee Details** ⭐ IMPORTANT
    - Template: `PayeeDetails_Upload_Template.xlsx`
    - Columns: Payee Name, Account Number, IFSC Code, PAN Number, **Bank Name** (from Phase 1)
    - **Dependency:** Requires Banks
    - Why Important: Required for Payees (Vendors and Landlords)
    - Expected Records: 100-500 (all payable entities)

#### **Week 3, Day 3-4:**
24. **Vendors** ⭐ HIGH PRIORITY
    - Template: `Vendors_Upload_Template.xlsx`
    - Columns: Vendor Code Alt, **Person Details Name** (from Phase 2), **Vendor Type Name** (from Phase 2)
    - **Dependencies:** Requires Person Details AND Vendor Types
    - Why Important: Required by Assets, Activity Works, Payees
    - Expected Records: 50-200 (all vendors)

25. **Landlords**
    - Template: `Landlords_Upload_Template.xlsx`
    - Columns: Rent Share Percentage, **Person Details Name** (from Phase 2)
    - **Dependency:** Requires Person Details
    - Expected Records: 100-500 (property owners)

26. **Warehouses**
    - Template: `Warehouses_Upload_Template.xlsx`
    - Columns: Warehouse Code, Warehouse Name, Warehouse Type, **Location Name** (from Phase 3)
    - **Dependency:** Requires Locations
    - Expected Records: 10-50

27. **Datacenters**
    - Template: `Datacenters_Upload_Template.xlsx`
    - Columns: Datacenter Code, Datacenter Name, Datacenter Type, **Location Name** (from Phase 3)
    - **Dependency:** Requires Locations
    - Expected Records: 5-20

---

### 🔵 **PHASE 4: Assets & Sites Setup** (DEPENDS ON PHASE 3)
*Collect these AFTER Phase 3 is complete*

#### **Week 4, Day 1-3:**
28. **Payees** ⭐ IMPORTANT
    - Template: `Payees_Upload_Template.xlsx`
    - Columns: **Payee Details Name** (Phase 3), **Payee Type** (Phase 1), **Vendor Name** (Phase 3) OR **Landlord Name** (Phase 3)
    - **Dependencies:** Requires Payee Details, Payee Types, AND either Vendors OR Landlords
    - Why Important: Required for Invoices and Vouchers
    - Expected Records: 150-700 (all payable entities)

29. **Site Code Generators**
    - Template: `SiteCodeGenerator_Upload_Template.xlsx`
    - Columns: Max Seq Digit, Running Seq, **Project Name** (Phase 2), **State Name** (Phase 1)
    - **Dependencies:** Requires Managed Projects AND States
    - Expected Records: 50-200 (one per project-state combination)

30. **Sites** ⭐ CRITICAL - COMPLEX DEPENDENCIES
    - Template: `Sites_Upload_Template.xlsx`
    - Columns: Site Code, ATM IP, **Location Name** (Phase 3), **Project Name** (Phase 2), **Site Category** (Phase 1), **Site Status** (Phase 1), **Site Type** (Phase 1), **Bank Person Contact** (Phase 2), **Regional Manager Contact** (Phase 2)
    - **Dependencies:** Requires Locations, Managed Projects, Site Categories, Generic Status Types, Site Types, AND Person Details
    - Why Critical: Core operational entity
    - Expected Records: 1000-10000 (all ATM sites)

---

### 🟣 **PHASE 5: Assets & Operations** (DEPENDS ON PHASE 4)
*Collect these AFTER Phase 4 is complete*

#### **Week 5, Day 1-3:**
31. **Assets** ⭐ CRITICAL - COMPLEX DEPENDENCIES
    - Template: `Assets_Upload_Template.xlsx`
    - Columns: Asset Name, Asset Tag ID, Serial Number, Model Number, **Asset Category** (Phase 2), **Asset Type** (Phase 1), **Lender Bank** (Phase 1), **Status Type** (Phase 1), **Vendor Name** (Phase 3)
    - **Dependencies:** Requires Asset Categories, Asset Types, Banks, Generic Status Types, AND Vendors
    - Why Critical: Core asset tracking entity
    - Expected Records: 5000-50000 (all physical assets)

32. **Asset Tag Code Generators**
    - Template: `AssetTagGenerator_Upload_Template.xlsx`
    - Columns: Max Seq Digit, Running Seq, **Asset Category** (Phase 2), **Bank Name** (Phase 1), **Vendor Name** (Phase 3)
    - **Dependencies:** Requires Asset Categories, Banks, AND Vendors
    - Expected Records: 100-500

33. **Activity Works** ⭐ IMPORTANT
    - Template: `ActivityWorks_Upload_Template.xlsx`
    - Columns: Vendor Order Number, Work Order Date, Work Start Date, Work Completion Date, **Activities Name** (Phase 2), **Status Type** (Phase 1), **Vendor Name** (Phase 3)
    - **Dependencies:** Requires Activities, Generic Status Types, AND Vendors
    - Expected Records: 1000-10000 (all work orders)

---

### 🔴 **PHASE 6: Financial Transactions** (DEPENDS ON PHASE 4-5)
*Collect these AFTER Phase 4 & 5 are complete*

#### **Week 6, Day 1-3:**
34. **Payment Details**
    - Template: `PaymentDetails_Upload_Template.xlsx`
    - Columns: Payment Amount, Payment Date, Transaction Number, **Payment Method** (Phase 1)
    - **Dependency:** Requires Payment Methods
    - Expected Records: 1000-10000 (all payments)

35. **Invoices** ⭐ IMPORTANT
    - Template: `Invoices_Upload_Template.xlsx`
    - Columns: Invoice Date, Invoice Number, Total Invoice Value, Payment Status, **Payee Name** (Phase 4), **Payment Details Transaction Number** (Phase 6)
    - **Dependencies:** Requires Payees AND Payment Details
    - Expected Records: 2000-20000 (all invoices)

36. **Vouchers**
    - Template: `Vouchers_Upload_Template.xlsx`
    - Columns: Voucher Date, Voucher Number, Final Amount, Payment Status, **Payee Name** (Phase 4), **Payment Details Transaction Number** (Phase 6)
    - **Dependencies:** Requires Payees AND Payment Details
    - Expected Records: 1000-10000 (all vouchers)

37. **Expenditures - Invoice**
    - Template: `ExpendituresInvoice_Upload_Template.xlsx`
    - Columns: **Cost Item** (Phase 3), **Invoice Number** (Phase 6), **Project Name** (Phase 2)
    - **Dependencies:** Requires Cost Items, Invoices, AND Managed Projects
    - Expected Records: 2000-20000 (links costs to invoices)

38. **Expenditures - Voucher**
    - Template: `ExpendituresVoucher_Upload_Template.xlsx`
    - Columns: **Cost Item** (Phase 3), **Voucher Number** (Phase 6), **Project Name** (Phase 2)
    - **Dependencies:** Requires Cost Items, Vouchers, AND Managed Projects
    - Expected Records: 1000-10000 (links costs to vouchers)

---

### ⚫ **PHASE 7: Asset Movement & Placement** (DEPENDS ON PHASE 5)
*Collect these AFTER Phase 5 is complete*

#### **Week 7, Day 1-4:**
39. **Asset Movement Tracker** ⚠️ COMPLEX
    - Template: `AssetMovementTracker_Upload_Template.xlsx`
    - Columns: **Asset Tag** (Phase 5), **Movement Type** (Phase 1), From Factory OR **From Warehouse** (Phase 3) OR **From Site** (Phase 4) OR **From Datacenter** (Phase 3), To **Warehouse** OR **Site** OR **Datacenter** (similar)
    - **Dependencies:** Requires Assets, Asset Movement Types, AND Warehouses/Sites/Datacenters
    - Why Complex: Multiple optional foreign keys based on movement type
    - Expected Records: 10000-100000 (all asset movements)

40. **Assets on Warehouse**
    - Template: `AssetsOnWarehouse_Upload_Template.xlsx`
    - Columns: Delivered On, **Asset Tag** (Phase 5), **Asset Movement Tracker ID** (Phase 7), **Asset Status** (Phase 1), **Warehouse Name** (Phase 3)
    - **Dependencies:** Requires Assets, Asset Movement Tracker, Generic Status Types, AND Warehouses
    - Expected Records: 5000-50000

41. **Assets on Site**
    - Template: `AssetsOnSite_Upload_Template.xlsx`
    - Columns: Delivered On, **Asset Tag** (Phase 5), **Asset Movement Tracker ID** (Phase 7), **Asset Status** (Phase 1), **Site Code** (Phase 4)
    - **Dependencies:** Requires Assets, Asset Movement Tracker, Generic Status Types, AND Sites
    - Expected Records: 5000-50000

42. **Assets on Datacenter**
    - Template: `AssetsOnDatacenter_Upload_Template.xlsx`
    - Columns: Delivered On, **Asset Tag** (Phase 5), **Asset Movement Tracker ID** (Phase 7), **Asset Status** (Phase 1), **Datacenter Code** (Phase 3)
    - **Dependencies:** Requires Assets, Asset Movement Tracker, Generic Status Types, AND Datacenters
    - Expected Records: 100-1000

43. **Activity Work Remarks**
    - Template: `ActivityWorkRemarks_Upload_Template.xlsx`
    - Columns: Comment, Commented On, **Activity Work Order Number** (Phase 5)
    - **Dependency:** Requires Activity Works
    - Expected Records: 1000-10000 (comments on work orders)

---

### ⚪ **PHASE 8: Complex Junction Tables** (DEPENDS ON PHASES 5-7)
*Collect these LAST - they link multiple entities together*

#### **Week 8:**
44. **Asset Expenditure & Activity Work** ⚠️ VERY COMPLEX
    - Template: `AssetExpenditureActivityWork_Upload_Template.xlsx`
    - Columns: **Activity Work Order Number** (Phase 5), **Asset Tag** (Phase 5), **Expenditure Invoice ID** (Phase 6)
    - **Dependencies:** Requires Activity Works, Assets, AND Expenditures Invoice
    - Why Complex: Links activity work to specific assets and invoices
    - Expected Records: 5000-50000

45. **Site Activity Work Expenditure** ⚠️ VERY COMPLEX
    - Template: `SiteActivityWorkExpenditure_Upload_Template.xlsx`
    - Columns: **Activity Work Order Number** (Phase 5), **Expenditure Invoice ID** (Phase 6), **Site Code** (Phase 4)
    - **Dependencies:** Requires Activity Works, Expenditures Invoice, AND Sites
    - Why Complex: Links site activity work to expenditures
    - Expected Records: 5000-50000

---

## 🚦 Validation Checkpoints

### ✅ Before Moving to Next Phase:
1. **Data Completeness:** Ensure all records are collected
2. **Data Quality:** Validate format, no duplicates, required fields filled
3. **Upload Success:** Bulk upload completed with 0 errors
4. **Sequence Reset:** Database sequences updated (automatic)
5. **Stakeholder Sign-off:** Team confirms data is accurate

---

## 📝 Template Naming Convention

All templates follow this pattern:
```
{EntityName}_Upload_Template.xlsx
```

Examples:
- `State_Upload_Template.xlsx`
- `AssetCategory_Upload_Template.xlsx`
- `Sites_Upload_Template.xlsx`

---

## ⚠️ Critical Notes

### 1. **Lookup Values Must Match Exactly**
When filling templates, foreign key references must match EXACTLY:
- **Case-sensitive:** "PENDING" ≠ "Pending"
- **Whitespace matters:** "SBI" ≠ "SBI " (trailing space)
- **Spelling must be exact:** Use the exact name from the parent table

### 2. **Never Skip Phases**
Following the order is mandatory. If you skip Phase 2 and jump to Phase 4, imports will fail because of missing foreign key references.

### 3. **Test with Sample Data First**
Before collecting all data:
1. Collect 5-10 sample records for each template in order
2. Upload them to test environment
3. Verify imports work
4. Then collect full datasets

### 4. **Parallel Collection Within Same Phase**
You CAN collect data in parallel for templates in the **same phase**, but NOT across phases.

Example:
- ✅ OK: Collect States, Banks, and Status Types simultaneously (all Phase 1)
- ❌ NOT OK: Collect States (Phase 1) and Cities (Phase 2) simultaneously

### 5. **High-Priority Items**
These are marked with ⭐ in the phases above:
- States (required by everything)
- Banks (required by projects and payments)
- Generic Status Types (required by sites, assets, etc.)
- Cities (required by locations)
- Managed Projects (required by sites)
- Person Details (required by vendors and landlords)
- Vendors (required by assets and activities)
- Sites (core operational entity)
- Assets (core tracking entity)

### 6. **Complex Dependencies**
These templates are marked with ⚠️ and require special attention:
- Sites (9 foreign keys)
- Assets (6 foreign keys)
- Asset Movement Tracker (complex optional FKs)
- Asset Expenditure & Activity Work (3-way junction)
- Site Activity Work Expenditure (3-way junction)

---

## 🎓 Training Requirements

### For Data Collectors:
1. Understanding of your business domain (Sites, Assets, Vendors, etc.)
2. Excel proficiency
3. Data quality awareness (no duplicates, proper formatting)
4. Knowledge of lookup values from parent tables

### For Data Validators:
1. Understanding of foreign key relationships
2. Ability to cross-reference data across templates
3. SQL basics (helpful but not required)
4. Quality control processes

---

## 📞 Support & Questions

### Common Issues:

**Q: What if we don't have data for some optional fields?**
A: Leave them blank. Only fields marked as "required" in the template must be filled.

**Q: Can we upload partial data and add more later?**
A: Yes! You can upload data in batches, as long as dependencies are satisfied.

**Q: What if a bulk upload fails?**
A: The system generates an error report showing which rows failed and why. Fix those rows and re-upload.

**Q: Can we update existing records?**
A: No, bulk upload is for new records only. Use the UI for updates, or delete and re-import.

**Q: What if we need to add a new State/Bank/etc. later?**
A: You can upload additional master data anytime. Child records can reference the new master data immediately.

---

## 📊 Progress Tracking Template

Use this to track your data collection progress:

| Phase | Template Name | Assigned To | Due Date | Records Expected | Status | Upload Date | Issues |
|-------|---------------|-------------|----------|------------------|--------|-------------|--------|
| 1 | States | | | 30 | Not Started | | |
| 1 | Banks | | | 50 | Not Started | | |
| ... | ... | | | | | | |

---

## ✨ Best Practices

1. **Start Small:** Collect master data first (Phase 1)
2. **Validate Early:** Upload Phase 1 data immediately to catch issues
3. **Use Templates:** Never create your own Excel format
4. **Document Sources:** Note where data came from for traceability
5. **Backup Everything:** Keep copies of all collected data
6. **Version Control:** Use template version numbers (v1, v2, etc.)
7. **Regular Reviews:** Weekly check-ins on progress
8. **Quality over Speed:** Better to take time and get it right

---

## 🏁 Final Checklist

Before declaring data collection complete:

- [ ] All 43 templates collected
- [ ] All uploads successful (0 errors)
- [ ] Database sequences reset
- [ ] Sample queries verified (can retrieve data correctly)
- [ ] Stakeholders approved data quality
- [ ] Backup taken of all collected data
- [ ] Documentation updated with any custom fields
- [ ] Training completed for data entry users

---

## 📅 Recommended Timeline

**Total Duration:** 8 weeks (aggressive) to 12 weeks (comfortable)

- **Weeks 1-2:** Foundation Masters & Financial Setup (Phases 1-2)
- **Weeks 3-4:** Vendors, Infrastructure, Assets & Sites (Phases 3-4)
- **Weeks 5-6:** Operations & Financial Transactions (Phases 5-6)
- **Weeks 7-8:** Asset Movements & Complex Relationships (Phases 7-8)

**Buffer:** Add 2-4 weeks for reviews, corrections, and re-uploads

---

## 📌 Document Version

- **Version:** 1.0
- **Created:** November 14, 2025
- **Last Updated:** November 14, 2025
- **Author:** System Architecture Team
- **Status:** Final

---

**Remember:** This order is based on database constraints. Following it ensures smooth data import without foreign key violations!
