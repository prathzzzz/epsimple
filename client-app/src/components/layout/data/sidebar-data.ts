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
  UserCheck,
  Tags,
  Layers,
  Package,
  ArrowRightLeft,
  DollarSign,
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
              title: 'Banks',
              url: '/banks',
              icon: Landmark,
            },
            {
              title: 'States',
              url: '/states',
              icon: MapPin,
            },
            {
              title: 'Person Types',
              url: '/person-types',
              icon: UserCheck,
            },
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
            {
              title: 'Cost Categories',
              url: '/cost-categories',
              icon: DollarSign,
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
