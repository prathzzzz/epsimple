import { type LinkProps } from '@tanstack/react-router'

type User = {
  name: string
  email: string
  avatar: string
}

type BaseNavItem = {
  title: string
  badge?: string
  icon?: React.ElementType
}

type NavLink = BaseNavItem & {
  url: LinkProps['to'] | (string & {})
  items?: never
}

type NavCollapsibleLevel2 = BaseNavItem & {
  items: (BaseNavItem & { url: LinkProps['to'] | (string & {}) })[]
  url?: never
}

type NavCollapsibleLevel1 = BaseNavItem & {
  items: (NavCollapsibleLevel2 | NavLink)[]
  url?: never
}

type NavItem = NavCollapsibleLevel1 | NavLink

type NavGroup = {
  title: string
  items: NavItem[]
}

type SidebarData = {
  user: User
  navGroups: NavGroup[]
}

export type { SidebarData, NavGroup, NavItem, NavCollapsibleLevel1, NavCollapsibleLevel2, NavLink }
