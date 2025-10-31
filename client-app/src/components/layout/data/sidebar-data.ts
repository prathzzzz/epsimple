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
  CheckCircle,
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
      ],
    },
    {
      title: 'Masters',
      items: [
        {
          title: 'Master Data',
          icon: Database,
          items: [
            {
              title: 'Location',
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
              title: 'Financial',
              icon: Landmark,
              items: [
                {
                  title: 'Banks',
                  url: '/banks',
                },
                {
                  title: 'Managed Projects',
                  url: '/managed-projects',
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
                  title: 'Payment Details',
                  url: '/payment-details',
                },
                {
                  title: 'Payee Types',
                  url: '/payee-types',
                },
                {
                  title: 'Payee Details',
                  url: '/payee-details',
                },
                {
                  title: 'Payees',
                  url: '/payees',
                },
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
              title: 'Vendor',
              icon: Building2,
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
                  title: 'Vendors',
                  url: '/vendors',
                },
              ],
            },
            {
              title: 'Landlord',
              icon: UserCircle,
              items: [
                {
                  title: 'Landlords',
                  url: '/landlords',
                },
              ],
            },
            {
              title: 'Asset',
              icon: Package,
              items: [
                {
                  title: 'Assets',
                  url: '/assets',
                },
                {
                  title: 'Asset Types',
                  url: '/asset-types',
                },
                {
                  title: 'Asset Categories',
                  url: '/asset-categories',
                },
                {
                  title: 'Asset Tag Generators',
                  url: '/asset-tag-generators',
                },
                {
                  title: 'Movement Types',
                  url: '/movement-types',
                },
              ],
            },
            {
              title: 'Activity',
              icon: Activity,
              items: [
                {
                  title: 'Activity Master',
                  url: '/activities',
                },
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
              title: 'Sites',
              icon: MapPinned,
              items: [
                {
                  title: 'Sites',
                  url: '/sites',
                },
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
              title: 'Person',
              icon: UserCircle,
              items: [
                {
                  title: 'Person Types',
                  url: '/person-types',
                },
                {
                  title: 'Person Details',
                  url: '/person-details',
                },
              ],
            },
            {
              title: 'Status',
              icon: CheckCircle,
              items: [
                {
                  title: 'Status Types',
                  url: '/generic-status-types',
                },
              ],
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
