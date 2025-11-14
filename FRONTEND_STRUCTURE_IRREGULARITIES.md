# Frontend Structure Irregularities & Standardization Guide

**Date Created**: November 14, 2025  
**Purpose**: Document inconsistencies across frontend feature modules and establish standards

---

## 🔍 IDENTIFIED IRREGULARITIES

### **1. Context/Provider File Organization**

#### **Pattern A: Proper 3-File Split (CORRECT)** ✅
**Used by**: `vouchers`, `invoices`, `expenditures-invoice`

```
features/
  vouchers/
    context/
      index.ts                    # Barrel exports
      voucher-context.ts         # Context interface + creation
      voucher-provider.tsx       # Provider component only
    hooks/
      index.ts                    # Barrel exports
      use-voucher.ts             # Custom hook
```

**Benefits**:
- ✅ Fast Refresh compliant (no mixed exports)
- ✅ Clear separation of concerns
- ✅ Reusable hook pattern
- ✅ Proper barrel exports

#### **Pattern B: Provider in Components (INCONSISTENT)** ⚠️
**Used by**: `states`, `banks`, `warehouses`, `datacenters`, `sites`, `payees`, `assets`

```
features/
  states/
    components/
      states-provider.tsx        # Context + Provider + Hook in components/
    context/                      # Empty or missing!
```

**Issues**:
- ❌ Provider mixed with UI components
- ❌ No dedicated context/ or hooks/ folders
- ❌ Hook exported from provider file (Fast Refresh risk)
- ❌ Inconsistent with newer patterns

#### **Pattern C: Single Provider File (LEGACY)** ❌
**Used by**: `vendors`

```
features/
  vendors/
    vendor-provider.tsx          # Context + Provider + Hook in root
    context/                      # Missing!
    hooks/                        # Missing!
```

**Issues**:
- ❌ All in one file at feature root
- ❌ No proper folder structure
- ❌ Fast Refresh violations
- ❌ Not scalable

#### **Pattern D: Inconsistent Hook Naming** ⚠️
**Used by**: `expenditures-invoice`

```
features/
  expenditures-invoice/
    hooks/
      use-expenditures-invoice-context.ts  # Should be use-expenditures-invoice.ts
```

**Issues**:
- ❌ Suffix "-context" is redundant (hook always accesses context)
- ❌ Inconsistent with `use-voucher.ts`, `use-invoice.ts` pattern

---

### **2. Missing Hooks Folders**

| Feature | Has hooks/ | Pattern |
|---------|-----------|---------|
| ✅ vouchers | Yes | Correct 3-file split |
| ✅ invoices | Yes | Correct 3-file split |
| ⚠️ expenditures-invoice | Yes | Wrong hook name |
| ❌ sites | No | Provider in components/ |
| ❌ assets | No | Provider in components/ |
| ❌ payees | No | Provider in components/ |
| ❌ states | No | Provider in components/ |
| ❌ banks | No | Provider in components/ |
| ❌ warehouses | No | Provider in components/ |
| ❌ vendors | No | Single file at root |

**Impact**: 
- Inconsistent import paths across features
- Harder to locate context hooks
- Mixed component/logic concerns

---

### **3. Context Folder Inconsistencies**

| Feature | context/ Structure | Status |
|---------|-------------------|--------|
| vouchers | context.ts + provider.tsx + index.ts | ✅ Perfect |
| invoices | context.ts + provider.tsx + index.ts | ✅ Perfect |
| expenditures-invoice | context.ts + provider.tsx + index.ts | ✅ Good structure |
| sites | Only provider.tsx | ⚠️ Incomplete |
| assets | Only provider.tsx | ⚠️ Incomplete |
| payees | Only provider.tsx | ⚠️ Incomplete |
| states | Empty or missing | ❌ Wrong location |
| banks | Empty or missing | ❌ Wrong location |
| vendors | Missing entirely | ❌ No folder |

---

### **4. Import Path Inconsistencies**

#### **Example 1: Vouchers (Initially Wrong, Now Fixed)**
```tsx
// ❌ WRONG (caused runtime error)
import { useVoucher } from "../context/voucher-provider";

// ✅ CORRECT
import { useVoucher } from "../hooks/use-voucher";
```

#### **Example 2: States**
```tsx
// Current (inconsistent with vouchers/invoices)
import { useStates } from "../components/states-provider";

// Should be:
import { useStates } from "../hooks/use-states";
```

#### **Example 3: Vendors**
```tsx
// Current (legacy)
import { useVendor } from "../vendor-provider";

// Should be:
import { useVendor } from "../hooks/use-vendor";
```

---

### **5. Data Folder Inconsistency**

**Features with `data/` folder**: `states`, `banks`

```
features/
  states/
    data/               # ❓ What is this for?
      ...
  banks/
    data/
      ...
```

**Question**: 
- What is stored in `data/` folders?
- Should all features have this?
- Is this for mock data, constants, or something else?

---

### **6. Special Cases**

#### **Assets Feature** 🎨
```
features/
  assets/
    lib/                # Additional lib/ folder
    context/
      asset-provider.tsx
```

**Questions**:
- What's in `lib/`? 
- Should other features have `lib/` for utilities?

#### **Vendors Feature** 🏢
```
features/
  vendors/
    schema.ts           # Schema at root
    vendor-provider.tsx # Provider at root
    create-vendor-button.tsx  # Component at root
    vendor-columns.tsx  # Columns at root
    vendor-drawer.tsx   # Drawer at root
    vendor-row-actions.tsx  # Actions at root
    vendors-table.tsx   # Table at root
    components/         # Some components here too
    api/
```

**Issues**:
- ❌ Completely flat structure
- ❌ Components scattered between root and components/
- ❌ No hooks/ or proper context/ organization

---

## 📋 STANDARDIZATION REQUIREMENTS

### **✅ STANDARD STRUCTURE (MANDATORY)**

Every feature MUST follow this structure:

```
features/
  {entity-name}/
    api/
      schema.ts              # Types/interfaces
      {entity}-api.ts        # API calls
      
    context/
      index.ts               # Barrel: export { EntityProvider } from './entity-provider'
      entity-context.ts      # Context interface + createContext() ONLY
      entity-provider.tsx    # Provider component ONLY
      
    hooks/
      index.ts               # Barrel: export { useEntity } from './use-entity'
      use-entity.ts          # Custom hook: useContext(EntityContext)
      
    components/
      {entity}-table.tsx
      {entity}-columns.tsx
      {entity}-dialogs.tsx
      {entity}-primary-buttons.tsx
      {entity}-mutate-drawer.tsx
      data-table-row-actions.tsx
      
    index.tsx              # Main feature page component
```

---

### **✅ FILE CONTENT RULES**

#### **1. entity-context.ts** (Interface + Context Creation ONLY)
```tsx
import { createContext } from 'react';
import type { Entity } from '../api/schema';

export interface EntityContextType {
  // ... state types
}

export const EntityContext = createContext<EntityContextType | undefined>(undefined);
```

**Rules**:
- ✅ ONLY interface and createContext()
- ✅ Export interface and context
- ❌ NO Provider component
- ❌ NO useState or logic

---

#### **2. entity-provider.tsx** (Provider Component ONLY)
```tsx
import { useState } from 'react';
import { EntityContext } from './entity-context';
import type { EntityContextType } from './entity-context';

export function EntityProvider({ children }: { children: React.ReactNode }) {
  // ... state and logic
  
  return (
    <EntityContext.Provider value={value}>
      {children}
    </EntityContext.Provider>
  );
}
```

**Rules**:
- ✅ ONLY Provider component
- ✅ Import context from entity-context.ts
- ✅ All state management here
- ❌ NO hook export (causes Fast Refresh warnings)

---

#### **3. hooks/use-entity.ts** (Custom Hook ONLY)
```tsx
import { useContext } from 'react';
import { EntityContext } from '../context/entity-context';
import type { EntityContextType } from '../context/entity-context';

export function useEntity(): EntityContextType {
  const context = useContext(EntityContext);
  if (context === undefined) {
    throw new Error('useEntity must be used within EntityProvider');
  }
  return context;
}
```

**Rules**:
- ✅ ONLY custom hook
- ✅ Import context from ../context/entity-context
- ✅ Proper error handling
- ❌ NO component exports

---

#### **4. index.tsx** (Main Page Component)
```tsx
import { EntityProvider } from './context/entity-provider';
import { useEntity } from './hooks/use-entity';
// ... other imports

function EntityContent() {
  const { state } = useEntity();
  // ... component logic
}

export default function EntityPage() {
  return (
    <EntityProvider>
      <EntityContent />
    </EntityProvider>
  );
}
```

**Rules**:
- ✅ Import Provider from ./context/entity-provider
- ✅ Import hook from ./hooks/use-entity
- ✅ Separate inner content component
- ❌ NO direct context imports

---

### **✅ IMPORT PATH STANDARDS**

#### **From Components (data-table-row-actions.tsx, dialogs.tsx, etc.)**
```tsx
// ✅ CORRECT
import { useEntity } from '../hooks/use-entity';

// ❌ WRONG
import { useEntity } from '../context/entity-provider';
import { useEntity } from '../entity-provider';
```

#### **From Main index.tsx**
```tsx
// ✅ CORRECT
import { EntityProvider } from './context/entity-provider';
import { useEntity } from './hooks/use-entity';

// ❌ WRONG
import { EntityProvider, useEntity } from './context/entity-provider';
```

---

### **✅ NAMING CONVENTIONS**

| File Type | Pattern | Example |
|-----------|---------|---------|
| Context interface | `{Entity}Context.ts` | `voucher-context.ts` |
| Provider component | `{Entity}Provider.tsx` | `voucher-provider.tsx` |
| Custom hook | `use-{entity}.ts` | `use-voucher.ts` |
| Barrel export | `index.ts` | `context/index.ts`, `hooks/index.ts` |

**Hook Naming Rules**:
- ✅ `use-entity.ts` (simple, matches entity name)
- ❌ `use-entity-context.ts` (redundant "-context" suffix)

---

## 🔧 MIGRATION CHECKLIST

### **Phase 1: High Priority (Bulk Upload Features)**

Need immediate migration (for consistency with vouchers/invoices):

1. ⬜ **expenditures-invoice**
   - Rename: `use-expenditures-invoice-context.ts` → `use-expenditures-invoice.ts`
   - Update all imports

2. ⬜ **payees**
   - Move: `context/payee-provider.tsx` → proper 3-file split
   - Create: `context/payee-context.ts`, `hooks/use-payee.ts`
   - Update all imports

3. ⬜ **assets**
   - Move: `context/asset-provider.tsx` → proper 3-file split
   - Create: `context/asset-context.ts`, `hooks/use-asset.ts`
   - Update all imports
   - Keep `lib/` folder (investigate purpose)

4. ⬜ **sites**
   - Move: `context/site-provider.tsx` → proper 3-file split
   - Create: `context/site-context.ts`, `hooks/use-site.ts`
   - Update all imports

---

### **Phase 2: Medium Priority (Master Data)**

5. ⬜ **states**
   - Move: `components/states-provider.tsx` → `context/` (3-file split)
   - Create: `hooks/use-states.ts`
   - Update all imports
   - Investigate `data/` folder purpose

6. ⬜ **banks**
   - Move: `components/banks-provider.tsx` → `context/` (3-file split)
   - Create: `hooks/use-banks.ts`
   - Update all imports
   - Investigate `data/` folder purpose

7. ⬜ **warehouses**
   - Move: `components/warehouse-provider.tsx` → `context/` (3-file split)
   - Create: `hooks/use-warehouse.ts`
   - Update all imports

8. ⬜ **datacenters**
   - Move: `components/datacenter-provider.tsx` → `context/` (3-file split)
   - Create: `hooks/use-datacenter.ts`
   - Update all imports

---

### **Phase 3: Lower Priority (Other Features)**

9. ⬜ **vendors** (Full Restructure)
   - Move all root components → `components/`
   - Create proper `context/` structure (3-file split)
   - Create `hooks/use-vendor.ts`
   - Move `schema.ts` → `api/schema.ts`
   - Update all imports

10. ⬜ **All remaining features**
    - Apply same pattern to:
      - cities, locations, movement-types, site-types, site-categories
      - asset-types, asset-categories, vendor-types, vendor-categories
      - cost-categories, cost-types, cost-items, managed-projects
      - payment-methods, payment-details, payee-types, payee-details
      - person-types, person-details, generic-status-types
      - activities, activity-works, landlords

---

## 📊 MIGRATION STATUS TRACKER

### **Currently Compliant** ✅
- [x] vouchers (3-file split, proper hooks)
- [x] invoices (3-file split, proper hooks)

### **Needs Hook Rename Only** ⚠️
- [ ] expenditures-invoice (rename hook file)

### **Needs Full Migration** ❌
**Priority 1 (Bulk Upload)**:
- [ ] payees
- [ ] assets  
- [ ] sites

**Priority 2 (Master Data)**:
- [ ] states
- [ ] banks
- [ ] warehouses
- [ ] datacenters

**Priority 3 (Vendor Module)**:
- [ ] vendors (full restructure)

**Priority 4 (Remaining)**:
- [ ] 30+ other features

---

## 🚨 CRITICAL ISSUES TO FIX IMMEDIATELY

### **1. Fast Refresh Violations**
Any file exporting BOTH a component AND a hook will cause warnings:

```tsx
// ❌ CAUSES WARNING
export function EntityProvider() { ... }
export function useEntity() { ... }
```

**Solution**: Split into separate files (context.ts, provider.tsx, use-entity.ts)

---

### **2. Wrong Import Paths**
Files importing hooks from provider files instead of hooks folder:

**Search for**:
```bash
# Find all wrong imports
grep -r "from.*provider" --include="*.tsx" --include="*.ts" | grep "use"
```

**Fix pattern**:
```tsx
// ❌ Wrong
import { useEntity } from '../context/entity-provider';

// ✅ Correct
import { useEntity } from '../hooks/use-entity';
```

---

### **3. Missing Error Boundaries**
Hooks used outside Provider context will crash. Always check:

```tsx
export function useEntity(): EntityContextType {
  const context = useContext(EntityContext);
  if (context === undefined) {
    throw new Error('useEntity must be used within EntityProvider');
  }
  return context;
}
```

---

## 📝 IMPLEMENTATION NOTES

### **Why 3-File Split?**

1. **Fast Refresh Compliance**: React Fast Refresh requires files to export ONLY components OR ONLY hooks
2. **Separation of Concerns**: Interface, Provider, Hook each have single responsibility
3. **Tree Shaking**: Unused context parts can be eliminated by bundler
4. **Testability**: Each piece can be tested independently
5. **Scalability**: Easy to add more hooks or context properties

---

### **Migration Template**

Use this template when migrating a feature:

```bash
# 1. Create proper folder structure
mkdir -p features/{entity}/context
mkdir -p features/{entity}/hooks

# 2. Split existing provider into 3 files
# - Extract interface → entity-context.ts
# - Keep provider → entity-provider.tsx  
# - Create hook → use-entity.ts

# 3. Create barrel exports
# - context/index.ts
# - hooks/index.ts

# 4. Update all imports
# Search: import.*from.*provider
# Replace: import.*from.*hooks/use-entity

# 5. Test Fast Refresh
# No warnings should appear in console
```

---

## 🎯 SUCCESS CRITERIA

Migration is complete when:

- ✅ All features have `context/` folder with 3-file split
- ✅ All features have `hooks/` folder with use-{entity}.ts
- ✅ No Fast Refresh warnings in console
- ✅ All imports use correct paths (hooks for hooks, context for Provider)
- ✅ All hooks have proper error handling
- ✅ Barrel exports in place (index.ts files)
- ✅ Consistent naming across all features

---

## 📚 REFERENCE IMPLEMENTATIONS

### **✅ Perfect Example: Vouchers**

Study these files as reference:

1. `features/vouchers/context/voucher-context.ts` - Interface + Context
2. `features/vouchers/context/voucher-provider.tsx` - Provider only
3. `features/vouchers/hooks/use-voucher.ts` - Hook only
4. `features/vouchers/index.tsx` - Proper imports
5. `features/vouchers/components/voucher-dialogs.tsx` - Component using hook

### **✅ Good Example: Invoices**

Same pattern as vouchers, equally good reference.

### **⚠️ Bad Example: Vendors**

Avoid this structure - flat organization, mixed concerns.

---

**Last Updated**: November 14, 2025  
**Next Review**: After Phase 1 migration complete  
**Owner**: GitHub Copilot / Development Team
