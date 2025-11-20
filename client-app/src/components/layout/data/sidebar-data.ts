import {
  LayoutDashboard,
  Bug,
  ListTodo,
  Users,
  ShieldCheck,
  Database,
  Landmark,
  MapPin,
  Package,
  Activity,
  Building2,
  MapPinned,
  UserCircle,
  Briefcase,
  Receipt,
  FolderTree,
  Shield,
} from 'lucide-react'
import { type SidebarData } from '../types'

export const sidebarData: SidebarData = {
  user: {
    name: 'satnaing',
    email: 'satnaingdev@gmail.com',
    avatar: '/avatars/shadcn.jpg',
  },

  navGroups: [
    {
      title: 'General',
      items: [
        {
          title: 'Dashboard',
          url: '/',
          icon: LayoutDashboard,
        },
        {
          title: 'Tasks',
          url: '/tasks',
          icon: ListTodo,
        },
        {
          title: 'Users',
          url: '/users',
          icon: Users,
        },
        {
          title: 'Roles & Permissions',
          url: '/roles',
          icon: Shield,
        },
      ],
    },
    {
      title: 'Core Masters',
      items: [
        {
          title: 'Location Setup',
          icon: MapPin,
          items: [
            {
              title: 'States',
              url: '/states',
            },
            {
              title: 'Cities',
              url: '/cities',
            },
            {
              title: 'Locations',
              url: '/locations',
            },
            {
              title: 'Warehouses',
              url: '/warehouses',
            },
            {
              title: 'Datacenters',
              url: '/datacenters',
            },
          ],
        },
        {
          title: 'Asset Configuration',
          icon: Package,
          items: [
            {
              title: 'Asset Types',
              url: '/asset-types',
            },
            {
              title: 'Asset Categories',
              url: '/asset-categories',
            },
            {
              title: 'Movement Types',
              url: '/movement-types',
            },
            {
              title: 'Asset Tag Generators',
              url: '/asset-tag-generators',
            },
          ],
        },
        {
          title: 'Site Configuration',
          icon: MapPinned,
          items: [
            {
              title: 'Site Types',
              url: '/site-types',
            },
            {
              title: 'Site Categories',
              url: '/site-categories',
            },
            {
              title: 'Site Code Generators',
              url: '/site-code-generators',
            },
          ],
        },
        {
          title: 'Activity Configuration',
          icon: Activity,
          items: [
            {
              title: 'Activity Master',
              url: '/activities',
            },
          ],
        },
        {
          title: 'Financial Setup',
          icon: Database,
          items: [
            {
              title: 'Banks',
              url: '/banks',
            },
            {
              title: 'Cost Categories',
              url: '/cost-categories',
            },
            {
              title: 'Cost Types',
              url: '/cost-types',
            },
            {
              title: 'Cost Items',
              url: '/cost-items',
            },
            {
              title: 'Payment Methods',
              url: '/payment-methods',
            },
            {
              title: 'Payee Types',
              url: '/payee-types',
            },
          ],
        },
        {
          title: 'Classifications',
          icon: FolderTree,
          items: [
            {
              title: 'Vendor Categories',
              url: '/vendor-categories',
            },
            {
              title: 'Vendor Types',
              url: '/vendor-types',
            },
            {
              title: 'Person Types',
              url: '/person-types',
            },
            {
              title: 'Status Types',
              url: '/generic-status-types',
            },
          ],
        },
      ],
    },
    {
      title: 'Operations',
      items: [
        {
          title: 'Asset Management',
          icon: Package,
          items: [
            {
              title: 'Assets',
              url: '/assets',
            },
          ],
        },
        {
          title: 'Site Management',
          icon: MapPinned,
          items: [
            {
              title: 'Sites',
              url: '/sites',
            },
          ],
        },
        {
          title: 'Activity Management',
          icon: Activity,
          items: [
            {
              title: 'Activities',
              url: '/activities-list',
            },
            {
              title: 'Activity Works',
              url: '/activity-works',
            },
          ],
        },
        {
          title: 'Project Management',
          icon: Briefcase,
          items: [
            {
              title: 'Managed Projects',
              url: '/managed-projects',
            },
          ],
        },
      ],
    },
    {
      title: 'Financial',
      items: [
        {
          title: 'Transactions',
          icon: Receipt,
          items: [
            {
              title: 'Invoices',
              url: '/invoices',
            },
            {
              title: 'Vouchers',
              url: '/vouchers',
            },
          ],
        },
        {
          title: 'Expenditures',
          icon: Landmark,
          items: [
            {
              title: 'Invoice Expenditures',
              url: '/expenditures/invoices',
            },
            {
              title: 'Voucher Expenditures',
              url: '/expenditures/vouchers',
            },
          ],
        },
        {
          title: 'Payment Management',
          icon: Landmark,
          items: [
            {
              title: 'Payment Details',
              url: '/payment-details',
            },
            {
              title: 'Payee Details',
              url: '/payee-details',
            },
            {
              title: 'Payees',
              url: '/payees',
            },
          ],
        },
      ],
    },
    {
      title: 'People & Organizations',
      items: [
        {
          title: 'Vendors',
          icon: Building2,
          items: [
            {
              title: 'Vendors',
              url: '/vendors',
            },
          ],
        },
        {
          title: 'Landlords',
          icon: UserCircle,
          items: [
            {
              title: 'Landlords',
              url: '/landlords',
            },
          ],
        },
        {
          title: 'Persons',
          icon: UserCircle,
          items: [
            {
              title: 'Person Details',
              url: '/person-details',
            },
          ],
        },
      ],
    },
    {
      title: 'Pages',
      items: [
        {
          title: 'Auth',
          icon: ShieldCheck,
          items: [
            {
              title: 'Sign In',
              url: '/sign-in',
            },
            {
              title: 'Sign Up',
              url: '/sign-up',
            },
            {
              title: 'Forgot Password',
              url: '/forgot-password',
            },
            {
              title: 'OTP',
              url: '/otp',
            },
          ],
        },
        {
          title: 'Errors',
          icon: Bug,
          items: [
            {
              title: 'Unauthorized',
              url: '/errors/unauthorized',
            },
            {
              title: 'Forbidden',
              url: '/errors/forbidden',
            },
            {
              title: 'Not Found',
              url: '/errors/not-found',
            },
            {
              title: 'Internal Server Error',
              url: '/errors/internal-server-error',
            },
            {
              title: 'Maintenance Error',
              url: '/errors/maintenance-error',
            },
          ],
        },
      ],
    },
  ],
}
