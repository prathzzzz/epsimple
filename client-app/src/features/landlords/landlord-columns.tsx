import { ColumnDef } from '@tanstack/react-table';
import { ArrowUpDown } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Landlord } from '@/features/landlords/api/landlords-api';
import { LandlordRowActions } from './landlord-row-actions';

export const landlordColumns: ColumnDef<Landlord>[] = [
  {
    accessorKey: 'landlordName',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Landlord Name
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => <div>{row.getValue('landlordName')}</div>,
  },
  {
    accessorKey: 'landlordPhone',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Phone
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => {
      const phone = row.getValue('landlordPhone') as string | undefined;
      return <div>{phone || '-'}</div>;
    },
  },
  {
    accessorKey: 'rentSharePercentage',
    header: ({ column }) => {
      return (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === 'asc')}
        >
          Rent Share %
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      );
    },
    cell: ({ row }) => {
      const percentage = row.getValue('rentSharePercentage') as number | undefined;
      return <div>{percentage !== undefined && percentage !== null ? `${percentage}%` : '-'}</div>;
    },
  },
  {
    id: 'actions',
    cell: ({ row }) => <LandlordRowActions landlord={row.original} />,
  },
];
