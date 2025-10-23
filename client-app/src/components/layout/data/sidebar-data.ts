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
                  title: 'Cost Categories',
                  url: '/cost-categories',
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
              ],
            },
            {
              title: 'Asset',
              icon: Package,
              items: [
                {
                  title: 'Asset Types',
                  url: '/asset-types',
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
              ],
            },
            {
              title: 'Sites',
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
              ],
            },
            {
              title: 'System',
              icon: Database,
              items: [
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
