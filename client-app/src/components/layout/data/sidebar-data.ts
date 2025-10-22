import {
  Construction,
  LayoutDashboard,
  Bug,
  ListTodo,
  FileX,
  Lock,
  ServerOff,
  UserX,
  Users,
  ShieldCheck,
  Database,
  Landmark,
  MapPin,
  Package,
  Activity,
  Building2,
  UserCheck,
  CircleDot,
  Tags,
  Layers,
  ArrowRightLeft,
  DollarSign,
  CreditCard,
  UserCog,
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
                  icon: MapPin,
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
                  icon: Landmark,
                },
                {
                  title: 'Cost Categories',
                  url: '/cost-categories',
                  icon: DollarSign,
                },
                {
                  title: 'Payment Methods',
                  url: '/payment-methods',
                  icon: CreditCard,
                },
                {
                  title: 'Payee Types',
                  url: '/payee-types',
                  icon: UserCog,
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
                  icon: Tags,
                },
                {
                  title: 'Vendor Types',
                  url: '/vendor-types',
                  icon: Layers,
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
                  icon: Package,
                },
                {
                  title: 'Movement Types',
                  url: '/movement-types',
                  icon: ArrowRightLeft,
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
                  icon: Activity,
                },
                {
                  title: 'Activities',
                  url: '/activities-list',
                  icon: Activity,
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
                  icon: UserCheck,
                },
                {
                  title: 'Status Types',
                  url: '/generic-status-types',
                  icon: CircleDot,
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
              icon: Lock,
            },
            {
              title: 'Forbidden',
              url: '/errors/forbidden',
              icon: UserX,
            },
            {
              title: 'Not Found',
              url: '/errors/not-found',
              icon: FileX,
            },
            {
              title: 'Internal Server Error',
              url: '/errors/internal-server-error',
              icon: ServerOff,
            },
            {
              title: 'Maintenance Error',
              url: '/errors/maintenance-error',
              icon: Construction,
            },
          ],
        },
      ],
    },
  ],
}
