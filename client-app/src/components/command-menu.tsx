import React from 'react'
import { useNavigate } from '@tanstack/react-router'
import { ArrowRight, Laptop, Moon, Sun } from 'lucide-react'
import { useSearch } from '@/context/search-provider'
import { useTheme } from '@/context/theme-provider'
import {
  CommandDialog,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandSeparator,
} from '@/components/ui/command'
import { sidebarData } from './layout/data/sidebar-data'

export function CommandMenu() {
  const navigate = useNavigate()
  const { setTheme } = useTheme()
  const { open, setOpen } = useSearch()

  const runCommand = React.useCallback(
    (command: () => unknown) => {
      setOpen(false)
      command()
    },
    [setOpen]
  )

  // Flatten nested navigation items for better UX
  const flattenNavItems = () => {
    const items: Array<{
      title: string
      url: string
      breadcrumb: string[]
    }> = []

    sidebarData.navGroups.forEach((group) => {
      group.items.forEach((navItem) => {
        // Direct URL items
        if (navItem.url) {
          items.push({
            title: navItem.title,
            url: navItem.url,
            breadcrumb: [group.title],
          })
        }
        // Level 1 nested items
        else if (navItem.items) {
          navItem.items.forEach((subItem) => {
            if (subItem.url) {
              items.push({
                title: subItem.title,
                url: subItem.url,
                breadcrumb: [group.title, navItem.title],
              })
            }
            // Level 2 nested items
            else if (subItem.items) {
              subItem.items.forEach((deepItem) => {
                if (deepItem.url) {
                  items.push({
                    title: deepItem.title,
                    url: deepItem.url,
                    breadcrumb: [group.title, subItem.title],
                  })
                }
              })
            }
          })
        }
      })
    })

    return items
  }

  const navItems = React.useMemo(() => flattenNavItems(), [])

  return (
    <CommandDialog modal open={open} onOpenChange={setOpen}>
      <CommandInput placeholder='Type a command or search...' />
      <CommandList 
        className='max-h-[300px]'
        style={{ scrollBehavior: 'auto' }}
      >
        <CommandEmpty>No results found.</CommandEmpty>
        <CommandGroup heading='Navigation'>
          {navItems.map((item) => (
            <CommandItem
              key={item.url}
              value={`${item.breadcrumb.join(' ')} ${item.title}`}
              onSelect={() => {
                runCommand(() => navigate({ to: item.url }))
              }}
            >
              <ArrowRight className='mr-2 size-4 text-muted-foreground' />
              <span className='font-medium'>{item.title}</span>
              {item.breadcrumb.length > 0 && (
                <span className='ml-2 text-xs text-muted-foreground'>
                  {item.breadcrumb.join(' › ')}
                </span>
              )}
            </CommandItem>
          ))}
        </CommandGroup>
          <CommandSeparator />
          <CommandGroup heading='Theme'>
            <CommandItem onSelect={() => runCommand(() => setTheme('light'))}>
              <Sun /> <span>Light</span>
            </CommandItem>
            <CommandItem onSelect={() => runCommand(() => setTheme('dark'))}>
              <Moon className='scale-90' />
              <span>Dark</span>
            </CommandItem>
            <CommandItem onSelect={() => runCommand(() => setTheme('system'))}>
              <Laptop />
              <span>System</span>
            </CommandItem>
          </CommandGroup>
      </CommandList>
    </CommandDialog>
  )
}
