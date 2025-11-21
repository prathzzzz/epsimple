import { MoreHorizontal, Pencil, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { toast } from 'sonner';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { ConfirmDialog } from '@/components/confirm-dialog';
import { PermissionGuard } from '@/components/permission-guard';

import { useLandlordContext } from './landlord-provider';
import { type Landlord, useDeleteLandlord } from '@/features/landlords/api/landlords-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

interface LandlordRowActionsProps {
  landlord: Landlord;
}

export const LandlordRowActions = ({ landlord }: LandlordRowActionsProps) => {
  const { openEditDrawer } = useLandlordContext();
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const deleteLandlord = useDeleteLandlord();
  const handleError = useErrorHandler();

  const handleEdit = () => {
    openEditDrawer(landlord);
  };

  const handleDelete = async () => {
    try {
      await deleteLandlord.mutateAsync(landlord.id);
      toast.success('Landlord deleted successfully');
      setIsDeleteDialogOpen(false);
    } catch (error) {
      const { message } = handleError.handleError(error);
      toast.error(message);
    }
  };

  return (
    <>
      <PermissionGuard anyPermissions={['LANDLORD:UPDATE', 'LANDLORD:DELETE']}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="h-8 w-8 p-0">
              <span className="sr-only">Open menu</span>
              <MoreHorizontal className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuLabel>Actions</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <PermissionGuard permission="LANDLORD:UPDATE">
              <DropdownMenuItem onClick={handleEdit}>
                <Pencil className="mr-2 h-4 w-4" />
                Edit
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="LANDLORD:DELETE">
              <DropdownMenuItem
                onClick={() => setIsDeleteDialogOpen(true)}
                className="text-destructive focus:text-destructive"
              >
                <Trash2 className="mr-2 h-4 w-4" />
                Delete
              </DropdownMenuItem>
            </PermissionGuard>
          </DropdownMenuContent>
        </DropdownMenu>
      </PermissionGuard>

      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        handleConfirm={handleDelete}
        title="Delete Landlord"
        desc={`Are you sure you want to delete "${landlord.landlordName}"? This action cannot be undone.`}
        confirmText="Delete"
        isLoading={deleteLandlord.isPending}
        destructive
      />
    </>
  );
};
