import React, { useState, useMemo } from 'react'
import { Checkbox } from '@/components/ui/checkbox'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { ScrollArea } from '@/components/ui/scroll-area'
import { ChevronDown, ChevronRight, Search, CheckSquare, Square } from 'lucide-react'
import { usePermissionsByCategory } from '@/hooks/use-roles'
import type { PermissionDTO } from '@/lib/permissions-api'
import { cn } from '@/lib/utils'

interface PermissionSelectorProps {
  /** Selected permission IDs */
  value: number[]
  /** Callback when selection changes */
  onChange: (permissionIds: number[]) => void
  /** Disabled state */
  disabled?: boolean
}

/**
 * Permission Selector Component
 * Displays permissions grouped by category with expand/collapse, select all, and search
 */
export function PermissionSelector({
  value = [],
  onChange,
  disabled = false,
}: PermissionSelectorProps) {
  const [searchTerm, setSearchTerm] = useState('')
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(
    new Set()
  )

  const { data: permissionsByCategory, isLoading } =
    usePermissionsByCategory()

  // Flatten all permissions for search
  const allPermissions = useMemo(() => {
    if (!permissionsByCategory) return []
    return Object.values(permissionsByCategory).flat()
  }, [permissionsByCategory])

  // Filter permissions by search term
  const filteredPermissionsByCategory = useMemo(() => {
    if (!permissionsByCategory) return {}
    if (!searchTerm) return permissionsByCategory

    const filtered: Record<string, PermissionDTO[]> = {}
    Object.entries(permissionsByCategory).forEach(([category, permissions]) => {
      const matchingPermissions = permissions.filter(
        (p) =>
          p.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.scope.toLowerCase().includes(searchTerm.toLowerCase()) ||
          p.action.toLowerCase().includes(searchTerm.toLowerCase())
      )
      if (matchingPermissions.length > 0) {
        filtered[category] = matchingPermissions
      }
    })
    return filtered
  }, [permissionsByCategory, searchTerm])

  // Auto-expand categories when searching
  React.useEffect(() => {
    if (searchTerm && filteredPermissionsByCategory) {
      setExpandedCategories(
        new Set(Object.keys(filteredPermissionsByCategory))
      )
    }
  }, [searchTerm, filteredPermissionsByCategory])

  const toggleCategory = (category: string) => {
    setExpandedCategories((prev) => {
      const next = new Set(prev)
      if (next.has(category)) {
        next.delete(category)
      } else {
        next.add(category)
      }
      return next
    })
  }

  const togglePermission = (permissionId: number) => {
    if (disabled) return
    const newValue = value.includes(permissionId)
      ? value.filter((id) => id !== permissionId)
      : [...value, permissionId]
    onChange(newValue)
  }

  const selectAllInCategory = (permissions: PermissionDTO[]) => {
    if (disabled) return
    const permissionIds = permissions.map((p) => p.id)
    const allSelected = permissionIds.every((id) => value.includes(id))

    if (allSelected) {
      // Deselect all in this category
      onChange(value.filter((id) => !permissionIds.includes(id)))
    } else {
      // Select all in this category
      const newValue = [...new Set([...value, ...permissionIds])]
      onChange(newValue)
    }
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center rounded-lg border border-dashed p-12">
        <div className="text-sm text-muted-foreground">
          Loading permissions...
        </div>
      </div>
    )
  }

  if (!filteredPermissionsByCategory || Object.keys(filteredPermissionsByCategory).length === 0) {
    return (
      <div className="flex items-center justify-center rounded-lg border border-dashed p-12">
        <div className="text-sm text-muted-foreground">
          {searchTerm ? 'No permissions match your search' : 'No permissions available'}
        </div>
      </div>
    )
  }

  const categories = Object.entries(filteredPermissionsByCategory)

  return (
    <div className="space-y-3">
      {/* Search Bar */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search permissions by name, scope, or action..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="pl-9 h-9"
          disabled={disabled}
        />
      </div>

      {/* Selected Count */}
      <div className="flex items-center justify-between text-xs">
        <span className="text-muted-foreground">
          {value.length} of {allPermissions.length} selected
        </span>
        {value.length > 0 && (
          <Badge variant="secondary" className="h-5 text-xs">
            {value.length}
          </Badge>
        )}
      </div>

      {/* Permission Categories */}
      <ScrollArea className="h-[400px] rounded-md border">
        <div className="p-3 space-y-1">
          {categories.map(([category, permissions]) => {
            const isExpanded = expandedCategories.has(category)
            const categoryPermissionIds = permissions.map((p) => p.id)
            const allCategorySelected = categoryPermissionIds.every((id) =>
              value.includes(id)
            )
            const someCategorySelected =
              categoryPermissionIds.some((id) => value.includes(id)) &&
              !allCategorySelected

            return (
              <div key={category} className="rounded-lg border bg-card">
                {/* Category Header */}
                <div
                  className={cn(
                    "flex items-center gap-2 p-3 hover:bg-accent/50 transition-colors",
                    isExpanded && "border-b"
                  )}
                >
                  <div className="flex items-center gap-2 flex-1">
                    <div
                      className="flex items-center gap-2 cursor-pointer"
                      onClick={() => toggleCategory(category)}
                    >
                      {isExpanded ? (
                        <ChevronDown className="h-4 w-4 text-muted-foreground shrink-0" />
                      ) : (
                        <ChevronRight className="h-4 w-4 text-muted-foreground shrink-0" />
                      )}
                    </div>
                    <div
                      className="flex items-center gap-2 cursor-pointer"
                      onClick={(e) => {
                        e.stopPropagation()
                        selectAllInCategory(permissions)
                      }}
                    >
                      {allCategorySelected ? (
                        <CheckSquare className="h-4 w-4 text-primary" />
                      ) : someCategorySelected ? (
                        <Square className="h-4 w-4 text-primary fill-primary/20" />
                      ) : (
                        <Square className="h-4 w-4 text-muted-foreground" />
                      )}
                    </div>
                    <span 
                      className="font-medium text-sm cursor-pointer"
                      onClick={() => toggleCategory(category)}
                    >{category}</span>
                    <Badge variant="outline" className="ml-auto h-5 px-2 text-xs">
                      {permissions.filter(p => value.includes(p.id)).length}/{permissions.length}
                    </Badge>
                  </div>
                </div>

                {/* Category Permissions */}
                {isExpanded && (
                  <div className="p-2 space-y-0.5">
                    {permissions.map((permission) => (
                      <label
                        key={permission.id}
                        htmlFor={`permission-${permission.id}`}
                        className={cn(
                          "flex items-start gap-3 rounded-md p-2 transition-colors",
                          "hover:bg-accent cursor-pointer",
                          value.includes(permission.id) && "bg-accent/50"
                        )}
                      >
                        <Checkbox
                          id={`permission-${permission.id}`}
                          checked={value.includes(permission.id)}
                          onCheckedChange={() => togglePermission(permission.id)}
                          disabled={disabled}
                          className="mt-0.5"
                        />
                        <div className="flex-1 min-w-0">
                          <div className="flex items-center gap-2">
                            <span className="font-mono text-xs font-medium truncate">
                              {permission.name}
                            </span>
                          </div>
                          {permission.description && (
                            <p className="text-xs text-muted-foreground mt-0.5 line-clamp-1">
                              {permission.description}
                            </p>
                          )}
                        </div>
                      </label>
                    ))}
                  </div>
                )}
              </div>
            )
          })}
        </div>
      </ScrollArea>
    </div>
  )
}
